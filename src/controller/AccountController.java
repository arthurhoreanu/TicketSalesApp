package controller;

import service.AccountService;

public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void createAccount(String role, String username, String password, String email) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || email == null || email.isEmpty()) {
            System.out.println("All fields are required for account creation.");
            return;
        }

        boolean success = accountService.createAccount(role, username, password, email);
        if (success) {
            System.out.println("Account created successfully.");
        } else {
            System.out.println("Failed to create account. Username may already exist or role is invalid.");
        }
    }

    public void login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Username and password are required for login.");
            return;
        }

        boolean success = accountService.login(username, password);
        if (success) {
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Login failed. Incorrect username or password.");
        }
    }

    public void logout() {
        boolean success = accountService.logout();
        if (success) {
            System.out.println("Logout successful.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void deleteAccount(int id) {
        boolean success = accountService.deleteAccount(id);
        if (success) {
            System.out.println("Account with ID " + id + " has been deleted.");
        } else {
            System.out.println("Failed to delete account. Either the account was not found or you lack the permissions.");
        }
    }
}
