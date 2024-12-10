package com.moodtracker.cli;

import java.util.Scanner;

public class ScannerTest {
    public static void main(String[] args) {
        System.out.println("Starting basic Scanner test");
        
        // Create scanner without any fancy configuration
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Simple echo test
            System.out.println("Type something and press Enter:");
            String input = scanner.nextLine();
            System.out.println("You typed: " + input);
            
            // Test if we can read another line
            System.out.println("\nType something else:");
            input = scanner.nextLine();
            System.out.println("You typed: " + input);
            
        } catch (Exception e) {
            System.out.println("Error occurred:");
            System.out.println("Error type: " + e.getClass().getName());
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            System.out.println("\nTest completed");
        }
    }
}
