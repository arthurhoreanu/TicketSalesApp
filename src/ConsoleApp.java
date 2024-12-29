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

        // Alegerea reprezentării datelor
        RepositoryFactory repositoryFactory = StartMenu.select(scanner);

        // Inițializarea serviciilor și controller-elor
        Controller controller = initializeController(repositoryFactory);

        boolean running = true;

        while (running) {
            if (controller.getAllUsers().isEmpty()) {
                running = MainMenu.display(scanner, controller); // Niciun utilizator în sistem
            } else if (controller.getCurrentUser() == null) {
                running = LoginMenu.display(scanner, controller); // Autentificare
            } else if (controller.getCurrentUser() instanceof Admin) {
                running = AdminMenu.display(scanner, controller); // Funcționalități Admin
            } else if (controller.getCurrentUser() instanceof Customer) {
                running = CustomerMenu.display(scanner, controller); // Funcționalități Customer
            }
        }
        scanner.close();
    }

    private static Controller initializeController(RepositoryFactory repositoryFactory) {
        // Inițializare servicii
        // Arthur's TODO
        UserService userService = new UserService(repositoryFactory.createUserRepository(), new CustomerService());
        EventService eventService = new EventService(repositoryFactory.createEventRepository(), new VenueService(repositoryFactory.createVenueRepository()));
        ArtistService artistService = new ArtistService(repositoryFactory.createArtistRepository());
        // Adaugă alte servicii similare aici

        // Inițializare controller-e
        UserController userController = new UserController(userService);
        EventController eventController = new EventController(eventService);
        ArtistController artistController = new ArtistController(artistService);
        // Adaugă alte controller-e similare aici

        // Arthur's TODO
        return new Controller(userController, eventController, artistController /*, alte controller-e */);
    }
}
