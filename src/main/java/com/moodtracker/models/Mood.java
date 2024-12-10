package com.moodtracker.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Mood {
    private int id; // New ID field
    private LocalDate date;
    private String mood;
    private String notes;

    // Constructor with ID
    public Mood(int id, LocalDate date, String mood, String notes) {
        this.id = id;
        this.date = date;
        this.mood = mood;
        this.notes = notes;
    }

    // Constructor without ID (for adding moods)
    public Mood(LocalDate date, String mood, String notes) {
        this(0, date, mood, notes); // ID defaults to 0 (ignored by DB during insertion)
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return "ID: " + id + ", Date: " + date.format(formatter) + ", Mood: " + mood + ", Notes: "
                + (notes == null || notes.isEmpty() ? "None" : notes);
    }

}
