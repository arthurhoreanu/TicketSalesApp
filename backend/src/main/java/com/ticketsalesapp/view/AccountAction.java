package com.ticketsalesapp.view;

import com.ticketsalesapp.controller.user.AdminController;
import com.ticketsalesapp.controller.user.CustomerController;
import com.ticketsalesapp.exception.BusinessLogicException;
import com.ticketsalesapp.exception.ValidationException;
import com.ticketsalesapp.model.user.User;
import com.ticketsalesapp.service.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AccountAction {

    private final AdminController adminController;
    private final CustomerController customerController;
    private final UserSession userSession;

    @Autowired
    public AccountAction(AdminController adminController, CustomerController customerController, UserSession userSession) {
        this.adminController = adminController;
        this.customerController = customerController;
        this.userSession = userSession;
    }

    public void handleCreateAccount(Scanner scanner) {
        System.out.println("=== Create Account ===");

        String role;
        while (true) {
            System.out.print("Enter role (Admin/Customer): ");
            role = scanner.nextLine();
            if ("Admin".equalsIgnoreCase(role) || "Customer".equalsIgnoreCase(role)) {
                break;
            } else {
                System.out.println("❌ Invalid role. Please enter 'Admin' or 'Customer'.");
            }
        }

        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (role.equalsIgnoreCase("Admin") && adminController.usernameExists(username)) {
                System.out.println("❌ Error: Username already taken. Try another one.");
                continue;
            } else if (role.equalsIgnoreCase("Customer") && customerController.usernameExists(username)) {
                System.out.println("❌ Error: Username already taken. Try another one.");
                continue;
            }
            break;
        }

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (role.equalsIgnoreCase("Admin") && !adminController.domainEmail(email)) {
                System.out.println("❌ Error: Admin must be a domain email.");
                continue;
            }
            break;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            if ("Admin".equalsIgnoreCase(role)) {
                adminController.createAdmin(username, email, password);
            } else {
                customerController.createCustomer(username, email, password);
            }
            System.out.println("✅ Account created successfully!");
        } catch (ValidationException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return;
        }
    }

    public User handleLogin(Scanner scanner) {
        System.out.println("=== Login ===");
        User loggedInUser = null;

        while (true) {
            try {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();

                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                try {
                    loggedInUser = adminController.login(username, password);
                    System.out.println("✅ Successfully logged in as Admin!");
                    break;
                } catch (BusinessLogicException e) {
                    try {
                        loggedInUser = customerController.login(username, password);
                        System.out.println("✅ Successfully logged in as Customer!");
                        break;
                    } catch (BusinessLogicException ex) {
                        System.out.println("❌ Error: Invalid credentials");
                        System.out.println("Would you like to try again? (y/n)");
                        if (!scanner.nextLine().equalsIgnoreCase("y")) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("❌ An unexpected error occurred: " + e.getMessage());
                break;
            }
        }
        return loggedInUser;
    }

    public boolean handleLogout(User user) throws BusinessLogicException {
        if (userSession.isAdmin(user)) {
            adminController.logout();
        } else {
            customerController.logout();
        }
        System.out.println("Logged out successfully.");
        return !adminController.getAllAdmins().isEmpty() || !customerController.getAllCustomers().isEmpty();
    }

    public void handleDeleteAccount(User user, Scanner scanner) throws BusinessLogicException {
        if (!userSession.isAdmin(user)) {
            throw new BusinessLogicException("Only administrators can delete accounts");
        }

        System.out.println("=== Delete User Account ===");
        System.out.println("1. Delete Admin Account");
        System.out.println("2. Delete Customer Account");
        System.out.print("Choose account type to delete (1/2): ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("\n=== Administrators List ===");
                adminController.displayAllAdmins();
                System.out.print("\nEnter Admin ID to delete: ");
                try {
                    int adminId = Integer.parseInt(scanner.nextLine());

                    // Verify if the current admin wants to delete their account
                    if (adminId == user.getId()) {
                        System.out.println("You cannot delete your own account during an active session!");
                        break;
                    }

                    if (adminController.findAdminById(adminId) != null) {
                        adminController.deleteAdmin(adminId);
                        System.out.println("Admin with ID " + adminId + " was successfully deleted.");
                    } else {
                        System.out.println("No admin found with ID: " + adminId);
                    }
                } catch (NumberFormatException e) {
                    throw new BusinessLogicException("Invalid ID format. Please enter a valid number.");
                }
                break;

            case "2":
                System.out.println("\n=== Customers List ===");
                customerController.getAllCustomers();
                System.out.print("\nEnter Customer ID to delete: ");
                try {
                    int customerId = Integer.parseInt(scanner.nextLine());
                    if (customerController.findCustomerById(customerId) != null) {
                        customerController.deleteCustomer(customerId);
                        System.out.println("Customer with ID " + customerId + " was successfully deleted.");
                        break;
                    } else {
                        System.out.println("No customer found with ID: " + customerId);
                    }
                } catch (NumberFormatException e) {
                    throw new BusinessLogicException("Invalid ID format. Please enter a valid number.");
                }

                break;

            default:
                System.out.println("Invalid choice. Please select 1 for Admin or 2 for Customer.");
                break;
        }
    }


}