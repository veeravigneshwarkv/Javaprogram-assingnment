package com.kec.academic;

import java.sql.SQLException;
import java.util.Scanner;

public class AssessmentWeightageMenu {

    private final AssessmentWeightageDAO dao =
            new AssessmentWeightageDAO();

    public void showMenu(Scanner scanner) {

        while (true) {

            System.out.println("\n===== ASSESSMENT WEIGHTAGE CONFIGURATION =====");
            System.out.println("1. Configure component weightage");
            System.out.println("2. View weightages");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {

                    case "1":
                        configureWeightage(scanner);
                        break;

                    case "2":
                        viewWeightages(scanner);
                        break;

                    case "0":
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (SQLException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void configureWeightage(Scanner scanner)
            throws SQLException {

        System.out.println("\nCourse types:");
        System.out.println("1. THEORY");
        System.out.println("2. PRACTICAL");
        System.out.println("3. EMBEDDED");

        System.out.print("Select course type: ");
        String choice = scanner.nextLine().trim();

        String courseType;

        switch (choice) {
            case "1":
                courseType = "THEORY";
                break;
            case "2":
                courseType = "PRACTICAL";
                break;
            case "3":
                courseType = "EMBEDDED";
                break;
            default:
                System.out.println("Invalid course type.");
                return;
        }

        System.out.println("\nComponents:");
        System.out.println("1. CIA1");
        System.out.println("2. CIA2");
        System.out.println("3. CIA3");
        System.out.println("4. ASSIGNMENT");
        System.out.println("5. QUIZ");
        System.out.println("6. LAB");

        System.out.print("Select component: ");
        String componentChoice = scanner.nextLine().trim();

        String component;

        switch (componentChoice) {
            case "1":
                component = "CIA1";
                break;
            case "2":
                component = "CIA2";
                break;
            case "3":
                component = "CIA3";
                break;
            case "4":
                component = "ASSIGNMENT";
                break;
            case "5":
                component = "QUIZ";
                break;
            case "6":
                component = "LAB";
                break;
            default:
                System.out.println("Invalid component.");
                return;
        }

        System.out.print(
                "Enter component weightage (% of internal assessment): "
        );

        String input = scanner.nextLine().trim();

        double weight;

        try {
            weight = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
            return;
        }

        dao.saveWeightage(courseType, component, weight);

        System.out.println("Weightage saved successfully.");

        dao.displayWeightages(courseType);
    }

    private void viewWeightages(Scanner scanner)
            throws SQLException {

        System.out.println("\n1. THEORY");
        System.out.println("2. PRACTICAL");
        System.out.println("3. EMBEDDED");

        System.out.print("Select course type: ");
        String choice = scanner.nextLine().trim();

        String courseType;

        switch (choice) {
            case "1":
                courseType = "THEORY";
                break;
            case "2":
                courseType = "PRACTICAL";
                break;
            case "3":
                courseType = "EMBEDDED";
                break;
            default:
                System.out.println("Invalid course type.");
                return;
        }

        dao.displayWeightages(courseType);
    }
}