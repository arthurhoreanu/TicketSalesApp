package service;

import exception.BusinessLogicException;
import exception.ValidationException;
import model.Admin;
import model.Customer;
import model.User;
import repository.IRepository;
import repository.factory.RepositoryFactory;

import java.util.List;

public class UserService {
    private final IRepository<User> userRepository;
    private final CustomerService customerService;
    private User currentUser;

    public UserService(RepositoryFactory repositoryFactory, CustomerService customerService) {
        this.userRepository = repositoryFactory.createUserRepository();
        this.customerService = customerService;
    }

    /**
     * Retrieves the current logged-in user.
     * @return The current logged-in user, or null if no user is logged in.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Retrieves all users in the repository.
     * @return A list of all users stored in the repository.
     */
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    /**
     * Checks if the given username is already taken by another user.
     * @param username The username to be checked.
     * @return true if the username is already taken, false if the username is unique.
     */
    public boolean takenUsername(String username) {
        List<User> users = userRepository.getAll();
        for (User user : users) {
            if (user.getUsername() != null && user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Validates if the email is from the "@tsc.com" domain.
     * @param email The email to be validated.
     * @return true if the email ends with "@tsc.com", false otherwise.
     */
    public boolean domainEmail(String email) {
        return email.endsWith("@tsc.com"); // [TicketSalesCompany placeholder]
    }

    public boolean createCustomer(String username, String email, String password) {
        if (takenUsername(username)) {
            throw new BusinessLogicException("Username '" + username + "' is already taken.");
        }
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(password);
        userRepository.create(customer);
        return true;
    }

    public boolean createAdmin(String username, String email, String password) {
        if (takenUsername(username)) {
            throw new BusinessLogicException("Username '" + username + "' is already taken.");
        }
        if (!domainEmail(email)) {
            throw new BusinessLogicException("Admins must have domain email.");
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setEmail(email);
        userRepository.create(admin);
        return true;
    }

    public boolean createAccount(String role, String username, String email, String password) {
        if ("Customer".equalsIgnoreCase(role)) {
            return createCustomer(username, email, password);
        } else if ("Admin".equalsIgnoreCase(role)) {
            return createAdmin(username, email, password);
        }
        return false;
    }

    /**
     * Logs in a user with the provided username and password.
     * @param username The username of the user trying to log in.
     * @param password The password of the user trying to log in.
     * @return true if login is successful (correct username and password), false if login fails (incorrect credentials).
     */
    public boolean login(String username, String password) {
        for (User user : userRepository.getAll()) {
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

    /**
     * Logs out the current user.
     * @return true if the user was logged out successfully, false if no user is logged in.
     */
    public boolean logout() {
        if (currentUser != null) {
            currentUser = null;
            return true; // Logout successful
        }
        return false; // No user logged in
    }

    /**
     * Deletes a user account with the specified ID.
     * @param id The ID of the user account to be deleted.
     * @return true if the account was deleted successfully, false if the user was not found or the current user is not an admin.
     */
    public boolean deleteAccount(int id) {
        if (currentUser == null || !(currentUser instanceof Admin)) {
            throw new BusinessLogicException("Only admins can delete accounts.");
        }
        User userToDelete = userRepository.read(id);
        if (userToDelete == null) {
            throw new ValidationException("User with ID " + id + " does not exist.");
        }
        userRepository.delete(id);
        return true;
    }

    public User findUserByID(int id) {
        return userRepository.read(id);
    }

    public Customer findCustomerByID(int customerID) {
        return userRepository.getAll().stream()
                .filter(user -> user instanceof Customer)
                .map(user -> (Customer) user)
                .filter(customer -> customer.getID() == customerID)
                .findFirst()
                .orElse(null);
    }

}