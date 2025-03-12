package com.ticketsalesapp.view.admin;

import com.ticketsalesapp.controller.AthleteController;
import com.ticketsalesapp.exception.EntityNotFoundException;
import com.ticketsalesapp.exception.ValidationException;
import com.ticketsalesapp.model.event.Athlete;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Provides a menu for admins to manage athletes, including creating, viewing, updating, and deleting athletes.
 */
@Component
public class AdminAthleteMenu {

    private final AthleteController athleteController;

    public AdminAthleteMenu(AthleteController athleteController) {
        this.athleteController = athleteController;
    }

    /**
     * Displays the athlete management menu and processes the selected options.
     * @param scanner the scanner to read user input
     */
    public void display(Scanner scanner) {
        boolean inAthleteMenu = true;
        while (inAthleteMenu) {
            try {
                System.out.println("==== Athlete Management ====");
                System.out.println("1. Create Athlete");
                System.out.println("2. View Athletes");
                System.out.println("3. Update Athlete");
                System.out.println("4. Delete Athlete");
                System.out.println("0. Back to Admin Menu");
                System.out.println("==========================");

                System.out.print("Choose an option: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        handleCreateAthlete(scanner);
                        break;
                    case "2":
                        handleViewAthletes();
                        break;
                    case "3":
                        handleUpdateAthlete(scanner);
                        break;
                    case "4":
                        handleDeleteAthlete(scanner);
                        break;
                    case "0":
                        inAthleteMenu = false;
                        break;
                    default:
                        throw new ValidationException("Invalid option. Please select a number between 0 and 4.");
                }
                System.out.println();
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handles the creation of a new athlete.
     * @param scanner the scanner to read user input
     */
    public void handleCreateAthlete(Scanner scanner) {
        try {
            System.out.println("=== Create Athlete ===");
            System.out.print("Enter athlete name: ");
            String athleteName = scanner.nextLine();
            System.out.print("Enter athlete sport: ");
            String athleteSport = scanner.nextLine();
            athleteController.createAthlete(athleteName, athleteSport);
            System.out.println("Athlete created successfully.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays a list of all athletes.
     */
    public void handleViewAthletes() {
        System.out.println("=== View Athletes ===");
        athleteController.displayAllAthletes();
    }

    /**
     * Handles updating an existing athlete with new information.
     * @param scanner the scanner to read user input
     */
    public void handleUpdateAthlete(Scanner scanner) {
        try {
            System.out.println("=== Update Athlete ===");
            athleteController.displayAllAthletes();
            System.out.print("Enter athlete ID to update: ");
            int athleteId = Integer.parseInt(scanner.nextLine());
            Athlete athlete = athleteController.findAthleteById(athleteId);
            System.out.print("Enter new athlete name (or press Enter to keep current name): ");
            String newName = scanner.nextLine();
            if (newName.isEmpty()) {
                newName = athlete.getAthleteName();
            }
            System.out.print("Enter new sport (or press Enter to keep current genre): ");
            String newSport = scanner.nextLine();
            if (newSport.isEmpty()) {
                newSport = athlete.getAthleteSport();
            }
            athleteController.updateAthlete(athleteId, newName, newSport);
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles deleting an athlete by their ID.
     * @param scanner the scanner to read user input
     */
    public void handleDeleteAthlete(Scanner scanner) {
        System.out.println("=== Delete Athlete ===");
        athleteController.displayAllAthletes();
        System.out.print("Enter athlete ID to delete: ");
        int athleteID = Integer.parseInt(scanner.nextLine());
        athleteController.deleteAthlete(athleteID);
    }
}