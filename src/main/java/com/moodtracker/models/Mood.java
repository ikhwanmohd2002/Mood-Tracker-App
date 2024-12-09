package com.moodtracker.models;

public class Mood {
    private String date;
    private String mood;
    private String notes;

    // Constructor
    public Mood(String date, String mood, String notes) {
        this.date = date;
        this.mood = mood;
        this.notes = notes;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
        return "Date: " + date + ", Mood: " + mood + ", Notes: " + (notes.isEmpty() ? "None" : notes);
    }
}
