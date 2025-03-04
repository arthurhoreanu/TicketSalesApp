package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.user.User;
import main.java.com.ticketsalesapp.service.UserService;

import java.util.List;

public class UserController {
    private final UserService userService;

    /**
     * Constructor for UserController.
     * @param userService The instance of UserService used to perform account operations.
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
        boolean isTaken = userService.takenUsername(username);
        if (isTaken) {
            System.out.println("The username '" + username + "' is already taken.");
        } else {
            System.out.println("The username '" + username + "' is available.");
        }
        return isTaken;
    }

    /**
     * Validates if the provided email belongs to a specific domain.
     * @param email The email to be validated.
     * @return true if the email ends with the required domain suffix; false otherwise.
     */
    public boolean domainEmail(String email) {
        boolean isValid = userService.domainEmail(email);
        if (isValid) {
            System.out.println("The email '" + email + "' is valid for the required domain.");
        } else {
            System.out.println("The email '" + email + "' is not valid for the required domain.");
        }
        return isValid;
    }

    /**
     * Creates a new user account if the provided information is valid.
     * Displays success or failure messages based on the result.
     * @param role The role of the new user, such as "Customer" or "Admin".
     * @param username The desired username for the account.
     * @param email The email associated with the account.
     * @param password The password for the account.
     * @return true if the account was successfully created, false otherwise.
     */
    public boolean createAccount(String role, String username, String email, String password) {
        if (username == null || username.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("All fields are required for account creation.");
            return false;
        }
        boolean success = userService.createAccount(role, username, email, password);
        if (success) {
            System.out.println("Account created successfully for role '" + role + "'.");
        } else {
            System.out.println("Failed to create account. Please check the provided information.");
        }
        return success;
    }

    /**
     * Attempts to log in a user with the provided username and password.
     * Displays a success message if login is successful or an error message if it fails.
     * @param username The username for login.
     * @param password The password for login.
     * @return true if login is successful, false otherwise.
     */
    public boolean login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Username and password are required for login.");
            return false;
        }
        boolean success = userService.login(username, password);
        if (success) {
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Login failed. Incorrect username or password.");
        }
        return success;
    }

    /**
     * Logs out the currently logged-in user.
     * Displays a success message if logout is successful or an error message if no user is logged in.
     * @return true if logout is successful, false otherwise.
     */
    public boolean logout() {
        boolean success = userService.logout();
        if (success) {
            System.out.println("Logout successful.");
        } else {
            System.out.println("No user is currently logged in.");
        }
        return success;
    }

    /**
     * Deletes the account with the specified ID if the current user has permission.
     * Displays a success message if deletion is successful or an error message if it fails.
     * @param id The ID of the account to delete.
     * @return true if the account was successfully deleted, false otherwise.
     */
    public boolean deleteAccount(int id) {
        boolean success = userService.deleteAccount(id);
        if (success) {
            System.out.println("Account with ID " + id + " has been deleted.");
        } else {
            System.out.println("Failed to delete account. Either the account was not found or you lack the permissions.");
        }
        return success;
    }

    /**
     * Finds a user by their ID.
     * @param id The ID of the user to find.
     * @return The User object, or null if no user with the given ID exists.
     */
    public User findUserByID(int id) {
        User user = userService.findUserByID(id);
        if (user != null) {
            System.out.println("User found: " + user.getUsername());
        } else {
            System.out.println("No user found with ID " + id + ".");
        }
        return user;
    }

    /**
     * Finds a customer by their ID.
     * @param customerID The ID of the customer to find.
     * @return The Customer object, or null if no customer with the given ID exists.
     */
    public Customer findCustomerByID(int customerID) {
        Customer customer = userService.findCustomerByID(customerID);
        if (customer != null) {
            System.out.println("Customer found: " + customer.getUsername());
        } else {
            System.out.println("No customer found with ID " + customerID + ".");
        }
        return customer;
    }
}
