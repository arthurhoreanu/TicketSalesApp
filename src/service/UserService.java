package service;

import model.Admin;
import model.Customer;
import model.User;
import repository.DBRepository;
import repository.FileRepository;
import repository.IRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class UserService {
    private final IRepository<User> userIRepository;
    private final CustomerService customerService;
    private User currentUser;
    private final FileRepository<Admin> adminFileRepository;
    private final FileRepository<Customer> customerFileRepository;

    public UserService(IRepository<User> userRepository, CustomerService customerService) {
        this.userIRepository = userRepository;
        this.customerService = customerService;
        this.adminFileRepository = new FileRepository<>("src/repository/data/admins.csv", Admin::fromCsv);
        this.customerFileRepository = new FileRepository<>("src/repository/data/customers.csv", Customer::fromCsv);
        syncFromCsv();
    }

    private void syncFromCsv() {
        List<Admin> admins = adminFileRepository.getAll();
        List<Customer> customers = customerFileRepository.getAll();
        for (Admin admin : admins) {
            userIRepository.create(admin);
        }
        for (Customer customer : customers) {
            userIRepository.create(customer);
        }
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
        return userIRepository.getAll();
    }

    /**
     * Checks if the given username is already taken by another user.
     * @param username The username to be checked.
     * @return true if the username is already taken, false if the username is unique.
     */
    public boolean takenUsername(String username) {
        for (User user : userIRepository.getAll()) {
            if (user.getUsername().equals(username)) {
                return true; // Username exists
            }
        }
        return false; // Username is unique
    }

    /**
     * Validates if the email is from the "@tsc.com" domain.
     * @param email The email to be validated.
     * @return true if the email ends with "@tsc.com", false otherwise.
     */
    public boolean domainEmail(String email) {
        return email.endsWith("@tsc.com"); // [TicketSalesCompany placeholder]
    }

    /**
     * Creates a new user account with the specified details.
     * @param role The role of the new user ("Customer" or "Admin").
     * @param username The username of the new user.
     * @param email The email of the new user.
     * @param password The password for the new user.
     * @return true if the account is successfully created, false if the account creation failed (e.g., username is taken or invalid role/email).
     */
    public boolean createAccount(String role, String username, String email, String password) {
        if (takenUsername(username)) {
            return false;
        }
        int newID = userIRepository.getAll().size() + 1;
        if ("Customer".equalsIgnoreCase(role)) {
            Customer customer = new Customer(newID, username, email, password);
            userIRepository.create(customer);
            customerFileRepository.create(customer);
            return true;
        } else if ("Admin".equalsIgnoreCase(role) && domainEmail(email)) {
            Admin admin = new Admin(newID, username, email, password);
            userIRepository.create(admin);
            adminFileRepository.create(admin);
            return true;
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
            return false;
        }
        User userToDelete = userIRepository.read(id);
        if (userToDelete != null) {
            userIRepository.delete(id);
            if (userToDelete instanceof Admin) {
                adminFileRepository.delete(id);
            } else if (userToDelete instanceof Customer) {
                customerFileRepository.delete(id);
            }
            return true;
        }
        return false;
    }

    public Customer findCustomerByID(int customerID) {
        return userIRepository.getAll().stream()
                .filter(user -> user instanceof Customer)
                .map(user -> (Customer) user)
                .filter(customer -> customer.getID() == customerID)
                .findFirst()
                .orElse(null);
    }

}