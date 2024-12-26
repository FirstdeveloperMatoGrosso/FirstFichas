package br.com.firstingressos.dashboard.models;

import java.util.Date;
import java.util.UUID;

public class Terminal {
    private String id;
    private String userId;
    private String serial;
    private String model;
    private String status;
    private String activationCode;
    private Date lastSeen;
    private String config;
    private boolean isActive;
    private String lastError;
    private Date createdAt;

    private Terminal() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = new Date();
    }

    public static class Builder {
        private Terminal terminal;

        public Builder() {
            terminal = new Terminal();
        }

        public Builder setUserId(String userId) {
            terminal.userId = userId;
            return this;
        }

        public Builder setSerial(String serial) {
            terminal.serial = serial;
            return this;
        }

        public Builder setModel(String model) {
            terminal.model = model;
            return this;
        }

        public Builder setStatus(String status) {
            terminal.status = status;
            return this;
        }

        public Builder setActivationCode(String activationCode) {
            terminal.activationCode = activationCode;
            return this;
        }

        public Builder setLastSeen(Date lastSeen) {
            terminal.lastSeen = lastSeen;
            return this;
        }

        public Builder setConfig(String config) {
            terminal.config = config;
            return this;
        }

        public Builder setActive(boolean active) {
            terminal.isActive = active;
            return this;
        }

        public Builder setLastError(String lastError) {
            terminal.lastError = lastError;
            return this;
        }

        public Terminal build() {
            return terminal;
        }
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getSerial() { return serial; }
    public String getModel() { return model; }
    public String getStatus() { return status; }
    public String getActivationCode() { return activationCode; }
    public Date getLastSeen() { return lastSeen; }
    public String getConfig() { return config; }
    public boolean isActive() { return isActive; }
    public String getLastError() { return lastError; }
    public Date getCreatedAt() { return createdAt; }
}
