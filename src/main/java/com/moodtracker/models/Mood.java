package com.moodtracker.models;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Mood {
    private Date date;
    private String mood;
    private String notes;

    // Constructor
    public Mood(Date date, String mood, String notes) {
        this.date = date;
        this.mood = mood;
        this.notes = notes;
    }

    // Getters and Setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    @Override
    public String toString() {
        return "Date: " + getFormattedDate() + ", Mood: " + mood + ", Notes: " + (notes.isEmpty() ? "None" : notes);
    }

}
