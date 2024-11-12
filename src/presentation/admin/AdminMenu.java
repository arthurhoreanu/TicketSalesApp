package presentation.admin;

import controller.Controller;
import presentation.AccountAction;

import java.util.Scanner;

public class AdminMenu {
    public static boolean display(Scanner scanner, Controller controller) {
        System.out.println("==== Admin Menu ====");
        System.out.println("1. Create Account");
        System.out.println("2. Logout");
        System.out.println("3. Delete User Account");
        System.out.println("4. Manage Events");
        System.out.println("5. Manage Tickets");
        System.out.println("6. Manage Venues");
        System.out.println("7. Manage Artists");
        System.out.println("8. Manage Athletes");
        System.out.println("0. Exit");
        System.out.println("====================");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                AccountAction.handleCreateAccount(scanner, controller);
                break;
            case "2":
                controller.logout();
                System.out.println("Logged out successfully.");
                break;
            case "3":
                AccountAction.handleDeleteUserAccount(scanner, controller);
                break;
            case "4":
                AdminEventMenu.display(scanner, controller);
                break;
            case "5":
                AdminTicketMenu.display(scanner, controller);
                break;
            case "6":
                AdminVenueMenu.display(scanner, controller);
                break;
            case "7":
                AdminArtistMenu.display(scanner, controller);
                break;
            case "8":
                AdminAthleteMenu.display(scanner, controller);
                break;
            case "0":
                System.out.println("Exiting the application. Goodbye!");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }
}
