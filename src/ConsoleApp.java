import controller.Controller;
import controller.AccountController;
import model.Admin;
import model.Customer;
import model.User;
import repository.IRepository;
import repository.InMemoryRepository;
import service.AccountService;
import java.util.Scanner;
import presentation.StartMenu;
import presentation.LoginMenu;
import presentation.AdminMenu;
import presentation.CustomerMenu;

public class ConsoleApp {
    public static void main(String[] args) {
        IRepository<User> userRepository = new InMemoryRepository<>();
        AccountService accountService = new AccountService(userRepository);
        AccountController accountController = new AccountController(accountService);
        Controller controller = new Controller(accountController);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            if (controller.getAllUsers().isEmpty()) {
                running = StartMenu.display(scanner, controller);
            } else if (controller.getCurrentUser() == null) {
                running = LoginMenu.display(scanner, controller);
            } else if (controller.getCurrentUser() instanceof Admin) {
                running = AdminMenu.display(scanner, controller);
            } else if (controller.getCurrentUser() instanceof Customer) {
                running = CustomerMenu.display(scanner, controller);
            }
        }
        scanner.close();
    }
}
