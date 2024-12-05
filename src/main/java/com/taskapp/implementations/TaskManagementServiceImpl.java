package com.taskapp.implementations;

import com.taskapp.models.Task;
import com.taskapp.services.TaskManagementService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TaskManagementServiceImpl implements TaskManagementService {
    private static final String DB_URL = "jdbc:sqlite:task_management.db";
    private final List<Task> tasks = new ArrayList<>();
    // private int nextId = 1;
    // private final DateTimeFormatter formatter =
    // DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void addTask(String description, String deadline) {
        String sql = "INSERT INTO tasks(description, deadline) VALUES(?, ?)";
        // Task newTask = new Task(nextId++, description, "pending", "no deadline");
        // tasks.add(newTask);
        // System.out.println("Task added successfully!");
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, description);
            pstmt.setString(2, deadline);
            pstmt.executeUpdate();
            System.out.println("Task added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void viewTasks() {
        String sql = "SELECT * FROM tasks";
        DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            boolean hasTasks = false;
            while (rs.next()) {
                hasTasks = true;

                // Fetch deadline and reformat it
                String dbDeadline = rs.getString("deadline");
                String formattedDeadline = "No deadline"; // Default message if no deadline is available

                if (dbDeadline != null && !dbDeadline.isEmpty()) {
                    LocalDate deadline = LocalDate.parse(dbDeadline, dbFormatter);
                    formattedDeadline = deadline.format(displayFormatter);
                }

                System.out.println("ID: " + rs.getInt("id") +
                        ", Description: " + rs.getString("description") +
                        ", Status: " + rs.getString("status") +
                        ", Deadline: " + formattedDeadline);
            }

            if (!hasTasks) {
                System.out.println("No tasks available.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        // if (tasks.isEmpty()) {
        // System.out.println("No tasks available.");
        // return;
        // }
        // tasks.forEach(System.out::println);
    }

    @Override
    public void deleteTask(int taskId) {
        // boolean removed = tasks.removeIf(task -> task.getId() == taskId);
        // if (removed) {
        // System.out.println("Task deleted successfully!");
        // } else {
        // System.out.println("Task not found.");
        // }
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
            System.out.println("Task deleted successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTaskStatus(int taskId, String status) {
        // Validate the status input
        // if (!status.equalsIgnoreCase("pending") &&
        // !status.equalsIgnoreCase("completed")) {
        // System.out.println("Invalid status. Please enter 'pending' or 'completed'.");
        // return; // Exit the method if the status is invalid
        // }

        // // Find and update the task if valid
        // tasks.stream()
        // .filter(task -> task.getId() == taskId)
        // .findFirst()
        // .ifPresentOrElse(task -> {
        // task.setStatus(status.toLowerCase()); // Normalize to lowercase
        // System.out.println("Task status updated to: " + status.toLowerCase());
        // }, () -> System.out.println("Task not found."));
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
            System.out.println("Task status updated successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setTaskDeadline(int taskId, String deadline) {
        // // Define the date format
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // // Validate the deadline input
        // try {
        // LocalDate parsedDate = LocalDate.parse(deadline, formatter);

        // // Proceed to find and update the task if the date is valid
        // tasks.stream()
        // .filter(task -> task.getId() == taskId)
        // .findFirst()
        // .ifPresentOrElse(task -> {
        // task.setDeadline(parsedDate.toString());
        // System.out.println("Deadline set to: " + parsedDate.format(formatter));
        // }, () -> System.out.println("Task not found."));
        // } catch (DateTimeParseException e) {
        // System.out.println("Invalid date format. Please use dd-MM-yyyy.");
        // } // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Validate the deadline input
        try {
            LocalDate parsedDate = LocalDate.parse(deadline, formatter);

            // Proceed to update the deadline in the database
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:task_management.db")) {
                String sql = "UPDATE tasks SET deadline = ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, parsedDate.toString()); // Set the formatted date
                    pstmt.setInt(2, taskId); // Set the task ID

                    int rowsUpdated = pstmt.executeUpdate(); // Execute the update query

                    if (rowsUpdated > 0) {
                        System.out.println("Deadline set to: " + parsedDate.format(formatter));
                    } else {
                        System.out.println("Task not found.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy.");
        }
    }

    @Override
    public void viewTasksByDeadline(String deadline) {
        // LocalDate inputDeadlinee = LocalDate.parse(deadline, formatter);
        // System.out.println(inputDeadlinee);
        // try {
        // // Parse the input deadline into a LocalDate object
        // LocalDate inputDeadline = LocalDate.parse(deadline, formatter);

        // // Filter tasks with deadlines before or on the input date
        // List<Task> filteredTasks = tasks.stream()
        // .filter(task -> {
        // if (task.getDeadline().equals("no deadline")) {
        // return false; // Exclude tasks without deadlines
        // }
        // LocalDate taskDeadline = LocalDate.parse(task.getDeadline(), formatter);
        // return !taskDeadline.isAfter(inputDeadline); // Include tasks on or before
        // the input date
        // })
        // .toList();

        // // Check if any tasks match the criteria
        // if (filteredTasks.isEmpty()) {
        // System.out.println("No tasks found with deadlines on or before: " +
        // deadline);
        // } else {
        // System.out.println("Tasks with deadlines on or before " + deadline + ":");
        // filteredTasks.forEach(System.out::println);
        // }
        // } catch (Exception e) {
        // System.out.println("Invalid deadline format. Please use DD-MM-YYYY.");
        // }
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // Parse the input deadline into a LocalDate object
            LocalDate inputDeadline = LocalDate.parse(deadline, formatter);
            String formattedDeadline = inputDeadline.format(dbFormatter);

            // Prepare a list to hold the filtered tasks
            List<Task> filteredTasks = new ArrayList<>();

            // Connect to the database
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:task_management.db")) {
                String sql = "SELECT id, description, status, deadline FROM tasks WHERE deadline IS NOT NULL AND deadline <= ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, formattedDeadline); // Use the formatted LocalDate

                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String description = rs.getString("description");
                            String status = rs.getString("status");
                            String dbDeadline = rs.getString("deadline");

                            filteredTasks.add(new Task(id, description, status, dbDeadline));
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                return;
            }

            // Check if any tasks match the criteria
            if (filteredTasks.isEmpty()) {
                System.out.println("No tasks found with deadlines on or before: " + deadline);
            } else {
                System.out.println("Tasks with deadlines on or before " + deadline + ":");
                filteredTasks.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Invalid deadline format. Please use DD-MM-YYYY.");
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks); // Return a copy to prevent external modification
    }
}
