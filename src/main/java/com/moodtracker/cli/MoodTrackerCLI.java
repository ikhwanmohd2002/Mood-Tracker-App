package com.moodtracker.cli;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Scanner;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Component
public class MoodTrackerCLI implements CommandLineRunner {
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Scanner scanner = new Scanner(System.in);
    private final ConfigurableApplicationContext context;

    public MoodTrackerCLI(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) {
        while (true) {
            try {
                // Get and display the menu
                String menu = getMenu();
                System.out.println(menu);

                String choice = scanner.nextLine().trim();
                
                if (choice.equalsIgnoreCase("7") || choice.equalsIgnoreCase("Q")) {
                    System.out.println("Goodbye!");
                    scanner.close();
                    context.close();
                    break;
                }

                handleMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private String getMenu() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private void handleMenuChoice(String choice) throws Exception {
        switch (choice) {
            case "1":
                addMood();
                break;
            case "2":
                viewMoods();
                break;
            case "3":
                editMood();
                break;
            case "4":
                deleteMood();
                break;
            case "5":
                searchMoodByDate();
                break;
            case "6":
                getWeeklySummary();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void addMood() throws Exception {
        System.out.println("\nEnter your mood (1-5):");
        String moodChoice = scanner.nextLine().trim();
        
        if (moodChoice.equalsIgnoreCase("Q")) {
            return;
        }

        String moodType = switch (moodChoice) {
            case "1" -> "HAPPY";
            case "2" -> "SAD";
            case "3" -> "ANGRY";
            case "4" -> "FEAR";
            case "5" -> "DISGUST";
            default -> throw new IllegalArgumentException("Invalid mood choice");
        };

        System.out.println("Enter a note (optional, press Enter to skip):");
        String note = scanner.nextLine().trim();

        String json = String.format("""
            {
                "type": "%s",
                "date": "%s",
                "note": "%s"
            }""", moodType, LocalDate.now(), note);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/moods"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            System.out.println("Mood added successfully!");
        } else {
            System.out.println("Error adding mood: " + response.body());
        }
    }

    private void viewMoods() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/moods"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("\nYour moods:\n" + response.body());
    }

    private void editMood() throws Exception {
        System.out.println("Enter mood ID to edit:");
        String id = scanner.nextLine().trim();
        
        if (id.equalsIgnoreCase("Q")) {
            return;
        }

        System.out.println("\nEnter new mood (1-5):");
        String moodChoice = scanner.nextLine().trim();
        
        String moodType = switch (moodChoice) {
            case "1" -> "HAPPY";
            case "2" -> "SAD";
            case "3" -> "ANGRY";
            case "4" -> "FEAR";
            case "5" -> "DISGUST";
            default -> throw new IllegalArgumentException("Invalid mood choice");
        };

        System.out.println("Enter a new note (optional, press Enter to skip):");
        String note = scanner.nextLine().trim();

        String json = String.format("""
            {
                "type": "%s",
                "date": "%s",
                "note": "%s"
            }""", moodType, LocalDate.now(), note);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/moods/" + id))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            System.out.println("Mood updated successfully!");
        } else {
            System.out.println("Error updating mood: " + response.body());
        }
    }

    private void deleteMood() throws Exception {
        System.out.println("Enter mood ID to delete:");
        String id = scanner.nextLine().trim();
        
        if (id.equalsIgnoreCase("Q")) {
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/moods/" + id))
            .DELETE()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 204) {
            System.out.println("Mood deleted successfully!");
        } else {
            System.out.println("Error deleting mood: " + response.body());
        }
    }

    private void searchMoodByDate() throws Exception {
        System.out.println("Enter date (YYYY-MM-DD):");
        String date = scanner.nextLine().trim();
        
        if (date.equalsIgnoreCase("Q")) {
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/moods/date/" + date))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("\nMood for " + date + ":\n" + response.body());
    }

    private void getWeeklySummary() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/moods/weekly/summary"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("\nWeekly Summary:\n" + response.body());
    }
}
