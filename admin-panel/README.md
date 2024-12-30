# First Ingressos - Painel Administrativo

Painel administrativo para gerenciamento de eventos, terminais, transações e ingressos.

## Funcionalidades

- Dashboard com métricas em tempo real
- Gerenciamento de eventos
- Monitoramento de terminais
- Visualização de transações
- Validação de ingressos
- Relatórios de vendas e eventos

## Tecnologias

- React 18
- Material UI 5
- Supabase
- React Router
- Recharts
- Date-fns

## Instalação

1. Clone o repositório
2. Instale as dependências:
```bash
npm install
```

3. Inicie o servidor de desenvolvimento:
```bash
npm start
```

## Estrutura do Projeto

```
src/
  ├── components/     # Componentes reutilizáveis
  ├── pages/         # Páginas da aplicação
  ├── services/      # Serviços e APIs
  └── App.js         # Componente principal
```

## Páginas

- `/` - Dashboard com métricas
- `/events` - Gerenciamento de eventos
- `/terminals` - Monitoramento de terminais
- `/transactions` - Histórico de transações
- `/tickets` - Gerenciamento de ingressos
- `/reports` - Relatórios e análises

## Segurança

- Autenticação via Supabase
- Row Level Security (RLS)
- SSL/TLS para todas as requisições
- Validação de dados

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Crie um Pull Request
