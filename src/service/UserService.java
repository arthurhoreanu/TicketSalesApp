package service;

import model.Admin;
import model.Customer;
import model.User;
import repository.DBRepository;
import repository.FileRepository;
import repository.IRepository;

import java.util.List;

public class UserService {
    private final IRepository<User> userRepository;
    private final CustomerService customerService;
    private User currentUser;
    private final FileRepository<Admin> adminFileRepository;
    private final DBRepository<Admin> adminDBRepository;
    private final FileRepository<Customer> customerFileRepository;
    private final DBRepository<Customer> customerDBRepository;

    public UserService(IRepository<User> userRepository, CustomerService customerService) {
        this.userRepository = userRepository;
        this.customerService = customerService;
        this.adminFileRepository = new FileRepository<>("src/repository/data/admins.csv", Admin::fromCsv);
        this.adminDBRepository = new DBRepository<>(Admin.class);
        this.customerFileRepository = new FileRepository<>("src/repository/data/customers.csv", Customer::fromCsv);
        this.customerDBRepository = new DBRepository<>(Customer.class);

        syncFromSource(customerFileRepository, customerDBRepository);
        syncFromSource(adminFileRepository, adminDBRepository);
    }

    private <T extends User> void syncFromSource(FileRepository<T> fileRepository, DBRepository<T> dbRepository) {
        List<T> usersFromFile = fileRepository.getAll();
        List<T> usersFromDB = dbRepository.getAll();

        for (T user : usersFromFile) {
            if (findUserByID(user.getID()) == null) {
                userRepository.create(user);
            }
        }

        for (T user : usersFromDB) {
            if (findUserByID(user.getID()) == null) {
                userRepository.create(user);
            }
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
        return userRepository.getAll();
    }

    /**
     * Checks if the given username is already taken by another user.
     * @param username The username to be checked.
     * @return true if the username is already taken, false if the username is unique.
     */
    public boolean takenUsername(String username) {
        for (User user : userRepository.getAll()) {
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

    public boolean createCustomer(String username, String email, String password) {
        if (takenUsername(username)) {
            return false;
        }

        Customer customer = new Customer(0, username, email, password);
        customerDBRepository.create(customer); // DB generează ID-ul

        if (customer.getID() == 0) {
            throw new IllegalStateException("Database did not generate an ID for the customer.");
        }

        customerFileRepository.create(customer);
        userRepository.create(customer);
        return true;
    }

    public boolean createAdmin(String username, String email, String password) {
        if (takenUsername(username) || !domainEmail(email)) {
            return false;
        }

        Admin admin = new Admin(0, username, email, password);
        adminDBRepository.create(admin); // DB generează ID-ul

        if (admin.getID() == 0) {
            throw new IllegalStateException("Database did not generate an ID for the admin.");
        }

        adminFileRepository.create(admin);
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
            return false;
        }
        User userToDelete = userRepository.read(id);
        if (userToDelete != null) {
            userRepository.delete(id);
            if (userToDelete instanceof Admin) {
                adminFileRepository.delete(id);
                adminDBRepository.delete(id);
            } else if (userToDelete instanceof Customer) {
                customerFileRepository.delete(id);
                customerDBRepository.delete(id);
            }
            return true;
        }
        return false;
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