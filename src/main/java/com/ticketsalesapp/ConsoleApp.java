package main.java.com.ticketsalesapp;

import main.java.com.ticketsalesapp.model.*;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.view.admin.AdminMenu;
import main.java.com.ticketsalesapp.view.*;
import main.java.com.ticketsalesapp.repository.factory.RepositoryFactory;
import main.java.com.ticketsalesapp.repository.*;
import main.java.com.ticketsalesapp.service.*;
import main.java.com.ticketsalesapp.controller.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleApp {
    public static void run() {
        Scanner scanner = new Scanner(System.in);

        RepositoryFactory repositoryFactory = StartMenu.select(scanner);
        ApplicationController applicationController = initializeController(repositoryFactory);

        // Initialize ControllerProvider
        ControllerProvider.initializeController(applicationController);

        List<String> csvFiles = List.of(
                "src/main/java/com/ticketsalesapp/repository/data/artists.csv", "src/main/java/com/ticketsalesapp/repository/data/athletes.csv", "src/main/java/com/ticketsalesapp/repository/data/seats.csv",
                "src/main/java/com/ticketsalesapp/repository/data/rows.csv", "src/main/java/com/ticketsalesapp/repository/data/sections.csv", "src/main/java/com/ticketsalesapp/repository/data/venues.csv",
                "src/main/java/com/ticketsalesapp/repository/data/tickets.csv", "src/main/java/com/ticketsalesapp/repository/data/carts.csv", "src/main/java/com/ticketsalesapp/repository/data/admins.csv",
                "src/main/java/com/ticketsalesapp/repository/data/customers.csv", "src/main/java/com/ticketsalesapp/repository/data/concerts.csv", "src/main/java/com/ticketsalesapp/repository/data/sports_events.csv",
                "src/main/java/com/ticketsalesapp/repository/data/concert_line_ups.csv", "src/main/java/com/ticketsalesapp/repository/data/sports_event_line_ups.csv"
        );
        IdInitializer.initializeGlobalId(csvFiles);

        boolean running = true;

        while (running) {
            if (applicationController.getAllUsers().isEmpty()) {
                running = MainMenu.display(scanner, applicationController);
            } else if (applicationController.getCurrentUser() == null) {
                running = LoginMenu.display(scanner, applicationController);
            } else if (applicationController.getCurrentUser() instanceof Admin) {
                running = AdminMenu.display(scanner, applicationController);
            } else if (applicationController.getCurrentUser() instanceof Customer) {
                running = CustomerMenu.display(scanner, applicationController);
            }
        }
        scanner.close();
    }

    private static ApplicationController initializeController(RepositoryFactory repositoryFactory) {

        // Service
        ArtistService artistService = new ArtistService(repositoryFactory);
        AthleteService athleteService = new AthleteService(repositoryFactory);
        VenueService venueService = new VenueService(repositoryFactory, repositoryFactory, repositoryFactory, repositoryFactory);
        ConcertService concertService = new ConcertService(repositoryFactory, repositoryFactory, repositoryFactory, venueService,
                artistService, athleteService);
        TicketService ticketService = new TicketService(repositoryFactory, venueService);
        CartService cartService = new CartService(repositoryFactory);
        CustomerService customerService = new CustomerService();
        AdminService adminService = new AdminService(repositoryFactory, customerService);

        // Controller
        ArtistController artistController = new ArtistController(artistService);
        AthleteController athleteController = new AthleteController(athleteService);
        VenueController venueController = new VenueController(venueService);
        TicketController ticketController = new TicketController(ticketService);
        CartController cartController = new CartController(cartService);
        CustomerController customerController = new CustomerController(customerService);
        ConcertController concertController = new ConcertController(concertService);
        AdminController adminController = new AdminController(adminService);

        // Main Controller
        return new ApplicationController(artistController, athleteController, venueController, ticketController,
                cartController, customerController, concertController, adminController);
    }
}