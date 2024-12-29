package presentation;
import repository.factory.*;

import java.util.Scanner;

/**
 * Displays the initial menu to choose the data representation (InMemory, File, DB).
 */
public class StartMenu {

    public static RepositoryFactory select(Scanner scanner) {
        System.out.println("==== Choose Data Representation ====");
        System.out.println("1. InMemory");
        System.out.println("2. File");
        System.out.println("3. Database");
        System.out.print("Your choice: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("You selected InMemory storage.");
                return new InMemoryRepositoryFactory();
            case "2":
                System.out.println("You selected File storage.");
                return new FileRepositoryFactory();
            case "3":
                System.out.println("You selected Database storage.");
                return new DBRepositoryFactory();
            default:
                System.out.println("Invalid choice. Defaulting to InMemory storage.");
                return new InMemoryRepositoryFactory();
        }
    }
}