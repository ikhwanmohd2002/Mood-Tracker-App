package com.moodtracker.cli;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MoodTrackerCLI implements CommandLineRunner {

    private static final String BASE_URL = "http://localhost:8020";
    private static final HttpClient client = HttpClient.newHttpClient();
    private final Scanner scanner;
    private final ConfigurableApplicationContext context;

    public MoodTrackerCLI(ConfigurableApplicationContext context) {
        this.context = context;
        this.scanner = new Scanner(System.in);
        System.out.println("DEBUG: Scanner initialized");
    }

    @Override
    public void run(String... args) {
        System.out.println("DEBUG: Starting CLI application");
        boolean running = true;

        while (running) {
            try {
                displayMenu();
                System.out.print("Enter your choice (1-7 or Q to quit): ");

                String choice = scanner.nextLine().trim();
                System.out.println("DEBUG: User choice received: '" + choice + "'");

                if (choice.equalsIgnoreCase("Q") || choice.equals("7")) {
                    System.out.println("DEBUG: Exit command received");
                    running = false;
                    continue;
                }

                if (!choice.isEmpty()) {
                    System.out.println("DEBUG: Processing menu choice: " + choice);
                    handleMenuChoice(choice);

                    System.out.println("\nPress Enter to continue or Q to quit...");
                    String continueChoice = scanner.nextLine().trim();
                    System.out.println("DEBUG: Continue choice received: '" + continueChoice + "'");

                    if (continueChoice.equalsIgnoreCase("Q")) {
                        System.out.println("DEBUG: Quit command received after menu action");
                        running = false;
                    }
                }

            } catch (Exception e) {
                System.out.println("DEBUG: Exception in main loop: " + e.getClass().getName());
                System.out.println("DEBUG: Exception message: " + e.getMessage());
                e.printStackTrace();

                handleError(e);
                System.out.println("\nPress Enter to continue or Q to quit...");
                String input = scanner.nextLine().trim();
                System.out.println("DEBUG: Error recovery input received: '" + input + "'");

                if (input.equalsIgnoreCase("Q")) {
                    System.out.println("DEBUG: Quit command received after error");
                    running = false;
                }
            }
        }

        cleanup();
    }

    private void displayMenu() {
        System.out.println("\n=== Mood Tracker Menu ===");
        System.out.println("1. Add Mood");
        System.out.println("2. View Moods");
        System.out.println("3. Edit Mood");
        System.out.println("4. Delete Mood");
        System.out.println("5. Search Mood by Date");
        System.out.println("6. Weekly Summary");
        System.out.println("7. Exit");
    }

    private void handleError(Exception e) {
        System.out.println("DEBUG: Handling error: " + e.getClass().getName());
        if (e.getCause() instanceof ConnectException) {
            System.out.println("Error: Cannot connect to the server at " + BASE_URL);
            System.out.println("Please ensure the server is running and try again.");
        } else {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cleanup() {
        System.out.println("DEBUG: Starting cleanup");
        System.out.println("Goodbye!");
        try {
            System.out.println("DEBUG: Closing scanner");
            scanner.close();
            System.out.println("DEBUG: Closing Spring context");
            context.close();
        } catch (Exception e) {
            System.out.println("DEBUG: Error during cleanup: " + e.getMessage());
        }
        System.out.println("DEBUG: Cleanup completed");
    }

    private void handleMenuChoice(String choice) throws Exception {
        System.out.println("DEBUG: Handling menu choice: " + choice);
        switch (choice) {
            case "1" ->
                addMood();
            case "2" ->
                viewMoods();
            case "3" ->
                editMood();
            case "4" ->
                deleteMood();
            case "5" ->
                searchMoodByDate();
            case "6" ->
                getWeeklySummary();
            default ->
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void addMood() throws Exception {
        System.out.println("DEBUG: Starting addMood()");
        while (true) {
            System.out.println("\nEnter your mood (1-5):");
            System.out.println("1. HAPPY");
            System.out.println("2. SAD");
            System.out.println("3. ANGRY");
            System.out.println("4. FEAR");
            System.out.println("5. DISGUST");
            System.out.println("(Q to cancel)");

            String moodChoice = scanner.nextLine().trim();
            System.out.println("DEBUG: Mood choice received: '" + moodChoice + "'");

            if (moodChoice.equalsIgnoreCase("Q")) {
                System.out.println("DEBUG: Cancelling mood addition");
                return;
            }

            if (!moodChoice.matches("[1-5]")) {
                System.out.println("Invalid mood. Please enter a number between 1 and 5.");
                continue;
            }

            String moodValue = switch (moodChoice) {
                case "1" ->
                    "HAPPY";
                case "2" ->
                    "SAD";
                case "3" ->
                    "ANGRY";
                case "4" ->
                    "FEAR";
                case "5" ->
                    "DISGUST";
                default ->
                    throw new IllegalArgumentException("Invalid mood choice");
            };

            System.out.println("Enter a note (optional, press Enter to skip):");
            String note = scanner.nextLine().trim();
            System.out.println("DEBUG: Note received: '" + note + "'");

            String json = String.format("""
                {
                    "mood": "%s",
                    "date": "%s",
                    "notes": "%s"
                }""", moodValue, LocalDate.now(), note);

            System.out.println("DEBUG: Sending JSON payload: " + json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/moods"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DEBUG: Response status: " + response.statusCode());
            System.out.println("DEBUG: Response body: " + response.body());

            if (response.statusCode() == 201) {
                System.out.println("Mood added successfully!");
                break;
            } else {
                System.out.println("Error adding mood: " + response.body());
                break;
            }
        }
    }

    private void viewMoods() throws Exception {
        System.out.println("DEBUG: Starting viewMoods()");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/moods"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("\nYour moods:\n" + response.body());
    }

    private void editMood() throws Exception {
        System.out.println("DEBUG: Starting editMood()");
        System.out.println("Enter mood ID to edit (Q to cancel):");
        String id = scanner.nextLine().trim();
        System.out.println("DEBUG: Mood ID received: " + id);

        if (id.equalsIgnoreCase("Q")) {
            System.out.println("DEBUG: Cancelling mood edit");
            return;
        }

        System.out.println("\nEnter new mood (1-5):");
        System.out.println("1. HAPPY");
        System.out.println("2. SAD");
        System.out.println("3. ANGRY");
        System.out.println("4. FEAR");
        System.out.println("5. DISGUST");
        String moodChoice = scanner.nextLine().trim();
        System.out.println("DEBUG: New mood choice received: " + moodChoice);

        if (!moodChoice.matches("[1-5]")) {
            System.out.println("Invalid mood choice. Operation cancelled.");
            return;
        }

        String moodValue = switch (moodChoice) {
            case "1" ->
                "HAPPY";
            case "2" ->
                "SAD";
            case "3" ->
                "ANGRY";
            case "4" ->
                "FEAR";
            case "5" ->
                "DISGUST";
            default ->
                throw new IllegalArgumentException("Invalid mood choice");
        };

        System.out.println("Enter a new note (optional, press Enter to skip):");
        String note = scanner.nextLine().trim();
        System.out.println("DEBUG: New note received: " + note);

        String json = String.format("""
            {
                "mood": "%s",
                "date": "%s",
                "notes": "%s"
            }""", moodValue, LocalDate.now(), note);

        System.out.println("DEBUG: Sending JSON payload: " + json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/moods/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("DEBUG: Response status: " + response.statusCode());
        System.out.println("DEBUG: Response body: " + response.body());

        if (response.statusCode() == 200) {
            System.out.println("Mood updated successfully!");
        } else {
            System.out.println("Error updating mood: " + response.body());
        }
    }

    private void deleteMood() throws Exception {
        System.out.println("DEBUG: Starting deleteMood()");
        System.out.println("Enter mood ID to delete (Q to cancel):");
        String id = scanner.nextLine().trim();
        System.out.println("DEBUG: Mood ID received: " + id);

        if (id.equalsIgnoreCase("Q")) {
            System.out.println("DEBUG: Cancelling mood deletion");
            return;
        }

        System.out.println("DEBUG: Sending request to delete mood");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/moods/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("DEBUG: Response status: " + response.statusCode());

        if (response.statusCode() == 204) {
            System.out.println("Mood deleted successfully!");
        } else {
            System.out.println("Error deleting mood: " + response.body());
        }
    }

    private void searchMoodByDate() throws Exception {
        System.out.println("DEBUG: Starting searchMoodByDate()");

        while (true) {
            System.out.println("Enter the date to search for moods (format: YYYY-MM-DD, Q to cancel):");
            String dateInput = scanner.nextLine().trim();
            System.out.println("DEBUG: Date input received: " + dateInput);

            // Check for cancellation
            if (dateInput.equalsIgnoreCase("Q")) {
                System.out.println("DEBUG: Cancelling searchMoodByDate");
                return;
            }

            try {
                // Validate and parse the date input
                LocalDate date = LocalDate.parse(dateInput);
                System.out.println("DEBUG: Valid date parsed: " + date);

                System.out.println("DEBUG: Sending request to search mood by date");
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/api/moods/date/" + date))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("DEBUG: Response status: " + response.statusCode());
                System.out.println("DEBUG: Response body: " + response.body());

                if (response.statusCode() == 200) {
                    System.out.println("\nMoods for " + date + ":\n" + response.body());
                    break; // Exit loop after successful search
                } else if (response.statusCode() == 404) {
                    System.out.println("No moods found for the given date.");
                    break; // Exit loop since the request was valid but no data was found
                } else {
                    System.out.println("Error searching moods: " + response.body());
                    break; // Exit loop on unexpected errors
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format or value. Please ensure the format is YYYY-MM-DD and the date is valid.\n");
                System.out.println("DEBUG: Invalid date input - " + e.getMessage());
            }
        }
    }

    private void getWeeklySummary() throws Exception {
        System.out.println("DEBUG: Starting getWeeklySummary()");
        System.out.println("DEBUG: Sending request to fetch weekly summary");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/moods/weekly/summary"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("DEBUG: Response status: " + response.statusCode());
        System.out.println("DEBUG: Response body: \n" + response.body());

        if (response.statusCode() == 200) {
            System.out.println(response.body());
        } else {
            System.out.println("Error fetching weekly summary: " + response.body());
        }
    }

}
