package main.java.com.ticketsalesapp.view.admin;

import main.java.com.ticketsalesapp.controller.ApplicationController;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.view.AccountAction;
import java.util.Scanner;

/**
 * Represents the menu for admin users, providing various management options such as user account deletion,
 * and management of events, tickets, venues, artists, and athletes.
 */
public class AdminMenu {

    /**
     * Displays the admin menu and processes the selected option.
     * @param scanner    the scanner to read user input
     * @param applicationController the controller to handle administrative actions
     * @return a boolean indicating if the application should continue running (false if exit is chosen)
     */
    public static boolean display(Scanner scanner, ApplicationController applicationController) {
        try {
            System.out.println("==== Admin Menu ====");
            System.out.println("1. Logout");
            System.out.println("2. Delete User Account");
            System.out.println("3. Manage Events");
            System.out.println("4. Manage Tickets");
            System.out.println("5. Manage Venues");
            System.out.println("6. Manage Artists");
            System.out.println("7. Manage Athletes");
            System.out.println("0. Exit");
            System.out.println("====================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    applicationController.logout();
                    System.out.println("Logged out successfully.");
                    break;
                case "2":
                    AccountAction.handleDeleteUserAccount(scanner, applicationController);
                    break;
                case "3":
                    AdminEventMenu.display(scanner, applicationController);
                    break;
                case "4":
                    AdminTicketMenu.display(scanner, applicationController);
                    break;
                case "5":
                    AdminVenueMenu.display(scanner, applicationController);
                    break;
                case "6":
                    AdminArtistMenu.display(scanner, applicationController);
                    break;
                case "7":
                    AdminAthleteMenu.display(scanner, applicationController);
                    break;
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    return false;
                default:
                    throw new ValidationException("Invalid option. Please select a number between 0 and 7.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}