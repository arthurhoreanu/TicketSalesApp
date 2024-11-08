package service;

import model.Admin;
import model.Customer;
import model.User;
import repository.IRepository;

public class AccountService {
    private final IRepository<User> userIRepository;
    private User currentUser;

    public AccountService(IRepository<User> userIRepository) {
        this.userIRepository = userIRepository;
    }

    public boolean createAccount(String role, String username, String password, String email) {
        // Check if username already exists
        for (User user : userIRepository.getAll()) {
            if (user.getUsername().equals(username)) {
                return false; // Username exists
            }
        }

        // Generate a new unique ID for the user
        int newId = userIRepository.getAll().size() + 1;

        // Create the new user based on the specified role
        User newUser;
        if ("Customer".equalsIgnoreCase(role)) {
            newUser = new Customer(newId, username, password, email);
        } else if ("Admin".equalsIgnoreCase(role)) {
            newUser = new Admin(newId, username, password, email);
        } else {
            return false; // Invalid role
        }

        // Add the new user to the repository
        userIRepository.create(newUser);
        return true; // Success
    }

    public boolean login(String username, String password) {
        for (User user : userIRepository.getAll()) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    currentUser = user;
                    return true; // Login successful
                } else {
                    return false; // Incorrect password
                }
            }
        }
        return false; // Username not found
    }

    public boolean logout() {
        if (currentUser != null) {
            currentUser = null;
            return true; // Logout successful
        }
        return false; // No user logged in
    }

    public boolean deleteAccount(int id) {
        // Check if the current user is an admin
        if (currentUser == null || !(currentUser instanceof Admin)) {
            return false; // Not an admin
        }

        // Check if the user with the specified ID exists
        boolean found = false;
        for (User user : userIRepository.getAll()) {
            if (user.getID() == id) {
                found = true;
                break;
            }
        }

        // Delete if found
        if (found) {
            userIRepository.delete(id);
            return true; // Deletion successful
        }
        return false; // User not found
    }
}
