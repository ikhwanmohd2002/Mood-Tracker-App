package com.moodtracker;

import java.text.SimpleDateFormat;
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
            System.out.print("Enter date (YYYY-MM-DD): ");
    String dateInput = scanner.nextLine();

    try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dateInput);

        System.out.print("Enter mood: ");
        String mood = scanner.nextLine();
        System.out.print("Enter notes (optional): ");
        String notes = scanner.nextLine();

        moodService.addMood(new Mood(date, mood, notes));
        System.out.println("Mood added successfully!");
    } catch (Exception e) {
        System.out.println("Invalid date format. Please use YYYY-MM-DD.");
    }

    }

    private static void viewMoods() {
        for (Mood mood : moodService.viewMoods()) {
            System.out.println(mood);
        }
    }

    private static void editMood(Scanner scanner) {
        System.out.print("Enter date (YYYY-MM-DD) to edit: ");
        String date = scanner.nextLine();
        System.out.print("Enter new mood: ");
        String newMood = scanner.nextLine();
        System.out.print("Enter new notes: ");
        String newNotes = scanner.nextLine();
        if (moodService.editMood(date, newMood, newNotes)) {
            System.out.println("Mood updated!");
        } else {
            System.out.println("Mood not found.");
        }
    }

    private static void deleteMood(Scanner scanner) {
        System.out.print("Enter date (YYYY-MM-DD) to delete: ");
        String date = scanner.nextLine();
        if (moodService.deleteMood(date)) {
            System.out.println("Mood deleted!");
        } else {
            System.out.println("Mood not found.");
        }
    }

    private static void searchMood(Scanner scanner) {
        System.out.print("Enter date (YYYY-MM-DD) to search: ");
        String date = scanner.nextLine();
        Mood mood = moodService.searchMoodByDate(date);
        System.out.println(mood != null ? mood : "No mood found.");
    }

    private static void weeklySummary() {
        System.out.println(moodService.weeklySummary());
    }
}
