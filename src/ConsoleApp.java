import controller.*;
import model.*;
import presentation.admin.AdminMenu;
import presentation.CustomerMenu;
import presentation.LoginMenu;
import presentation.StartMenu;
import repository.IRepository;
import repository.InMemoryRepository;
import service.*;

import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {

        // Instantiate repositories
        IRepository<User> userRepository = new InMemoryRepository<>();
        IRepository<Event> eventRepository = new InMemoryRepository<>();
        IRepository<Venue> venueRepository = new InMemoryRepository<>();
        IRepository<Seat> seatRepository = new InMemoryRepository<>();
        IRepository<Artist> artistRepository = new InMemoryRepository<>();
        IRepository<Athlete> athleteRepository = new InMemoryRepository<>();
        IRepository<Ticket> ticketRepository = new InMemoryRepository<>();
        IRepository<Cart> cartRepository = new InMemoryRepository<>();
        IRepository<Section> sectionRepository = new InMemoryRepository<>();
        IRepository<Row> rowRepository = new InMemoryRepository<>();

        // Instantiate services
        CustomerService customerService = new CustomerService();
        UserService userService = new UserService(userRepository, customerService);
        SeatService seatService = new SeatService(seatRepository);
        SectionService sectionService = new SectionService(seatService, sectionRepository, seatRepository);
        VenueService venueService = new VenueService(venueRepository, sectionService);
        EventService eventService = new EventService(eventRepository, venueService);
        ArtistService artistService = new ArtistService(artistRepository, eventRepository);
        AthleteService athleteService = new AthleteService(athleteRepository, eventRepository);
        TicketService ticketService = new TicketService(ticketRepository, seatService, eventService, venueService);
        CartService cartService = new CartService(cartRepository);
        RowService rowService = new RowService(seatService, rowRepository, seatRepository);
        PaymentService paymentService = new PaymentService();

        // Instantiate controllers
        UserController userController = new UserController(userService);
        EventController eventController = new EventController(eventService);
        VenueController venueController = new VenueController(venueService);
        SeatController seatController = new SeatController(seatService);
        SectionController sectionController = new SectionController(sectionService, sectionRepository);
        ArtistController artistController = new ArtistController(artistService);
        AthleteController athleteController = new AthleteController(athleteService);
        CustomerController customerController = new CustomerController(customerService);
        CartController cartController = new CartController(cartService);
        TicketController ticketController = new TicketController(ticketService);
        RowController rowController = new RowController(rowService);
        PaymentController paymentController = new PaymentController(paymentService);

        // Instantiate main Controller
        Controller controller = new Controller(
                userController, eventController, venueController, sectionController, seatController,
                artistController, athleteController, customerController, cartController, ticketController,
                rowController, paymentController);

        ControllerProvider.setController(controller);

        // Set up scanner and application loop
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