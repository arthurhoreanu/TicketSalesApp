package com.ticketsalesapp.view;

import com.ticketsalesapp.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Displays the starting menu for new users, allowing account creation or exiting the application.
 */
@Component
public class MainMenu {

    private final AccountAction accountAction;

    @Autowired
    public MainMenu(AccountAction accountAction) {
        this.accountAction = accountAction;
    }

    /**
     * Displays the start menu options and processes the user's choice.
     *
     * @param scanner the scanner used to read user input
     * @return true if the application should continue running; false if it should exit
     */
    public boolean display(Scanner scanner) {
        System.out.println("==== Welcome to the App ====");
        System.out.println("1. Create Account");
        System.out.println("0. Exit");
        System.out.println("============================");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        try {
            switch (choice) {
                case "1":
                    accountAction.handleCreateAccount(scanner);
                    break;
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    return false;
                default:
                    throw new ValidationException("Invalid option. Please select 1 or 0.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
