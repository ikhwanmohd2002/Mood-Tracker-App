package com.taskapp.models;

public class Task {
    private int id;
    private String description;
    private String status; // e.g., "pending", "completed"
    private String deadline; // e.g., "dd-MM-yyyy" or "no deadline"

    // Default constructor for JSON deserialization
    public Task() {
    }

    public Task(int id, String description, String status, String deadline) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Description: %s | Status: %s | Deadline: %s",
                id, description, status, deadline);
    }
}
