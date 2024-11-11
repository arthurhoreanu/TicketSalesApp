package presentation.admin;

import controller.Controller;

import java.util.Scanner;

public class AdminTicketMenu {
    public static void display(Scanner scanner, Controller controller) {
        boolean inTicketMenu = true;

        while (inTicketMenu) {
            // TODO this menu
            System.out.println("==== Venue Management ====");
            System.out.println("0. Back to Admin Menu");
            System.out.println("==========================");
        }

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "0":
                inTicketMenu = false;
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        System.out.println();
    }

    // TODO methods go here
}