package br.com.firstingressos.dashboard.models;

import java.util.Date;
import java.util.UUID;

public class Event {
    private String id;
    private String userId;
    private String name;
    private String description;
    private Date date;
    private String location;
    private int price;
    private int totalTickets;
    private int availableTickets;
    private Date createdAt;

    private Event() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = new Date();
    }

    public static class Builder {
        private Event event;

        public Builder() {
            event = new Event();
        }

        public Builder setUserId(String userId) {
            event.userId = userId;
            return this;
        }

        public Builder setName(String name) {
            event.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            event.description = description;
            return this;
        }

        public Builder setDate(Date date) {
            event.date = date;
            return this;
        }

        public Builder setLocation(String location) {
            event.location = location;
            return this;
        }

        public Builder setPrice(int price) {
            event.price = price;
            return this;
        }

        public Builder setTotalTickets(int totalTickets) {
            event.totalTickets = totalTickets;
            return this;
        }

        public Builder setAvailableTickets(int availableTickets) {
            event.availableTickets = availableTickets;
            return this;
        }

        public Event build() {
            return event;
        }
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Date getDate() { return date; }
    public String getLocation() { return location; }
    public int getPrice() { return price; }
    public int getTotalTickets() { return totalTickets; }
    public int getAvailableTickets() { return availableTickets; }
    public Date getCreatedAt() { return createdAt; }

    // Métodos de negócio
    public boolean hasAvailableTickets() {
        return availableTickets > 0;
    }

    public void decrementAvailableTickets() {
        if (hasAvailableTickets()) {
            availableTickets--;
        }
    }

    public boolean isUpcoming() {
        return date.after(new Date());
    }
}
