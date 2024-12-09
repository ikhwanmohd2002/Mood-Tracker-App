package com.moodtracker.services;

import com.moodtracker.models.Mood;

import java.util.List;

public interface MoodTrackerService {
    void addMood(Mood mood);

    List<Mood> viewMoods(); // View all moods with IDs

    boolean editMoodById(int id, String newMood, String newNotes); // Edit using ID

    boolean deleteMoodById(int id); // Delete using ID

    Mood searchMoodByDate(String date); // Search using Date

    String weeklySummary();
}
