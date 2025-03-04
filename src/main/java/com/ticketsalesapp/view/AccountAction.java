package main.java.com.ticketsalesapp.view;

import main.java.com.ticketsalesapp.controller.Controller;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.User;
import java.util.Scanner;

/**
 * Provides actions related to account management, such as creating, logging in, and deleting user accounts.
 */
public class AccountAction {

    /**
     * Handles account creation by prompting for role, username, email, and password.
     * Ensures unique usernames and valid domain emails for Admin accounts.
     *
     * @param scanner    the scanner to read user input
     * @param controller the controller managing account-related actions
     */
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
            if ("Customer".equalsIgnoreCase(role)) break;
            else if ("Admin".equalsIgnoreCase(role) && controller.domainEmail(email)) break;
            else System.out.println("Admins must have a domain email.");
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        controller.createAccount(role, username, email, password);
    }

    /**
     * Handles user login by prompting for username and password.
     *
     * @param scanner    the scanner to read user input
     * @param controller the controller managing login actions
     */
    public static void handleLogin(Scanner scanner, Controller controller) {
        System.out.println("=== Login ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        controller.login(username, password);
    }

    /**
     * Handles deletion of a user account by listing accounts, prompting for an account ID, and deleting the selected account.
     * Excludes Admin accounts from deletion.
     *
     * @param scanner    the scanner to read user input
     * @param controller the controller managing account deletion actions
     */
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
