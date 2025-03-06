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

        this.repositoryFactory = StartMenu.select(scanner);

        boolean running = true;

        while (running) {
            if (currentUser == null) {
                if (userSession.getCurrentUser().isEmpty()) {
                    running = mainMenu.display(scanner);
                    if (!running) break;
                }
                currentUser = loginMenu.display(scanner);
                if (currentUser == null) {
                    running = false;
                }
            } else if (currentUser instanceof Admin) {
                running = adminMenu.display(scanner, (Admin) currentUser);
                if (!running) currentUser = null;
            } else if (currentUser instanceof Customer) {
                running = customerMenu.display(scanner, (Customer) currentUser);
                if (!running) currentUser = null;
            }
        }

        scanner.close();
    }
}
