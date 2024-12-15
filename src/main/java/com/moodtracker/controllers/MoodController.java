package com.moodtracker.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moodtracker.models.Mood;
import com.moodtracker.services.MoodTrackerService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class MoodController {

    @Autowired
    private MoodTrackerService moodService;

    @GetMapping("/")
    public ResponseEntity<String> showInterface() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== Mood Tracker ===\n");
        menu.append("1. Add Mood\n");
        menu.append("2. View Moods\n");
        menu.append("3. Edit Mood\n");
        menu.append("4. Delete Mood\n");
        menu.append("5. Search Mood by Date\n");
        menu.append("6. Weekly Summary\n");
        menu.append("7. Exit\n");
        menu.append("Choose an option: \n\n");
        menu.append("Choose a mood:\n");
        menu.append("1 - Happy\n");
        menu.append("2 - Sad\n");
        menu.append("3 - Angry\n");
        menu.append("4 - Fear\n");
        menu.append("5 - Disgust\n");
        menu.append("Enter the number corresponding to your mood (Type 'Q' to cancel): \n");

        return ResponseEntity.ok()
                .header("Content-Type", "text/plain")
                .body(menu.toString());
    }

    @PostMapping("/api/moods")
    public ResponseEntity<Mood> addMood(@Valid @RequestBody Mood mood) {
        return new ResponseEntity<>(moodService.addMood(mood), HttpStatus.CREATED);
    }

    @GetMapping("/api/moods")
    public ResponseEntity<List<Mood>> getAllMoods() {
        return ResponseEntity.ok(moodService.getAllMoods());
    }

    @GetMapping("/api/moods/{id}")
    public ResponseEntity<Mood> getMoodById(@PathVariable Long id) {
        return ResponseEntity.ok(moodService.getMoodById(id));
    }

    @GetMapping("/api/moods/date/{date}")
    public ResponseEntity<List<Mood>> getMoodByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.ok(moodService.getMoodByDate(date));
    }

    @PutMapping("/api/moods/{id}")
    public ResponseEntity<Mood> updateMood(@PathVariable Long id, @Valid @RequestBody Mood mood) {
        return ResponseEntity.ok(moodService.updateMood(id, mood));
    }

    @DeleteMapping("/api/moods/{id}")
    public ResponseEntity<Void> deleteMood(@PathVariable Long id) {
        moodService.deleteMood(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/moods/weekly")
    public ResponseEntity<List<Mood>> getWeeklyMoods() {
        return ResponseEntity.ok(moodService.getWeeklyMoods());
    }

    @GetMapping("/api/moods/weekly/summary")
    public ResponseEntity<String> getWeeklySummary() {
        return ResponseEntity.ok(moodService.getWeeklySummary());
    }
}
