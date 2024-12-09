package com.moodtracker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Date;

import com.moodtracker.implementations.MoodTrackerServiceImpl;
import com.moodtracker.models.Mood;
import com.moodtracker.services.MoodTrackerService;
import com.moodtracker.Database;

public class Main {
    private static final MoodTrackerService moodService = new MoodTrackerServiceImpl();

    public static void main(String[] args) {
        com.moodtracker.Database.initialize();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Mood Tracker ===");
            System.out.println("1. Add Mood");
            System.out.println("2. View Moods");
            System.out.println("3. Edit Mood");
            System.out.println("4. Delete Mood");
            System.out.println("5. Search Mood by Date");
            System.out.println("6. Weekly Summary");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addMood(scanner);
                case 2 -> viewMoods();
                case 3 -> editMood(scanner);
                case 4 -> deleteMood(scanner);
                case 5 -> searchMood(scanner);
                case 6 -> weeklySummary();
                case 7 -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addMood(Scanner scanner) {
        System.out.print("Enter date (DD-MM-YYYY): ");
        String dateInput = scanner.nextLine().trim(); // Trim any extra whitespace

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(dateInput, formatter); // Parse the date

            System.out.print("Enter mood: ");
            String mood = scanner.nextLine();
            System.out.print("Enter notes (optional): ");
            String notes = scanner.nextLine();


            moodService.addMood(new Mood(date, mood, notes));
            System.out.println("Mood added successfully!");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY format.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void viewMoods() {
        for (Mood mood : moodService.viewMoods()) {
            System.out.println(mood);
        }
    }

    private static void editMood(Scanner scanner) {
        System.out.print("Enter Mood ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new mood: ");
        String newMood = scanner.nextLine();
        System.out.print("Enter new notes: ");
        String newNotes = scanner.nextLine();

        if (moodService.editMoodById(id, newMood, newNotes)) {
            System.out.println("Mood updated!");
        } else {
            System.out.println("Mood not found.");
        }
    }

    private static void deleteMood(Scanner scanner) {
        System.out.print("Enter Mood ID to delete: ");
        int id = scanner.nextInt();

        if (moodService.deleteMoodById(id)) {
            System.out.println("Mood deleted!");
        } else {
            System.out.println("Mood not found.");
        }
    }

    private static void searchMood(Scanner scanner) {
        System.out.print("Enter date (DD-MM-YYYY) to search: ");
        String dateInput = scanner.nextLine();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(dateInput, formatter);
            Mood mood = moodService.searchMoodByDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // Pass database-compatible format
            System.out.println(mood != null ? mood : "No mood found for the specified date.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
        }
    }

    private static void weeklySummary() {
        System.out.println(moodService.weeklySummary());
    }
}
