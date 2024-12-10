package com.moodtracker.services;

import com.moodtracker.models.Mood;
import java.time.LocalDate;
import java.util.List;

public interface MoodTrackerService {
    Mood addMood(Mood mood);
    List<Mood> getAllMoods();
    Mood getMoodById(Long id);
    Mood getMoodByDate(LocalDate date);
    Mood updateMood(Long id, Mood mood);
    void deleteMood(Long id);
    List<Mood> getWeeklyMoods();
    String getWeeklySummary();
}
