package main.java.com.ticketsalesapp;

import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.user.User;
import main.java.com.ticketsalesapp.service.user.AdminService;
import main.java.com.ticketsalesapp.service.user.CustomerService;
import main.java.com.ticketsalesapp.service.user.UserSession;
import main.java.com.ticketsalesapp.view.admin.AdminMenu;
import main.java.com.ticketsalesapp.view.*;
import main.java.com.ticketsalesapp.repository.factory.RepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleApp {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final UserSession userSession;
    private final AdminMenu adminMenu;
    private final CustomerMenu customerMenu;
    private final LoginMenu loginMenu;
    private final MainMenu mainMenu;

    private RepositoryFactory repositoryFactory;
    private User currentUser = null;

    @Autowired
    public ConsoleApp(AdminService adminService, CustomerService customerService, UserSession userSession, AdminMenu adminMenu,
                      CustomerMenu customerMenu, LoginMenu loginMenu, MainMenu mainMenu) {
        this.adminService = adminService;
        this.customerService = customerService;
        this.userSession = userSession;
        this.adminMenu = adminMenu;
        this.customerMenu = customerMenu;
        this.loginMenu = loginMenu;
        this.mainMenu = mainMenu;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        // 1. Choose data representation
        this.repositoryFactory = StartMenu.select(scanner);
        boolean running = true;

        // 2. No users in our repos
        running = mainMenu.display(scanner);
        if (!running) {
            scanner.close();
            return;
        }

        // 3. Main loop
        while (running) {
            if (currentUser == null) {
                // Are there users in our repos?
                boolean hasUsers = !adminService.getAllAdmins().isEmpty() ||
                        !customerService.getAllCustomers().isEmpty();

                if (!hasUsers) {
                    // If we somehow don't, go back to "No Users Menu"
                    running = mainMenu.display(scanner);
                    if (!running) break;
                } else {
                    // If we do, then "LogIn Menu"
                    currentUser = loginMenu.display(scanner);
                    if (currentUser == null) {
                        running = false;
                    }
                }
            } else if (currentUser instanceof Admin) {
                boolean shouldContinue = adminMenu.display(scanner, (Admin) currentUser);
                if (!shouldContinue) {
                    running = false;
                } else {
                    currentUser = null;  // Back to "LogIn Menu"
                }
            } else if (currentUser instanceof Customer) {
                boolean shouldContinue = customerMenu.display(scanner, (Customer) currentUser);
                if (!shouldContinue) {
                    running = false;
                } else {
                    currentUser = null;  // Back to "LogIn Menu"
                }
            }
        }

        scanner.close();
    }
}