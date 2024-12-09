package com.moodtracker.implementations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.moodtracker.Database;
import com.moodtracker.models.Mood;
import com.moodtracker.services.MoodTrackerService;

public class MoodTrackerServiceImpl implements MoodTrackerService {
    

    @Override
    public void addMood(Mood mood) {
        String sql = "INSERT INTO moods (date, mood, notes) VALUES (?, ?, ?)";
    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(mood.getDate());

        pstmt.setString(1, formattedDate);
        pstmt.setString(2, mood.getMood());
        pstmt.setString(3, mood.getNotes());
        pstmt.executeUpdate();
        System.out.println("Mood added successfully!");
    } catch (SQLException e) {
        System.out.println("Failed to add mood: " + e.getMessage());
    }
    }

    @Override
    public List<Mood> viewMoods() {
        List<Mood> moods = new ArrayList<>();
        String sql = "SELECT date, mood, notes FROM moods ORDER BY date DESC";
    
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
            while (rs.next()) {
                Date date = formatter.parse(rs.getString("date"));
                String mood = rs.getString("mood");
                String notes = rs.getString("notes");
    
                moods.add(new Mood(date, mood, notes));
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve moods: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed to parse date: " + e.getMessage());
        }
        return moods;
    }

    @Override
    public boolean editMood(String date, String newMood, String newNotes) {
        String sql = "UPDATE moods SET mood = ?, notes = ? WHERE date = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newMood);
            pstmt.setString(2, newNotes);
            pstmt.setString(3, date);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Failed to edit mood: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteMood(String date) {
        String sql = "DELETE FROM moods WHERE date = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Failed to delete mood: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Mood searchMoodByDate(String dateInput) {
        String sql = "SELECT * FROM moods WHERE date = ?";
    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, dateInput);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(rs.getString("date"));
            String mood = rs.getString("mood");
            String notes = rs.getString("notes");

            return new Mood(date, mood, notes);
        }
    } catch (SQLException e) {
        System.out.println("Failed to search mood: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("Failed to parse date: " + e.getMessage());
    }
    return null;
    }

    @Override
    public String weeklySummary() {
        // Simplified logic for demo purposes
        String sql = "SELECT mood, COUNT(*) as count FROM moods GROUP BY mood";
        StringBuilder summary = new StringBuilder("Weekly Mood Summary:\n");

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                summary.append(rs.getString("mood")).append(": ").append(rs.getInt("count")).append("\n");
            }
        } catch (SQLException e) {
            System.out.println("Failed to generate summary: " + e.getMessage());
        }
        return summary.toString();
    }
}
