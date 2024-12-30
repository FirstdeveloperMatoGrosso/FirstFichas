-- Habilita RLS para todas as tabelas
ALTER TABLE transactions ENABLE ROW LEVEL SECURITY;
ALTER TABLE terminals ENABLE ROW LEVEL SECURITY;
ALTER TABLE events ENABLE ROW LEVEL SECURITY;
ALTER TABLE tickets ENABLE ROW LEVEL SECURITY;

-- Política para Transações
CREATE POLICY "Transações visíveis apenas para o próprio usuário e admin"
ON transactions
FOR SELECT
USING (
    auth.uid() = user_id 
    OR 
    auth.jwt()->>'role' = 'admin'
);

CREATE POLICY "Apenas usuários autenticados podem criar transações"
ON transactions
FOR INSERT
WITH CHECK (
    auth.role() = 'authenticated'
    AND
    auth.uid() = user_id
);

CREATE POLICY "Apenas admin pode atualizar transações"
ON transactions
FOR UPDATE
USING (auth.jwt()->>'role' = 'admin');

-- Política para Terminais
CREATE POLICY "Terminais visíveis apenas para o próprio usuário e admin"
ON terminals
FOR SELECT
USING (
    auth.uid() = user_id 
    OR 
    auth.jwt()->>'role' = 'admin'
);

CREATE POLICY "Apenas admin pode criar terminais"
ON terminals
FOR INSERT
WITH CHECK (auth.jwt()->>'role' = 'admin');

CREATE POLICY "Apenas admin pode atualizar terminais"
ON terminals
FOR UPDATE
USING (auth.jwt()->>'role' = 'admin');

CREATE POLICY "Apenas admin pode deletar terminais"
ON terminals
FOR DELETE
USING (auth.jwt()->>'role' = 'admin');

-- Política para Eventos
CREATE POLICY "Eventos são visíveis para todos"
ON events
FOR SELECT
USING (true);

CREATE POLICY "Apenas admin pode criar eventos"
ON events
FOR INSERT
WITH CHECK (auth.jwt()->>'role' = 'admin');

CREATE POLICY "Apenas admin pode atualizar eventos"
ON events
FOR UPDATE
USING (auth.jwt()->>'role' = 'admin');

CREATE POLICY "Apenas admin pode deletar eventos"
ON events
FOR DELETE
USING (auth.jwt()->>'role' = 'admin');

-- Política para Ingressos
CREATE POLICY "Ingressos visíveis apenas para o próprio usuário, organizador e admin"
ON tickets
FOR SELECT
USING (
    auth.uid() = user_id 
    OR 
    auth.jwt()->>'role' = 'admin'
    OR
    EXISTS (
        SELECT 1 FROM events e 
        WHERE e.id = tickets.event_id 
        AND e.user_id = auth.uid()
    )
);

CREATE POLICY "Apenas usuários autenticados podem comprar ingressos"
ON tickets
FOR INSERT
WITH CHECK (
    auth.role() = 'authenticated'
    AND
    auth.uid() = user_id
    AND
    EXISTS (
        SELECT 1 FROM events e 
        WHERE e.id = event_id 
        AND e.available_tickets > 0
    )
);

CREATE POLICY "Apenas admin e organizador podem validar ingressos"
ON tickets
FOR UPDATE
USING (
    auth.jwt()->>'role' = 'admin'
    OR
    EXISTS (
        SELECT 1 FROM events e 
        WHERE e.id = tickets.event_id 
        AND e.user_id = auth.uid()
    )
);

-- Funções auxiliares
CREATE OR REPLACE FUNCTION is_admin()
RETURNS boolean
LANGUAGE sql SECURITY DEFINER
AS $$
  SELECT auth.jwt()->>'role' = 'admin';
$$;

CREATE OR REPLACE FUNCTION is_event_organizer(event_id uuid)
RETURNS boolean
LANGUAGE sql SECURITY DEFINER
AS $$
  SELECT EXISTS (
    SELECT 1 FROM events e 
    WHERE e.id = event_id 
    AND e.user_id = auth.uid()
  );
$$;

-- Triggers para atualização automática
CREATE OR REPLACE FUNCTION update_event_tickets()
RETURNS TRIGGER
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        -- Quando um ingresso é vendido, diminui a quantidade disponível
        UPDATE events 
        SET available_tickets = available_tickets - 1
        WHERE id = NEW.event_id 
        AND available_tickets > 0;
        
        IF NOT FOUND THEN
            RAISE EXCEPTION 'Não há ingressos disponíveis';
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        -- Quando um ingresso é cancelado, aumenta a quantidade disponível
        UPDATE events 
        SET available_tickets = available_tickets + 1
        WHERE id = OLD.event_id;
    END IF;
    RETURN NULL;
END;
$$;

CREATE TRIGGER tickets_update_event
AFTER INSERT OR DELETE ON tickets
FOR EACH ROW
EXECUTE FUNCTION update_event_tickets();

-- Trigger para validação de ingressos
CREATE OR REPLACE FUNCTION validate_ticket()
RETURNS TRIGGER
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
BEGIN
    IF NEW.status = 'VALIDATED' AND OLD.status != 'VALIDATED' THEN
        -- Verifica se o ingresso já foi validado
        IF OLD.status = 'VALIDATED' THEN
            RAISE EXCEPTION 'Ingresso já foi validado';
        END IF;
        
        -- Verifica se o evento já aconteceu
        IF EXISTS (
            SELECT 1 FROM events e 
            WHERE e.id = NEW.event_id 
            AND e.date < NOW()
        ) THEN
            RAISE EXCEPTION 'Evento já aconteceu';
        END IF;
        
        -- Registra data de validação
        NEW.validated_at = NOW();
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER ticket_validation
BEFORE UPDATE ON tickets
FOR EACH ROW
WHEN (NEW.status = 'VALIDATED')
EXECUTE FUNCTION validate_ticket();

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_terminals_user_id ON terminals(user_id);
CREATE INDEX IF NOT EXISTS idx_events_user_id ON events(user_id);
CREATE INDEX IF NOT EXISTS idx_tickets_user_id ON tickets(user_id);
CREATE INDEX IF NOT EXISTS idx_tickets_event_id ON tickets(event_id);
CREATE INDEX IF NOT EXISTS idx_events_date ON events(date);
CREATE INDEX IF NOT EXISTS idx_tickets_status ON tickets(status);

-- Comentários nas tabelas
COMMENT ON TABLE transactions IS 'Transações de pagamento';
COMMENT ON TABLE terminals IS 'Terminais de pagamento Stone';
COMMENT ON TABLE events IS 'Eventos disponíveis';
COMMENT ON TABLE tickets IS 'Ingressos vendidos';
