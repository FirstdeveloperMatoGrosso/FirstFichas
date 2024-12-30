package br.com.firstingressos.dashboard.models;

import java.util.Date;
import java.util.UUID;

public class Ticket {
    private String id;
    private String eventId;
    private String userId;
    private String transactionId;
    private String status;
    private String qrCode;
    private Date validatedAt;
    private Date createdAt;

    private Ticket() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.status = "PENDING"; // PENDING, ACTIVE, VALIDATED, CANCELLED
    }

    public static class Builder {
        private Ticket ticket;

        public Builder() {
            ticket = new Ticket();
        }

        public Builder setEventId(String eventId) {
            ticket.eventId = eventId;
            return this;
        }

        public Builder setUserId(String userId) {
            ticket.userId = userId;
            return this;
        }

        public Builder setTransactionId(String transactionId) {
            ticket.transactionId = transactionId;
            return this;
        }

        public Builder setStatus(String status) {
            ticket.status = status;
            return this;
        }

        public Builder setQrCode(String qrCode) {
            ticket.qrCode = qrCode;
            return this;
        }

        public Builder setValidatedAt(Date validatedAt) {
            ticket.validatedAt = validatedAt;
            return this;
        }

        public Ticket build() {
            // Gera QR Code se não foi definido
            if (ticket.qrCode == null) {
                ticket.qrCode = generateQRCode();
            }
            return ticket;
        }
    }

    // Getters
    public String getId() { return id; }
    public String getEventId() { return eventId; }
    public String getUserId() { return userId; }
    public String getTransactionId() { return transactionId; }
    public String getStatus() { return status; }
    public String getQrCode() { return qrCode; }
    public Date getValidatedAt() { return validatedAt; }
    public Date getCreatedAt() { return createdAt; }

    // Métodos de negócio
    public boolean isValidated() {
        return "VALIDATED".equals(status);
    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }

    public void validate() {
        if (isActive()) {
            status = "VALIDATED";
            validatedAt = new Date();
        }
    }

    public void activate() {
        if ("PENDING".equals(status)) {
            status = "ACTIVE";
        }
    }

    public void cancel() {
        if (!isValidated()) {
            status = "CANCELLED";
        }
    }

    // Gera QR Code único
    private static String generateQRCode() {
        return UUID.randomUUID().toString();
    }
}
