package com.moodtracker.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moodtracker.models.Mood;

@Repository
public interface MoodRepository extends JpaRepository<Mood, Long> {
    Optional<Mood> findByDate(LocalDate date);
    List<Mood> findAllByOrderByDateDesc();
    List<Mood> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);
}
