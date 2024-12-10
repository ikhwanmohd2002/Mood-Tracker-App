package com.moodtracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.moodtracker.implementations.MoodTrackerServiceImpl;
import com.moodtracker.models.Mood;
import com.moodtracker.services.MoodTrackerService;

public class Main implements BundleActivator {
    private static final MoodTrackerService moodService = new MoodTrackerServiceImpl();

    @Override
    public void start(BundleContext context) throws Exception {
        // Get the MoodTrackerService from the OSGi service registry
        // ServiceReference<MoodTrackerService> reference =
        // context.getServiceReference(MoodTrackerService.class);
        // MoodTrackerService moodService = context.getService(reference);

        if (moodService != null) {
            System.out.println("MoodTrackerService is available. Testing it...");

            // Example usage of the service
            Mood newMood = new Mood(LocalDate.now(), "Happy", "Feeling great!");
            moodService.addMood(newMood);

            System.out.println("All Moods:");
            moodService.viewMoods().forEach(System.out::println);
        } else {
            System.out.println("MoodTrackerService is not available.");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Stopping Main bundle.");
    }

    public static void main(String[] args) {
        com.moodtracker.database.Database.initialize();

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
            System.out.println("Choose a mood:");
            System.out.println("1 - Happy");
            System.out.println("2 - Sad");
            System.out.println("3 - Angry");
            System.out.println("4 - Fear");
            System.out.println("5 - Disgust");
            System.out.print("Enter the number corresponding to your mood (Type 'Q' to cancel): ");
            String moodInput = scanner.nextLine().trim();
            if (moodInput.equalsIgnoreCase("Q")) {
                System.out.println("Operation cancelled. Returning to main menu.");
                return;
            }

            int moodChoice;
            try {
                moodChoice = Integer.parseInt(moodInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return;
            }

            // Map the choice to a mood string
            String mood;
            switch (moodChoice) {
                case 1 -> mood = "Happy";
                case 2 -> mood = "Sad";
                case 3 -> mood = "Angry";
                case 4 -> mood = "Fear";
                case 5 -> mood = "Disgust";
                default -> {
                    System.out.println("Invalid choice. Returning to main menu.");
                    return;
                }
            }

            String notes = getInputOrCancel(scanner, "Enter notes (optional)");
            if (notes == null)
                return;

            String dateInput = getInputOrCancel(scanner, "Enter date (DD-MM-YYYY)");
            if (dateInput == null)
                return;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date;
            try {
                date = LocalDate.parse(dateInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use DD-MM-YYYY.");
                return;
            }

            moodService.addMood(new Mood(date, mood, notes));
            System.out.println("Mood added successfully!");
            viewMoods();
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
        viewMoods();

        String idInput = getInputOrCancel(scanner, "Enter Mood ID to edit");
        if (idInput == null)
            return;

        int id;
        try {
            id = Integer.parseInt(idInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Returning to main menu.");
            return;
        }

        Mood currentMood = moodService.searchMoodById(id);
        if (currentMood == null) {
            System.out.println("Mood not found. Returning to main menu.");
            return;
        }

        System.out.println("What would you like to edit?");
        System.out.println("1 - Mood");
        System.out.println("2 - Description");
        System.out.println("3 - Date");
        String choiceInput = getInputOrCancel(scanner, "Enter your choice");
        if (choiceInput == null)
            return;

        int choice;
        try {
            choice = Integer.parseInt(choiceInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Returning to main menu.");
            return;
        }

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
                String moodInput = getInputOrCancel(scanner, "Enter the number corresponding to your mood");
                if (moodInput == null)
                    return;

                int moodChoice;
                try {
                    moodChoice = Integer.parseInt(moodInput);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Returning to main menu.");
                    return;
                }

                switch (moodChoice) {
                    case 1 -> updatedMood = "Happy";
                    case 2 -> updatedMood = "Sad";
                    case 3 -> updatedMood = "Angry";
                    case 4 -> updatedMood = "Fear";
                    case 5 -> updatedMood = "Disgust";
                    default -> {
                        System.out.println("Invalid mood choice. Returning to main menu.");
                        return;
                    }
                }
            }
            case 2 -> {
                updatedNotes = getInputOrCancel(scanner, "Enter new description");
                if (updatedNotes == null)
                    return;
            }
            case 3 -> {
                String dateInput = getInputOrCancel(scanner, "Enter new date (DD-MM-YYYY)");
                if (dateInput == null)
                    return;

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    updatedDate = LocalDate.parse(dateInput, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Returning to main menu.");
                    return;
                }
            }
            default -> {
                System.out.println("Invalid choice. Returning to main menu.");
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
        String idInput = getInputOrCancel(scanner, "Enter Mood ID to delete");
        if (idInput == null)
            return;

        int id;
        try {
            id = Integer.parseInt(idInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Returning to main menu.");
            return;
        }

        if (moodService.deleteMoodById(id)) {
            System.out.println("Mood deleted!");
            viewMoods();
        } else {
            System.out.println("Mood not found. Returning to main menu.");
        }
    }

    private static void searchMood(Scanner scanner) {
        String dateInput = getInputOrCancel(scanner, "Enter date (DD-MM-YYYY) to search");
        if (dateInput == null)
            return;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(dateInput, formatter);
            Mood mood = moodService.searchMoodByDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // Pass
                                                                                                              // database-compatible
                                                                                                              // format

            if (mood != null) {
                System.out.println(mood);
            } else {
                System.out.println("No mood found for the specified date.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
        }
    }

    private static void weeklySummary() {
        System.out.println(moodService.weeklySummary());
    }

    private static String getInputOrCancel(Scanner scanner, String prompt) {
        System.out.print(prompt + " (Type 'Q' to cancel): ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("Q")) {
            System.out.println("Operation cancelled. Returning to main menu.");
            return null;
        }
        return input;
    }

}
