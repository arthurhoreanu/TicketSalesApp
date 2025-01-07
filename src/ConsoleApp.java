import model.*;
import presentation.admin.AdminMenu;
import presentation.*;
import repository.factory.RepositoryFactory;
import repository.*;
import service.*;
import controller.*;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RepositoryFactory repositoryFactory = StartMenu.select(scanner);
        Controller controller = initializeController(repositoryFactory);

        // Initialize ControllerProvider
        ControllerProvider.initializeController(controller);

        List<String> csvFiles = List.of(
                "src/repository/data/artists.csv", "src/repository/data/athletes.csv", "src/repository/data/seats.csv",
                "src/repository/data/rows.csv", "src/repository/data/sections.csv", "src/repository/data/venues.csv",
                "src/repository/data/tickets.csv", "src/repository/data/carts.csv", "src/repository/data/admins.csv",
                "src/repository/data/customers.csv", "src/repository/data/concerts.csv", "src/repository/data/sports_events.csv",
                "src/repository/data/concert_line_ups.csv", "src/repository/data/sports_event_line_ups.csv"
        );
        IdInitializer.initializeGlobalId(csvFiles);

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

        // Service
        ArtistService artistService = new ArtistService(repositoryFactory);
        AthleteService athleteService = new AthleteService(repositoryFactory);
        VenueService venueService = new VenueService(repositoryFactory, repositoryFactory, repositoryFactory, repositoryFactory);
        EventService eventService = new EventService(repositoryFactory, repositoryFactory, repositoryFactory, venueService,
                artistService, athleteService);
        TicketService ticketService = new TicketService(repositoryFactory, venueService);
        CartService cartService = new CartService(repositoryFactory);
        CustomerService customerService = new CustomerService();
        UserService userService = new UserService(repositoryFactory, customerService);

        // Controller
        ArtistController artistController = new ArtistController(artistService);
        AthleteController athleteController = new AthleteController(athleteService);
        VenueController venueController = new VenueController(venueService);
        TicketController ticketController = new TicketController(ticketService);
        CartController cartController = new CartController(cartService);
        CustomerController customerController = new CustomerController(customerService);
        EventController eventController = new EventController(eventService);
        UserController userController = new UserController(userService);

        // Main Controller
        return new Controller(artistController, athleteController, venueController, ticketController,
                cartController, customerController, eventController, userController);
    }

}