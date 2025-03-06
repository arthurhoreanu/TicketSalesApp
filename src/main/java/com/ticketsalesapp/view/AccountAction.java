package main.java.com.ticketsalesapp.view;

import main.java.com.ticketsalesapp.model.user.User;
import main.java.com.ticketsalesapp.service.user.AdminService;
import main.java.com.ticketsalesapp.service.user.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AccountAction {

    private final AdminService adminService;
    private final CustomerService customerService;

    @Autowired
    public AccountAction(AdminService adminService, CustomerService customerService) {
        this.adminService = adminService;
        this.customerService = customerService;
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
                System.out.println("Invalid role. Please enter 'Admin' or 'Customer'.");
            }
        }
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if ("Admin".equalsIgnoreCase(role)) {
            adminService.createAdmin(username, email, password);
        } else {
            customerService.createCustomer(username, email, password);
        }
        System.out.println("Account created successfully!");
    }

    public User handleLogin(Scanner scanner) {
        System.out.println("=== Login ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = adminService.findByUsernameAndPassword(username, password);
        if (user == null) {
            user = customerService.findByUsernameAndPassword(username, password);
        }
        return user;
    }


}
