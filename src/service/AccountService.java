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
        if (userIRepository.getAll().stream()
                .anyMatch(user -> user.getUsername().equals(username))) {
            return false; }

        int newId = userIRepository.getAll().size() + 1;
        User newUser;

        if ("Customer".equalsIgnoreCase(role)) {
            newUser = new Customer(newId, username, password, email);
        } else if ("Admin".equalsIgnoreCase(role)) {
            newUser = new Admin(newId, username, password, email);
        } else {
            System.out.println("Invalid user type.");
            return false;
        }

        userIRepository.create(newUser);
        System.out.println("Account created successfully.");
        return true;
    }
}
