package com.moodtracker.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moodtracker.models.Mood;
import com.moodtracker.repositories.MoodRepository;
import com.moodtracker.services.MoodTrackerService;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class MoodTrackerServiceImpl implements MoodTrackerService {

    @Autowired
    private MoodRepository moodRepository;

    @Override
    public Mood addMood(Mood mood) {
        return moodRepository.save(mood);
    }

    @Override
    public List<Mood> getAllMoods() {
        return moodRepository.findAllByOrderByDateDesc();
    }

    @Override
    public Mood getMoodById(Long id) {
        return moodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mood not found with id: " + id));
    }

    @Override
    public Mood getMoodByDate(LocalDate date) {
        return moodRepository.findByDate(date)
                .orElseThrow(() -> new EntityNotFoundException("Mood not found for date: " + date));
    }

    @Override
    public Mood updateMood(Long id, Mood updatedMood) {
        Mood existingMood = getMoodById(id);
        
        existingMood.setDate(updatedMood.getDate());
        existingMood.setMood(updatedMood.getMood());
        existingMood.setNotes(updatedMood.getNotes());
        
        return moodRepository.save(existingMood);
    }

    @Override
    public void deleteMood(Long id) {
        if (!moodRepository.existsById(id)) {
            throw new EntityNotFoundException("Mood not found with id: " + id);
        }
        moodRepository.deleteById(id);
    }

    @Override
    public List<Mood> getWeeklyMoods() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        return moodRepository.findByDateBetweenOrderByDateDesc(startDate, endDate);
    }

    @Override
    public String getWeeklySummary() {
        List<Mood> weeklyMoods = getWeeklyMoods();
        
        Map<String, Long> moodCounts = weeklyMoods.stream()
                .collect(Collectors.groupingBy(Mood::getMood, Collectors.counting()));
        
        StringBuilder summary = new StringBuilder("Weekly Mood Summary:\n");
        moodCounts.forEach((mood, count) -> 
            summary.append(mood).append(": ").append(count).append("\n")
        );
        
        return summary.toString();
    }
}
