package br.com.firstingressos.dashboard.services;

import android.content.Context;
import android.util.Log;

import io.supabase.gotrue.GoTrueClient;
import io.supabase.postgrest.PostgrestClient;
import io.supabase.realtime.RealtimeClient;
import io.supabase.realtime.SupabaseRealtimeClient;
import io.supabase.storage.StorageClient;
import io.supabase.SupabaseClient;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import br.com.firstingressos.dashboard.models.Transaction;
import br.com.firstingressos.dashboard.models.Terminal;
import br.com.firstingressos.dashboard.models.Event;
import br.com.firstingressos.dashboard.models.Ticket;

public class SupabaseService {
    private static SupabaseService instance;
    private final SupabaseClient supabaseClient;
    private final RealtimeClient realtimeClient;
    
    // Credenciais do Supabase
    private static final String SUPABASE_URL = "https://ilgrwnmvciwwdfcupefr.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlsZ3J3bm12Y2l3d2RmY3VwZWZyIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczNDMyMjIxNywiZXhwIjoyMDQ5ODk4MjE3fQ.aqqCCP2CouVEAYYRUxK78NgwNT_AMqIs1mw-QrQA4pI";

    private SupabaseService() {
        // Inicializa o cliente Supabase
        supabaseClient = new SupabaseClient(SUPABASE_URL, SUPABASE_KEY);
        
        // Inicializa o cliente Realtime
        realtimeClient = supabaseClient.getRealtimeClient();
        
        // Configura SSL
        supabaseClient.getPostgrestClient().setSSLMode(true);
        
        // Inicia as inscrições realtime
        setupRealtimeSubscriptions();
    }

    public static synchronized SupabaseService getInstance() {
        if (instance == null) {
            instance = new SupabaseService();
        }
        return instance;
    }

    // Configuração de Realtime
    private void setupRealtimeSubscriptions() {
        // Inscreve para mudanças em transações
        realtimeClient.createChannel("public:transactions")
            .on("INSERT", payload -> {
                Log.d("Supabase", "Nova transação: " + payload.toString());
                notifyTransactionListeners("INSERT", payload);
            })
            .on("UPDATE", payload -> {
                Log.d("Supabase", "Transação atualizada: " + payload.toString());
                notifyTransactionListeners("UPDATE", payload);
            })
            .subscribe();

        // Inscreve para mudanças em terminais
        realtimeClient.createChannel("public:terminals")
            .on("*", payload -> {
                Log.d("Supabase", "Mudança em terminal: " + payload.toString());
                notifyTerminalListeners(payload);
            })
            .subscribe();

        // Inscreve para mudanças em eventos
        realtimeClient.createChannel("public:events")
            .on("*", payload -> {
                Log.d("Supabase", "Mudança em evento: " + payload.toString());
                notifyEventListeners(payload);
            })
            .subscribe();

        // Inscreve para mudanças em ingressos
        realtimeClient.createChannel("public:tickets")
            .on("UPDATE", payload -> {
                Log.d("Supabase", "Ingresso atualizado: " + payload.toString());
                notifyTicketListeners(payload);
            })
            .subscribe();
    }

    // Métodos para Transações
    public void saveTransaction(Transaction transaction, TransactionCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", transaction.getUserId());
        data.put("terminal_id", transaction.getTerminalId());
        data.put("amount", transaction.getAmount());
        data.put("payment_type", transaction.getPaymentType());
        data.put("status", transaction.getStatus());
        data.put("installments", transaction.getInstallments());
        data.put("stone_transaction_id", transaction.getStoneTransactionId());

        getDatabase()
            .from("transactions")
            .insert(data)
            .execute(response -> {
                if (response.getData() != null) {
                    Log.d("Supabase", "Transação salva com sucesso");
                    callback.onSuccess(transaction);
                } else {
                    Log.e("Supabase", "Erro ao salvar transação: " + response.getError());
                    callback.onError(response.getError().getMessage());
                }
            });
    }

    public void getTransactions(String userId, TransactionCallback callback) {
        getDatabase()
            .from("transactions")
            .select()
            .eq("user_id", userId)
            .order("created_at", QueryOrder.DESCENDING)
            .execute(response -> {
                if (response.getData() != null) {
                    List<Transaction> transactions = response.getData();
                    callback.onSuccess(transactions);
                } else {
                    callback.onError(response.getError().getMessage());
                }
            });
    }

    // Métodos para Terminais
    public void saveTerminal(Terminal terminal, TerminalCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", terminal.getUserId());
        data.put("serial", terminal.getSerial());
        data.put("model", terminal.getModel());
        data.put("status", terminal.getStatus());
        data.put("activation_code", terminal.getActivationCode());
        data.put("config", terminal.getConfig());
        data.put("is_active", terminal.isActive());

        getDatabase()
            .from("terminals")
            .insert(data)
            .execute(response -> {
                if (response.getData() != null) {
                    Log.d("Supabase", "Terminal salvo com sucesso");
                    callback.onSuccess(terminal);
                } else {
                    Log.e("Supabase", "Erro ao salvar terminal: " + response.getError());
                    callback.onError(response.getError().getMessage());
                }
            });
    }

