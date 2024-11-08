package presentation;

import controller.Controller;
import java.util.Scanner;

public class StartMenu {
    public static boolean display(Scanner scanner, Controller controller) {
        System.out.println("==== Welcome to the App ====");
        System.out.println("1. Create Account");
        System.out.println("0. Exit");
        System.out.println("============================");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                MenuActions.handleCreateAccount(scanner, controller);
                break;
            case "0":
                System.out.println("Exiting the application. Goodbye!");
                return false; // End the application
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }
}
