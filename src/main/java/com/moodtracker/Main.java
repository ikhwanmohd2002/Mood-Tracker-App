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
       
    
        try {
            
    
            // Display the list of moods
            System.out.println("Choose a mood:");
            System.out.println("1 - Happy");
            System.out.println("2 - Sad");
            System.out.println("3 - Angry");
            System.out.println("4 - Fear");
            System.out.println("5 - Disgust");
            System.out.print("Enter the number corresponding to your mood: ");
            int moodChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
    
            // Map the choice to a mood string
            String mood;
            switch (moodChoice) {
                case 1 -> mood = "Happy";
                case 2 -> mood = "Sad";
                case 3 -> mood = "Angry";
                case 4 -> mood = "Fear";
                case 5 -> mood = "Disgust";
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    return; // Exit the method if the choice is invalid
                }
            }
    
            System.out.print("Enter notes (optional): ");
            String notes = scanner.nextLine();
    
            System.out.print("Enter date (DD-MM-YYYY): ");
            String dateInput = scanner.nextLine().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(dateInput, formatter); 
            moodService.addMood(new Mood(date, mood, notes));
            System.out.println("Mood added successfully!");
            viewMoods();
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
        // Display all moods
        viewMoods();
    
        System.out.print("Enter Mood ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        // Retrieve the current mood entry
        Mood currentMood = moodService.searchMoodById(id);
        if (currentMood == null) {
            System.out.println("Mood not found.");
            return;
        }
    
        // Display editing options
        System.out.println("What would you like to edit?");
        System.out.println("1 - Mood");
        System.out.println("2 - Description");
        System.out.println("3 - Date");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        String updatedMood = currentMood.getMood();
        String updatedNotes = currentMood.getNotes();
        LocalDate updatedDate = currentMood.getDate();
    
        switch (choice) {
            case 1 -> {
                System.out.println("Choose a new mood:");
                System.out.println("1 - Happy");
                System.out.println("2 - Sad");
                System.out.println("3 - Angry");
                System.out.println("4 - Fear");
                System.out.println("5 - Disgust");
                System.out.print("Enter the number corresponding to your mood: ");
                int moodChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                switch (moodChoice) {
                    case 1 -> updatedMood = "Happy";
                    case 2 -> updatedMood = "Sad";
                    case 3 -> updatedMood = "Angry";
                    case 4 -> updatedMood = "Fear";
                    case 5 -> updatedMood = "Disgust";
                    default -> {
                        System.out.println("Invalid mood choice. Exiting edit.");
                        return;
                    }
                }
            }
            case 2 -> {
                System.out.print("Enter new description: ");
                updatedNotes = scanner.nextLine();
            }
            case 3 -> {
                System.out.print("Enter new date (DD-MM-YYYY): ");
                String dateInput = scanner.nextLine().trim();
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    updatedDate = LocalDate.parse(dateInput, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Exiting edit.");
                    return;
                }
            }
            default -> {
                System.out.println("Invalid choice. Exiting edit.");
                return;
            }
        }
    
        if (moodService.editMoodById(id, updatedMood, updatedNotes, updatedDate)) {
            System.out.println("Mood updated successfully!");
            viewMoods();
        } else {
            System.out.println("Failed to update mood.");
        }
    }
    
    

    private static void deleteMood(Scanner scanner) {
        System.out.print("Enter Mood ID to delete: ");
        int id = scanner.nextInt();

        if (moodService.deleteMoodById(id)) {
            System.out.println("Mood deleted!");
            viewMoods();
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
