package com.taskapp.services;

import com.taskapp.models.Task;

import java.util.List;

public interface TaskManagementService {
    void addTask(String description, String deadline);

    void viewTasks();

    void deleteTask(int taskId);

    void updateTaskStatus(int taskId, String status);

    void setTaskDeadline(int taskId, String deadline);

    void viewTasksByDeadline(String deadline);

    List<Task> getTasks(); // Helper method for filtering tasks
}
