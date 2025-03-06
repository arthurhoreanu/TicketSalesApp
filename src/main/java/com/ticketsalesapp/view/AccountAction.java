package main.java.com.ticketsalesapp.view;

import main.java.com.ticketsalesapp.controller.ApplicationController;
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
     * @param applicationController the controller managing account-related actions
     */
    public static void handleCreateAccount(Scanner scanner, ApplicationController applicationController) {
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
            if (!applicationController.isUsernameTaken(username)) {
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
            else if ("Admin".equalsIgnoreCase(role) && applicationController.domainEmail(email)) break;
            else System.out.println("Admins must have a domain email.");
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        applicationController.createAccount(role, username, email, password);
    }

    /**
     * Handles user login by prompting for username and password.
     *
     * @param scanner    the scanner to read user input
     * @param applicationController the controller managing login actions
     */
    public static void handleLogin(Scanner scanner, ApplicationController applicationController) {
        System.out.println("=== Login ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        applicationController.login(username, password);
    }

    /**
     * Handles deletion of a user account by listing accounts, prompting for an account ID, and deleting the selected account.
     * Excludes Admin accounts from deletion.
     *
     * @param scanner    the scanner to read user input
     * @param applicationController the controller managing account deletion actions
     */
    public static void handleDeleteUserAccount(Scanner scanner, ApplicationController applicationController) {
        System.out.println("=== Delete User Account ===");

        for (User user : applicationController.getAllUsers()) {
            if (!(user instanceof Admin)) {
                System.out.println("ID: " + user.getID() + ", Username: " + user.getUsername());
            }
        }
        System.out.print("Enter the ID of the account to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        applicationController.deleteAccount(id);
    }
}
