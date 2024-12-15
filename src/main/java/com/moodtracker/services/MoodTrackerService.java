package com.moodtracker.services;

import java.time.LocalDate;
import java.util.List;

import com.moodtracker.models.Mood;

public interface MoodTrackerService {

    Mood addMood(Mood mood);

    List<Mood> getAllMoods();

    Mood getMoodById(Long id);

    List<Mood> getMoodByDate(LocalDate date);

    Mood updateMood(Long id, Mood mood);

    void deleteMood(Long id);

    List<Mood> getWeeklyMoods();

    String getWeeklySummary();
}
