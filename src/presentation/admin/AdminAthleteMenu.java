package presentation.admin;

import controller.Controller;
import model.Athlete;

import java.util.List;
import java.util.Scanner;

public class AdminAthleteMenu {
    public static void display(Scanner scanner, Controller controller) {
        boolean inAthleteMenu = true;
        while (inAthleteMenu) {
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
                    handleCreateAthlete(scanner, controller);
                    break;
                case "2":
                    handleViewAthletes(controller);
                    break;
                case "3":
                    handleUpdateAthlete(scanner, controller);
                    break;
                case "4":
                    handleDeleteAthlete(scanner, controller);
                    break;
                case "0":
                    inAthleteMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }

    public static void handleCreateAthlete(Scanner scanner, Controller controller) {
        System.out.println("=== Create Athlete ===");
        System.out.print("Enter athlete name: ");
        String athleteName = scanner.nextLine();
        System.out.print("Enter athlete sport: ");
        String athleteSport = scanner.nextLine();
        controller.createAthlete(athleteName, athleteSport);
    }

    public static void handleViewAthletes(Controller controller) {
        System.out.println("=== View Athletes ===");
        List<Athlete> athletes = controller.getAllAthletes();

        if (athletes.isEmpty()) {
            System.out.println("No athletes found.");
        } else {
            for (Athlete athlete : athletes) {
                System.out.println(athlete);
            }
        }
    }

    public static void handleUpdateAthlete(Scanner scanner, Controller controller) {
        System.out.println("=== Update Athlete ===");

        List<Athlete> athletes = controller.getAllAthletes();
        if (athletes.isEmpty()) {
            System.out.println("No athletes available.");
        } else {
            for (Athlete athlete : athletes) {
                System.out.println(athlete);
            }
        }

        System.out.print("Enter athlete ID to update: ");
        int athleteID = Integer.parseInt(scanner.nextLine());

        Athlete athlete = controller.findAthleteByID(athleteID);
        if (athlete == null) {
            System.out.println("Athlete not found.");
            return;
        }

        System.out.print("Enter new athlete name (or press Enter to keep current name): ");
        String newName = scanner.nextLine();
        if (newName.isEmpty()) {
            newName = athlete.getAthleteName(); // Keep current name if input is empty
        }

        System.out.print("Enter new sport (or press Enter to keep current genre): ");
        String newSport = scanner.nextLine();
        if (newSport.isEmpty()) {
            newSport = athlete.getAthleteSport(); // Keep current sport if input is empty
        }

        controller.updateAthlete(athleteID, newName, newSport);
    }

    public static void handleDeleteAthlete(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Athlete ===");

        List<Athlete> athletes = controller.getAllAthletes();
        if (athletes.isEmpty()) {
            System.out.println("No athletes available.");
        } else {
            for (Athlete athlete : athletes) {
                System.out.println(athlete);
            }
        }

        System.out.print("Enter athlete ID to delete: ");
        int artistID = Integer.parseInt(scanner.nextLine());

        controller.deleteAthlete(artistID);
    }
}
