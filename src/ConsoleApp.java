import controller.*;
import model.*;
import repository.IRepository;
import repository.InMemoryRepository;
import service.*;
import java.util.Scanner;
import presentation.*;

public class ConsoleApp {
    public static void main(String[] args) {
        IRepository<User> userRepository = new InMemoryRepository<>();
        IRepository<Event> eventRepository = new InMemoryRepository<>();

        AccountService accountService = new AccountService(userRepository);
        EventService eventService = new EventService(eventRepository);

        AccountController accountController = new AccountController(accountService);
        EventController eventController = new EventController(eventService);

        Controller controller = new Controller(accountController, eventController);

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
