package presentation;

import controller.Controller;
import java.util.Scanner;

/**
 * Displays the main login menu, allowing users to create an account, log in, or exit the application.
 */
public class LoginMenu {

    /**
     * Displays the main login menu options to the user and processes their choice.
     * Users can create an account, log in, or exit the application.
     *
     * @param scanner    the scanner used to read user input
     * @param controller the controller managing account-related actions
     * @return true if the application should continue running; false if it should exit
     */
    public static boolean display(Scanner scanner, Controller controller) {
        System.out.println("==== Main Menu ====");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.println("===================");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                AccountAction.handleCreateAccount(scanner, controller);
                break;
            case "2":
                AccountAction.handleLogin(scanner, controller);
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
