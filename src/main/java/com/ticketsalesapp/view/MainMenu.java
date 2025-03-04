package main.java.com.ticketsalesapp.view;

import main.java.com.ticketsalesapp.controller.Controller;
import main.java.com.ticketsalesapp.exception.ValidationException;
import java.util.Scanner;

/**
 * Displays the starting menu for new users, allowing account creation or exiting the application.
 */
public class MainMenu {

    /**
     * Displays the start menu options and processes the user's choice.
     * Users can create an account or exit the application.
     *
     * @param scanner    the scanner used to read user input
     * @param controller the controller managing account-related actions
     * @return true if the application should continue running; false if it should exit
     */
    public static boolean display(Scanner scanner, Controller controller) {
        System.out.println("==== Welcome to the App ====");
        System.out.println("1. Create Account");
        System.out.println("0. Exit");
        System.out.println("============================");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        try {
            switch (choice) {
                case "1":
                    AccountAction.handleCreateAccount(scanner, controller);
                    break;
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    return false;
                default:
                    throw new ValidationException("Invalid option. Please select 1 or 0.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
