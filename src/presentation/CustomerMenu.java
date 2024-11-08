package presentation;

import controller.Controller;
import java.util.Scanner;

public class CustomerMenu {
    public static boolean display(Scanner scanner, Controller controller) {
        System.out.println("==== Customer Menu ====");
        System.out.println("1. Logout");
        System.out.println("0. Exit");
        System.out.println("=======================");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                controller.logout();
                System.out.println("Logged out successfully.");
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
