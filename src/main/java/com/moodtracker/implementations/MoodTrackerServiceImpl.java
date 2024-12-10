package com.moodtracker.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.moodtracker.Database;
import com.moodtracker.models.Mood;
import com.moodtracker.services.MoodTrackerService;

public class MoodTrackerServiceImpl implements MoodTrackerService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    

    @Override
    public void addMood(Mood mood) {
    String sql = "INSERT INTO moods (date, mood, notes) VALUES (?, ?, ?)";
    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Convert date to `yyyy-MM-dd` for database storage
        String formattedDate = mood.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

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
    String sql = "SELECT id, date, mood, notes FROM moods ORDER BY date DESC";

    try (Connection conn = Database.connect();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            int id = rs.getInt("id");

            // Parse `yyyy-MM-dd` from the database and format to `dd-MM-yyyy`
            LocalDate date = LocalDate.parse(rs.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String mood = rs.getString("mood");
            String notes = rs.getString("notes");
            moods.add(new Mood(id, date, mood, notes));
        }
    } catch (SQLException e) {
        System.out.println("Failed to retrieve moods: " + e.getMessage());
    }
    return moods;
}





    @Override
    public boolean deleteMoodById(int id) {
        String sql = "DELETE FROM moods WHERE id = ?";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Failed to delete mood: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Mood searchMoodByDate(String date) {
    String sql = "SELECT id, date, mood, notes FROM moods WHERE date = ?";
    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, date); // Pass date in `yyyy-MM-dd`
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            LocalDate parsedDate = LocalDate.parse(rs.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String mood = rs.getString("mood");
            String notes = rs.getString("notes");
            return new Mood(id, parsedDate, mood, notes);
        }
    } catch (SQLException e) {
        System.out.println("Failed to search mood: " + e.getMessage());
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

    @Override
public boolean editMoodById(int id, String newMood, String newNotes, LocalDate newDate) {
    String sql = "UPDATE moods SET mood = ?, notes = ?, date = ? WHERE id = ?";
    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Convert LocalDate to String in database format
        String formattedDate = newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        pstmt.setString(1, newMood);
        pstmt.setString(2, newNotes);
        pstmt.setString(3, formattedDate);
        pstmt.setInt(4, id);

        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Failed to edit mood: " + e.getMessage());
        return false;
    }
}





@Override
public Mood searchMoodById(int id) {
    String sql = "SELECT id, date, mood, notes FROM moods WHERE id = ?";
    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int moodId = rs.getInt("id");
            LocalDate date = LocalDate.parse(rs.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String mood = rs.getString("mood");
            String notes = rs.getString("notes");

            return new Mood(moodId, date, mood, notes);
        }
    } catch (SQLException e) {
        System.out.println("Failed to search mood by ID: " + e.getMessage());
    }
    return null;
}



}
