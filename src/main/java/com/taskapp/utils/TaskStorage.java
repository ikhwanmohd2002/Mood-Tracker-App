package com.taskapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.taskapp.models.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {
    private static final String FILE_PATH = "tasks.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Save tasks to a JSON file
    public static void saveTasks(List<Task> tasks) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), tasks);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    // Load tasks from a JSON file
    public static List<Task> loadTasks() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>(); // Return an empty list if the file doesn't exist
            }

            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, Task.class);
            return objectMapper.readValue(file, listType);
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
