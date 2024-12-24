package controller;

import model.Customer;
import model.User;
import service.UserService;

import java.util.List;

public class UserController {
    private final UserService userService;

    /**
     * Constructor for UserController.
     * @param userService The instance of AccountService used to perform account operations.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the currently logged-in user.
     * @return The current user, or null if no user is logged in.
     */
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    /**
     * Retrieves a list of all registered users.
     * @return A list containing all users in the system.
     */
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Checks if a given username is already taken.
     * @param username The username to check for availability.
     * @return true if the username is taken; false if it's available.
     */
    public boolean isUsernameTaken(String username) {
        return userService.takenUsername(username);
    }

    /**
     * Validates if the provided email belongs to a specific domain.
     * @param email The email to be validated.
     * @return true if the email ends with the required domain suffix; false otherwise.
     */
    public boolean domainEmail(String email) {
        return userService.domainEmail(email);
    }

    /**
     * Creates a new user account if the provided information is valid.
     * Displays success or failure messages based on the result.
     * @param role The role of the new user, such as "Customer" or "Admin".
     * @param username The desired username for the account.
     * @param email The email associated with the account.
     * @param password The password for the account.
     */
    public void createAccount(String role, String username, String email, String password) {
        if (username == null || username.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("All fields are required for account creation.");
            return;
        }
        boolean success = userService.createAccount(role, username, email, password);
        if (success) {
            System.out.println("Account created successfully.");
        } else {
            System.out.println("Failed to create account.");
        }
    }

    /**
     * Attempts to log in a user with the provided username and password.
     * Displays a success message if login is successful or an error message if it fails.
     * @param username The username for login.
     * @param password The password for login.
     */
    public void login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Username and password are required for login.");
            return;
        }
        boolean success = userService.login(username, password);
        if (success) {
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Login failed. Incorrect username or password.");
        }
    }

    /**
     * Logs out the currently logged-in user.
     * Displays a success message if logout is successful or an error message if no user is logged in.
     */
    public void logout() {
        boolean success = userService.logout();
        if (success) {
            System.out.println("Logout successful.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    /**
     * Deletes the account with the specified ID if the current user has permission.
     * Displays a success message if deletion is successful or an error message if it fails.
     * @param id The ID of the account to delete.
     */
    public void deleteAccount(int id) {
        boolean success = userService.deleteAccount(id);
        if (success) {
            System.out.println("Account with ID " + id + " has been deleted.");
        } else {
            System.out.println("Failed to delete account. Either the account was not found or you lack the permissions.");
        }
    }

    public Customer findCustomerByID(int customerID) {
        return userService.findCustomerByID(customerID);
    }
}
