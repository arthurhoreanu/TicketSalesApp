import model.*;
import presentation.admin.AdminMenu;
import presentation.CustomerMenu;
import presentation.LoginMenu;
import presentation.StartMenu;
import presentation.MainMenu;
import repository.factory.RepositoryFactory;
import service.*;
import controller.*;

import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RepositoryFactory repositoryFactory = StartMenu.select(scanner);

        Controller controller = initializeController(repositoryFactory);

        boolean running = true;

        while (running) {
            if (controller.getAllUsers().isEmpty()) {
                running = MainMenu.display(scanner, controller);
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

    private static Controller initializeController(RepositoryFactory repositoryFactory) {

        // TODO aici noile Serivce și Controller, alfabetic ar fi mai nice

        // Service
        ArtistService artistService = new ArtistService(repositoryFactory);
        AthleteService athleteService = new AthleteService(repositoryFactory);
        CartService cartService = new CartService(repositoryFactory);
        CustomerService customerService = new CustomerService();
        PaymentService paymentService = new PaymentService();
        PurchaseHistoryService purchaseHistoryService = new PurchaseHistoryService(repositoryFactory);
        UserService userService = new UserService(repositoryFactory, customerService);
        EventService eventService = new EventService(repositoryFactory);

        // Controller
        ArtistController artistController = new ArtistController(artistService);
        AthleteController athleteController = new AthleteController(athleteService);
        CartController cartController = new CartController(cartService);
        CustomerController customerController = new CustomerController(customerService);
        PaymentController paymentController = new PaymentController(paymentService);
        PurchaseHistoryController purchaseHistoryController = new PurchaseHistoryController(purchaseHistoryService);
        UserController userController = new UserController(userService);
        EventController eventController = new EventController(eventService);

        // Main Controller
        return new Controller(artistController, athleteController, cartController, customerController, paymentController,
                purchaseHistoryController, userController, eventController);
    }
}