package br.com.firstingressos.dashboard.models;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private String id;
    private String userId;
    private String terminalId;
    private int amount;
    private String paymentType;
    private String status;
    private int installments;
    private String stoneTransactionId;
    private Date createdAt;

    private Transaction() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = new Date();
    }

    public static class Builder {
        private Transaction transaction;

        public Builder() {
            transaction = new Transaction();
        }

        public Builder setUserId(String userId) {
            transaction.userId = userId;
            return this;
        }

        public Builder setTerminalId(String terminalId) {
            transaction.terminalId = terminalId;
            return this;
        }

        public Builder setAmount(int amount) {
            transaction.amount = amount;
            return this;
        }

        public Builder setPaymentType(String paymentType) {
            transaction.paymentType = paymentType;
            return this;
        }

        public Builder setStatus(String status) {
            transaction.status = status;
            return this;
        }

        public Builder setInstallments(int installments) {
            transaction.installments = installments;
            return this;
        }

        public Builder setStoneTransactionId(String stoneTransactionId) {
            transaction.stoneTransactionId = stoneTransactionId;
            return this;
        }

        public Transaction build() {
            return transaction;
        }
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTerminalId() { return terminalId; }
    public int getAmount() { return amount; }
    public String getPaymentType() { return paymentType; }
    public String getStatus() { return status; }
    public int getInstallments() { return installments; }
    public String getStoneTransactionId() { return stoneTransactionId; }
    public Date getCreatedAt() { return createdAt; }
}
