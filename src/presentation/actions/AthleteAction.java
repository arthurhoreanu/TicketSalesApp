package presentation.actions;

import controller.Controller;
import model.Athlete;

import java.util.List;
import java.util.Scanner;

public class AthleteAction {
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
        if (!newName.trim().isEmpty()) {
            athlete.setAthleteName(newName);
        }

        System.out.print("Enter new sport (or press Enter to keep current genre): ");
        String newSport = scanner.nextLine();
        if (!newSport.trim().isEmpty()) {
            athlete.setAthleteSport(newSport);
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
