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
                System.out.println("Username already exists.");
                return false;
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
            System.out.println("Invalid user type.");
            return false;
        }

        // Add the new user to the repository
        userIRepository.create(newUser);
        System.out.println("Account created successfully.");
        return true;
    }

    public boolean login(String username, String password) {
        for (User user : userIRepository.getAll()) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    currentUser = user;
                    System.out.println("Login successful.");
                    return true;
                } else {
                    System.out.println("Incorrect password.");
                    return false;
                }
            }
        }
        System.out.println("Username not found.");
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Logged out: " + currentUser.getUsername());
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public boolean deleteAccount(int id) {
        // Check if the current user is an admin
        if (currentUser == null || !(currentUser instanceof Admin)) {
            System.out.println("Only an admin can delete accounts.");
            return false;
        }
        // Iterate over all users to find the user with the matching ID
        boolean found = false;
        for (User user : userIRepository.getAll()) {
            if (user.getID() == id) {
                found = true;
                break;
            }
        }
        // If the user with the specified ID was found, proceed with deletion
        if (found) {
            userIRepository.delete(id);
            System.out.println("Account deleted successfully.");
            return true;
        } else {
            System.out.println("Account not found.");
            return false;
        }
    }

}