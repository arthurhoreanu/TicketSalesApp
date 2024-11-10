package service;

import model.Admin;
import model.Customer;
import model.FavouriteItem;
import model.User;
import repository.IRepository;

import java.util.List;

public class AccountService {
    private final IRepository<User> userIRepository;
    private final CustomerService customerService;
    private User currentUser;

    public AccountService(IRepository<User> userIRepository, CustomerService customerService) {
        this.userIRepository = userIRepository;
        this.customerService = customerService;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getAllUsers() {
        return userIRepository.getAll();
    }

    public boolean takenUsername(String username) {
        for (User user : userIRepository.getAll()) {
            if (user.getUsername().equals(username)) {
                return true; // Username exists
            }
        }
        return false; // Username is unique
    }

    public boolean domainEmail(String email) {
        return email.endsWith("@tsc.com"); // [TicketSalesCompany placeholder
    }

    public boolean createAccount(String role, String username, String email, String password) {
        if (takenUsername(username)) {
            return false;
        }

        // Generate a new unique ID for the user
        int newID = userIRepository.getAll().size() + 1;

        // Create the new user based on the specified role
        User newUser;
        if ("Customer".equalsIgnoreCase(role)) newUser = new Customer(newID, username, email, password);
        else if ("Admin".equalsIgnoreCase(role) && domainEmail(email))
            newUser = new Admin(newID, username, email, password);
        else return false;

        // Add the new user to the repository
        userIRepository.create(newUser);
        return true; // Success
    }

    public boolean login(String username, String password) {
        for (User user : userIRepository.getAll()) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    currentUser = user;
                    if (currentUser instanceof Customer) {
                        customerService.setCurrentCustomer((Customer) currentUser);
                        return true;
                    } else if (currentUser instanceof Admin) {
                        return true;
                    }
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