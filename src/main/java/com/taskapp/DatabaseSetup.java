package com.taskapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:task_management.db";

        // SQL for creating the "tasks" table
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    description TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'pending',
                    deadline DATE
                );
                """;

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // Create the tasks table
            stmt.execute(sql);
            System.out.println("Database and table created.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
