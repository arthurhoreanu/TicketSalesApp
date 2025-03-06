package main.java.com.ticketsalesapp.view.admin;

import main.java.com.ticketsalesapp.controller.ApplicationController;
import main.java.com.ticketsalesapp.exception.EntityNotFoundException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.Athlete;
import java.util.Scanner;

/**
 * Provides a menu for admins to manage athletes, including creating, viewing, updating, and deleting athletes.
 */
public class AdminAthleteMenu {

    /**
     * Displays the athlete management menu and processes the selected options.
     * @param scanner the scanner to read user input
     * @param applicationController the controller to handle athlete management actions
     */
    public static void display(Scanner scanner, ApplicationController applicationController) {
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
                        handleCreateAthlete(scanner, applicationController);
                        break;
                    case "2":
                        handleViewAthletes(applicationController);
                        break;
                    case "3":
                        handleUpdateAthlete(scanner, applicationController);
                        break;
                    case "4":
                        handleDeleteAthlete(scanner, applicationController);
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
     * @param applicationController the controller to manage athlete creation
     */
    public static void handleCreateAthlete(Scanner scanner, ApplicationController applicationController) {
        try {
            System.out.println("=== Create Athlete ===");
            System.out.print("Enter athlete name: ");
            String athleteName = scanner.nextLine();
            if (athleteName.isEmpty()) {
                throw new ValidationException("Athlete name cannot be empty.");
            }
            System.out.print("Enter athlete sport: ");
            String athleteSport = scanner.nextLine();
            if (athleteSport.isEmpty()) {
                throw new ValidationException("Athlete sport cannot be empty.");
            }
            applicationController.createAthlete(athleteName, athleteSport);
            System.out.println("Athlete created successfully.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays a list of all athletes.
     * @param applicationController the controller to retrieve athletes from
     */
    public static void handleViewAthletes(ApplicationController applicationController) {
        System.out.println("=== View Athletes ===");
        applicationController.getAllAthletes();
    }

    /**
     * Handles updating an existing athlete with new information.
     * @param scanner the scanner to read user input
     * @param applicationController the controller to manage athlete updates
     */
    public static void handleUpdateAthlete(Scanner scanner, ApplicationController applicationController) {
        try {
            System.out.println("=== Update Athlete ===");
            applicationController.getAllAthletes();
            System.out.print("Enter athlete ID to update: ");
            int athleteID = Integer.parseInt(scanner.nextLine());

            Athlete athlete = applicationController.findAthleteByID(athleteID);
            if (athlete == null) {
                throw new EntityNotFoundException("Athlete with ID " + athleteID + " not found.");
            }
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
            applicationController.updateAthlete(athleteID, newName, newSport);
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles deleting an athlete by their ID.
     * @param scanner the scanner to read user input
     * @param applicationController the controller to manage athlete deletion
     */
    public static void handleDeleteAthlete(Scanner scanner, ApplicationController applicationController) {
        System.out.println("=== Delete Athlete ===");
        applicationController.getAllAthletes();
        System.out.print("Enter athlete ID to delete: ");
        int athleteID = Integer.parseInt(scanner.nextLine());
        applicationController.deleteAthlete(athleteID);
    }
}