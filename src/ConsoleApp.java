import controller.*;
import model.*;
import presentation.menus.AdminMenu;
import presentation.menus.CustomerMenu;
import presentation.menus.LoginMenu;
import presentation.menus.StartMenu;
import repository.IRepository;
import repository.InMemoryRepository;
import service.*;
import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        IRepository<User> userRepository = new InMemoryRepository<>();
        IRepository<Event> eventRepository = new InMemoryRepository<>();
        IRepository<Venue> venueRepository = new InMemoryRepository<>();
        IRepository<Seat> seatRepository = new InMemoryRepository<>();

        AccountService accountService = new AccountService(userRepository);
        SeatService seatService = new SeatService(seatRepository);
        SectionService sectionService = new SectionService(seatService);
        VenueService venueService = new VenueService(venueRepository, sectionService);
        EventService eventService = new EventService(eventRepository, venueService);

        AccountController accountController = new AccountController(accountService);
        EventController eventController = new EventController(eventService);
        VenueController venueController = new VenueController(venueService);
        SeatController seatController = new SeatController(seatService);
        SectionController sectionController = new SectionController(sectionService);

        Controller controller = new Controller(accountController, eventController, venueController, sectionController, seatController);

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
