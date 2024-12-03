package com.taskapp.implementations;

import com.taskapp.models.Task;
import com.taskapp.services.TaskManagementService;
import com.taskapp.utils.TaskStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskManagementServiceImpl implements TaskManagementService {
    private final List<Task> tasks;
    private int nextId;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TaskManagementServiceImpl() {
        this.tasks = TaskStorage.loadTasks(); // Load tasks from local storage
        this.nextId = tasks.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1; // Determine the next ID
    }

    @Override
    public void addTask(String description) {
        Task newTask = new Task(nextId++, description, "pending", "no deadline");
        tasks.add(newTask);
        TaskStorage.saveTasks(tasks); // Save to local storage
        System.out.println("Task added successfully!");
    }

    @Override
    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        tasks.forEach(System.out::println);
    }

    @Override
    public void deleteTask(int taskId) {
        boolean removed = tasks.removeIf(task -> task.getId() == taskId);
        if (removed) {
            TaskStorage.saveTasks(tasks);
            System.out.println("Task deleted successfully!");
        } else {
            System.out.println("Task not found.");
        }
    }

    @Override
    public void updateTaskStatus(int taskId, String status) {
        tasks.stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .ifPresentOrElse(task -> {
                    task.setStatus(status);
                    TaskStorage.saveTasks(tasks); // Save to local storage
                    System.out.println("Task status updated to: " + status);
                }, () -> System.out.println("Task not found."));
    }

    @Override
    public void setTaskDeadline(int taskId, String deadline) {
        tasks.stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .ifPresentOrElse(task -> {
                    task.setDeadline(deadline);
                    TaskStorage.saveTasks(tasks); // Save to local storage
                    System.out.println("Deadline set to: " + deadline);
                }, () -> System.out.println("Task not found."));
    }

    @Override
    public void viewTasksByDeadline(String deadline) {
        LocalDate inputDeadlinee = LocalDate.parse(deadline, formatter);
        System.out.println(inputDeadlinee);
        try {
            // Parse the input deadline into a LocalDate object
            LocalDate inputDeadline = LocalDate.parse(deadline, formatter);

            // Filter tasks with deadlines before or on the input date
            List<Task> filteredTasks = tasks.stream()
                    .filter(task -> {
                        if (task.getDeadline().equals("no deadline")) {
                            return false; // Exclude tasks without deadlines
                        }
                        LocalDate taskDeadline = LocalDate.parse(task.getDeadline(), formatter);
                        return !taskDeadline.isAfter(inputDeadline); // Include tasks on or before the input date
                    })
                    .toList();

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
