package com.taskapp;

import com.taskapp.implementations.TaskManagementServiceImpl;
import com.taskapp.services.TaskManagementService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManagementService taskService = new TaskManagementServiceImpl();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nTask Management App");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Delete Task");
            System.out.println("4. Update Task Status");
            System.out.println("5. Set Task Deadline");
            System.out.println("6. View Task by Deadline");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter task deadline: ");
                    String deadline = scanner.nextLine();
                    taskService.addTask(description, deadline);
                }
                case 2 -> taskService.viewTasks();
                case 3 -> {
                    System.out.print("Enter task ID to delete: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    taskService.deleteTask(id);
                }
                case 4 -> {
                    System.out.print("Enter task ID to update status: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new status (pending, completed): ");
                    String status = scanner.nextLine();
                    taskService.updateTaskStatus(id, status);
                }
                case 5 -> {
                    System.out.print("Enter task ID to set deadline: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter deadline (DD-MM-YYYY): ");
                    String deadline = scanner.nextLine();
                    taskService.setTaskDeadline(id, deadline);
                }
                case 6 -> {
                    System.out.print("Enter deadline (DD-MM-YYYY) to filter tasks: ");
                    String deadline = scanner.nextLine();
                    taskService.viewTasksByDeadline(deadline);
                }
                case 7 -> System.out.println("Exiting the app. Goodbye!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);

        scanner.close();
    }
}
