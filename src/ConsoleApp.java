import controller.Controller;
import controller.AccountController;
import model.User;
import repository.IRepository;
import repository.InMemoryRepository;
import service.AccountService;

import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        IRepository<User> userRepository = new InMemoryRepository<>();
        AccountService accountService = new AccountService(userRepository);
        AccountController accountController = new AccountController(accountService);
        Controller controller = new Controller(accountController);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleCreateAccount(scanner, controller);
                    break;
                case "2":
                    handleLogin(scanner, controller);
                    break;
                case "3":
                    controller.logout();
                    break;
                case "4":
                    handleDeleteAccount(scanner, controller);
                    break;
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("==== Menu ====");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("3. Logout");
        System.out.println("4. Delete Account");
        System.out.println("0. Exit");
        System.out.println("==================================");
    }

    private static void handleCreateAccount(Scanner scanner, Controller controller) {
        System.out.println("=== Create Account ===");

        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();

            // Check if the username already exists
            if (!controller.isUsernameTaken(username)) {
                break; // Exit loop if username is unique
            } else {
                System.out.println("Username already exists. Please try a different username.");
            }
        }

        System.out.print("Enter role (Admin/Customer): ");
        String role = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        controller.createAccount(role, username, password, email);
    }

    private static void handleLogin(Scanner scanner, Controller controller) {
        System.out.println("=== Login ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        controller.login(username, password);
    }

    private static void handleDeleteAccount(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Account ===");

        System.out.print("Enter the ID of the account to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        controller.deleteAccount(id);
    }
}
