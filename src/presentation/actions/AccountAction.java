package presentation.actions;

import controller.Controller;
import model.*;

import java.util.Scanner;

public class AccountAction {
    public static void handleCreateAccount(Scanner scanner, Controller controller) {
        System.out.println("=== Create Account ===");

        String role;
        while (true) {
            System.out.print("Enter role (Admin/Customer): ");
            role = scanner.nextLine();
            if ("Admin".equalsIgnoreCase(role) || "Customer".equalsIgnoreCase(role)) {
                break;
            } else {
                System.out.println("Invalid role. Please enter 'Admin' or 'Customer'.");
            }
        }

        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (!controller.isUsernameTaken(username)) {
                break;
            } else {
                System.out.println("Username already exists. Please try a different username.");
            }
        }

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if(!(controller.domainEmail(email))) {
                break;
            }
            else {
                System.out.println("Admins must have a domain email.");
            }
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        controller.createAccount(role, username, password, email);
    }

    public static void handleLogin(Scanner scanner, Controller controller) {
        System.out.println("=== Login ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        controller.login(username, password);
    }

    public static void handleDeleteUserAccount(Scanner scanner, Controller controller) {
        System.out.println("=== Delete User Account ===");

        for (User user : controller.getAllUsers()) {
            if (!(user instanceof Admin)) {
                System.out.println("ID: " + user.getID() + ", Username: " + user.getUsername());
            }
        }
        System.out.print("Enter the ID of the account to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        controller.deleteAccount(id);
    }


}
