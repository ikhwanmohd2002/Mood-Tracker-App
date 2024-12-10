package com.moodtracker.services;

import com.moodtracker.models.Mood;

import java.time.LocalDate;
import java.util.List;

public interface MoodTrackerService {
    void addMood(Mood mood);
    List<Mood> viewMoods();
    boolean editMoodById(int id, String newMood, String newNotes, LocalDate newDate);
    Mood searchMoodById(int id);
    boolean deleteMoodById(int id);
    Mood searchMoodByDate(String date);
    String weeklySummary();
}