    public void updateTerminalStatus(String terminalId, String status, TerminalCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", status);
        data.put("last_seen", new Date());

        getDatabase()
            .from("terminals")
            .update(data)
            .eq("id", terminalId)
            .execute(response -> {
                if (response.getData() != null) {
                    Log.d("Supabase", "Status do terminal atualizado");
                    callback.onSuccess(null);
                } else {
                    callback.onError(response.getError().getMessage());
                }
            });
    }

    // Métodos para Eventos
    public void saveEvent(Event event, EventCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", event.getUserId());
        data.put("name", event.getName());
        data.put("description", event.getDescription());
        data.put("date", event.getDate());
        data.put("location", event.getLocation());
        data.put("price", event.getPrice());
        data.put("total_tickets", event.getTotalTickets());
        data.put("available_tickets", event.getAvailableTickets());

        getDatabase()
            .from("events")
            .insert(data)
            .execute(response -> {
                if (response.getData() != null) {
                    Log.d("Supabase", "Evento salvo com sucesso");
                    callback.onSuccess(event);
                } else {
                    callback.onError(response.getError().getMessage());
                }
            });
    }

    public void updateEventTickets(String eventId, int availableTickets, EventCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("available_tickets", availableTickets);

        getDatabase()
            .from("events")
            .update(data)
            .eq("id", eventId)
            .execute(response -> {
                if (response.getData() != null) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(response.getError().getMessage());
                }
            });
    }

    // Métodos para Ingressos
    public void saveTicket(Ticket ticket, TicketCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("event_id", ticket.getEventId());
        data.put("user_id", ticket.getUserId());
        data.put("transaction_id", ticket.getTransactionId());
        data.put("status", ticket.getStatus());
        data.put("qr_code", ticket.getQrCode());

        getDatabase()
            .from("tickets")
            .insert(data)
            .execute(response -> {
                if (response.getData() != null) {
                    Log.d("Supabase", "Ingresso salvo com sucesso");
                    callback.onSuccess(ticket);
                } else {
                    callback.onError(response.getError().getMessage());
                }
            });
    }

    public void validateTicket(String ticketId, TicketCallback callback) {
        getDatabase()
            .from("tickets")
            .select()
            .eq("id", ticketId)
            .single()
            .execute(response -> {
                if (response.getData() != null) {
                    Ticket ticket = response.getData();
                    if (!ticket.isValidated()) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("status", "VALIDATED");
                        data.put("validated_at", new Date());

                        getDatabase()
                            .from("tickets")
                            .update(data)
                            .eq("id", ticketId)
                            .execute(updateResponse -> {
                                if (updateResponse.getData() != null) {
                                    callback.onSuccess(ticket);
                                } else {
                                    callback.onError("Erro ao validar ingresso");
                                }
                            });
                    } else {
                        callback.onError("Ingresso já validado");
                    }
                } else {
                    callback.onError("Ingresso não encontrado");
                }
            });
    }

    // Interfaces de Callback
    public interface TransactionCallback {
        void onSuccess(Transaction transaction);
        void onError(String error);
    }

    public interface TerminalCallback {
        void onSuccess(Terminal terminal);
        void onError(String error);
    }

    public interface EventCallback {
        void onSuccess(Event event);
        void onError(String error);
    }

    public interface TicketCallback {
        void onSuccess(Ticket ticket);
        void onError(String error);
    }

    // Listeners para Realtime
    private List<TransactionCallback> transactionListeners = new ArrayList<>();
    private List<TerminalCallback> terminalListeners = new ArrayList<>();
    private List<EventCallback> eventListeners = new ArrayList<>();
    private List<TicketCallback> ticketListeners = new ArrayList<>();

    public void addTransactionListener(TransactionCallback listener) {
        transactionListeners.add(listener);
    }

    public void addTerminalListener(TerminalCallback listener) {
        terminalListeners.add(listener);
    }

    public void addEventListener(EventCallback listener) {
        eventListeners.add(listener);
    }

    public void addTicketListener(TicketCallback listener) {
        ticketListeners.add(listener);
    }

    private void notifyTransactionListeners(String event, Object payload) {
        for (TransactionCallback listener : transactionListeners) {
            // Converte payload para Transaction
            Transaction transaction = convertPayloadToTransaction(payload);
            listener.onSuccess(transaction);
        }
    }

    private void notifyTerminalListeners(Object payload) {
        for (TerminalCallback listener : terminalListeners) {
            Terminal terminal = convertPayloadToTerminal(payload);
            listener.onSuccess(terminal);
        }
    }

    private void notifyEventListeners(Object payload) {
        for (EventCallback listener : eventListeners) {
            Event event = convertPayloadToEvent(payload);
            listener.onSuccess(event);
        }
    }

    private void notifyTicketListeners(Object payload) {
        for (TicketCallback listener : ticketListeners) {
            Ticket ticket = convertPayloadToTicket(payload);
            listener.onSuccess(ticket);
        }
    }

    // Métodos auxiliares para conversão de payload
    private Transaction convertPayloadToTransaction(Object payload) {
        // Implementar conversão do payload para Transaction
        return null;
    }

    private Terminal convertPayloadToTerminal(Object payload) {
        // Implementar conversão do payload para Terminal
        return null;
    }

    private Event convertPayloadToEvent(Object payload) {
        // Implementar conversão do payload para Event
        return null;
    }

    private Ticket convertPayloadToTicket(Object payload) {
        // Implementar conversão do payload para Ticket
        return null;
    }

    private PostgrestClient getDatabase() {
        return supabaseClient.getPostgrestClient();
    }
}
