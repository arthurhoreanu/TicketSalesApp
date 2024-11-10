package presentation.menus.admin;

import controller.Controller;
import presentation.actions.AthleteAction;

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
                    AthleteAction.handleCreateAthlete(scanner, controller);
                    break;
                case "2":
                    AthleteAction.handleViewAthletes(controller);
                    break;
                case "3":
                    AthleteAction.handleUpdateAthlete(scanner, controller);
                    break;
                case "4":
                    AthleteAction.handleDeleteAthlete(scanner, controller);
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
}
