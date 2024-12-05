package com.taskapp.activator;

import com.taskapp.implementations.TaskManagementServiceImpl;
import com.taskapp.services.TaskManagementService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class TaskManagementActivator implements BundleActivator {
    private static final String DB_URL = "jdbc:sqlite:task_management.db";

    @Override
    public void start(BundleContext context) throws Exception {
        // TaskManagementService taskService = new TaskManagementServiceImpl();
        // context.registerService(TaskManagementService.class.getName(), taskService,
        // null);

        // System.out.println("TaskService has been registered.");

        System.out.println("TaskManagementApp started!");

        try {
            Class.forName("org.sqlite.JDBC"); // Explicitly load the SQLite driver
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC Driver: " + e.getMessage());
            return;
        }

        // Connect to SQLite database
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Connected to SQLite database!");
                createTable(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(BundleContext context) {
        System.out.println("Stopping Task Management App...");
    }

    private void createTable(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    description TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'pending',
                    deadline DATE
                );
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tasks table created or already exists.");
        }
    }
}
