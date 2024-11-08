package controller;

import model.User;

import java.util.List;

public class Controller {
    private final AccountController accountController;

    public Controller(AccountController accountController) {
        this.accountController = accountController;
    }

    public User getCurrentUser() {
        return accountController.getCurrentUser();
    }

    public List<User> getAllUsers() {
        return accountController.getAllUsers();
    }

    public boolean isUsernameTaken(String username) {
        return accountController.isUsernameTaken(username);
    }

    public boolean domainEmail(String email) {
        return accountController.domainEmail(email);
    }

    public void createAccount(String role, String username, String password, String email) {
        accountController.createAccount(role, username, password, email);
    }

    public void login(String username, String password) {
        accountController.login(username, password);
    }

    public void logout() {
        accountController.logout();
    }

    public void deleteAccount(int id) {
        accountController.deleteAccount(id);
    }

}
