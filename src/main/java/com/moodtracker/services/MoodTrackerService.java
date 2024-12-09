package com.moodtracker.services;

import com.moodtracker.models.Mood;

import java.util.List;

public interface MoodTrackerService {
    void addMood(Mood mood);

    List<Mood> viewMoods();

    boolean editMood(String date, String newMood, String newNotes);

    boolean deleteMood(String date);

    Mood searchMoodByDate(String date);

    String weeklySummary();
}
