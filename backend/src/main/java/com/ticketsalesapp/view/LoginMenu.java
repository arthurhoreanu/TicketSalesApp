package com.ticketsalesapp.view;

import com.ticketsalesapp.exception.ValidationException;
import com.ticketsalesapp.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class LoginMenu {

    private final AccountAction accountAction;

    @Autowired
    public LoginMenu(AccountAction accountAction) {
        this.accountAction = accountAction;
    }

    public User display(Scanner scanner) {
        while (true) {
            System.out.println("==== Main Menu ====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.println("===================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        accountAction.handleCreateAccount(scanner);
                        break;
                    case "2":
                        User user = accountAction.handleLogin(scanner);
                        if (user != null) {
                            return user;
                        }
                        System.out.println("Invalid credentials. Please try again.");
                        break;
                    case "0":
                        System.out.println("Exiting the application. Goodbye!");
                        return null;
                    default:
                        throw new ValidationException("Invalid option. Please select 1, 2, or 0.");
                }
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
