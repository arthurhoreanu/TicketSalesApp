package main.java.com.ticketsalesapp.view;

import main.java.com.ticketsalesapp.controller.user.AdminController;
import main.java.com.ticketsalesapp.controller.user.CustomerController;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.User;
import main.java.com.ticketsalesapp.service.user.AdminService;
import main.java.com.ticketsalesapp.service.user.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AccountAction {

    private final AdminController adminController;
    private final CustomerController customerController;
    private final CustomerService customerService;
    private final AdminService adminService;

    @Autowired
    public AccountAction(AdminController adminController, CustomerController customerController, CustomerService customerService, AdminService adminService) {
        this.adminController = adminController;
        this.customerController = customerController;
        this.customerService = customerService;
        this.adminService = adminService;
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
        if (user instanceof Admin) {
            adminController.logout();
        } else {
            customerController.logout();
        }
        System.out.println("Logged out successfully.");

        // Verificăm dacă există utilizatori în repo-uri
        if (!adminService.getAllAdmins().isEmpty() || !customerService.getAllCustomers().isEmpty()) {
            return true;  // Există utilizatori, ar trebui să mergem la login
        }
        return false;  // Nu există utilizatori, ieșim din aplicație
    }

}