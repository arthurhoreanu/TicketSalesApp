package main.java.com.ticketsalesapp.view;

import main.java.com.ticketsalesapp.controller.ApplicationController;
import main.java.com.ticketsalesapp.exception.ValidationException;

import java.util.Scanner;

public class LoginMenu {

    public static boolean display(Scanner scanner, ApplicationController applicationController) {
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
                    AccountAction.handleCreateAccount(scanner, applicationController);
                    break;
                case "2":
                    AccountAction.handleLogin(scanner, applicationController);
                    break;
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    return false;
                default:
                    throw new ValidationException("Invalid option. Please select 1, 2, or 0.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}