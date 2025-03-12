package main.java.com.ticketsalesapp.view.admin;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.service.user.AdminService;
import main.java.com.ticketsalesapp.view.AccountAction;
import main.java.com.ticketsalesapp.view.LoginMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AdminMenu {

    private final AccountAction accountAction;
    private final AdminArtistMenu adminArtistMenu;
    private final AdminAthleteMenu adminAthleteMenu;

    @Autowired
    public AdminMenu(AccountAction accountAction, AdminArtistMenu adminArtistMenu, AdminAthleteMenu adminAthleteMenu) {
        this.accountAction = accountAction;
        this.adminArtistMenu = adminArtistMenu;
        this.adminAthleteMenu = adminAthleteMenu;
    }

    public boolean display(Scanner scanner, Admin admin) {
        try {
            System.out.println("==== Admin Menu ====");
            System.out.println("1. Logout");
            System.out.println("2. Delete User Account");
            System.out.println("3. Manage Events");
            System.out.println("4. Manage Tickets");
            System.out.println("5. Manage Venues");
            System.out.println("6. Manage Artists");
            System.out.println("7. Manage Athletes");
            System.out.println("0. Exit");
            System.out.println("====================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    return accountAction.handleLogout(admin);
                case "2":
                    accountAction.handleDeleteAccount(admin, scanner);
                    break;
                case "3":
                    System.out.println("Manage Events - Not implemented yet.");
                    break;
                case "4":
                    System.out.println("Manage Tickets - Not implemented yet.");
                    break;
                case "5":
                    System.out.println("Manage Venues - Not implemented yet.");
                    break;
                case "6":
                    adminArtistMenu.display(scanner);
                    break;
                case "7":
                    adminAthleteMenu.display(scanner);
                    break;
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    return false;
                default:
                    throw new ValidationException("Invalid option. Please select a number between 0 and 7.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
