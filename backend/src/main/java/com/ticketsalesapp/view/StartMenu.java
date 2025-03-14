package com.ticketsalesapp.view;

import com.ticketsalesapp.exception.ValidationException;
import com.ticketsalesapp.repository.factory.*;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Displays the initial menu to choose the data representation (InMemory, File, DB).
 */
@Component
public class StartMenu {

    public static RepositoryFactory select(Scanner scanner) {
        try {
            System.out.println("==== Choose Data Representation ====");
            System.out.println("1. InMemory");
//            System.out.println("2. File"); // TODO later
//            System.out.println("3. Database"); // TODO later
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("You selected InMemory storage.");
                    return new InMemoryRepositoryFactory();
//                case "2":
//                    System.out.println("You selected File storage.");
//                    return new FileRepositoryFactory();
//                case "3":
//                    System.out.println("You selected Database storage.");
//                    return new DBRepositoryFactory();
                default:
                    throw new ValidationException("Invalid choice. Please select a valid option (1, 2, or 3).");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            System.out.println("Defaulting to InMemory storage.");
            return new InMemoryRepositoryFactory();
        }
    }
}