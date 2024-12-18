package com.ticketsalescompany;

import com.ticketsalescompany.model.*;
import com.ticketsalescompany.presentation.admin.AdminMenu;
import com.ticketsalescompany.presentation.CustomerMenu;
import com.ticketsalescompany.presentation.LoginMenu;
import com.ticketsalescompany.presentation.StartMenu;
import com.ticketsalescompany.repository.IRepository;
import com.ticketsalescompany.repository.InMemoryRepository;
import com.ticketsalescompany.service.*;
import com.ticketsalescompany.controller.*;

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
        IRepository<Order> orderRepository = new InMemoryRepository<>();
        IRepository<Ticket> ticketRepository = new InMemoryRepository<>();
        IRepository<ShoppingCart> shoppingCartRepository = new InMemoryRepository<>();
        IRepository<Section> sectionRepository = new InMemoryRepository<>();
        IRepository<ShoppingCartTicket> shoppingCartTicketRepository = new InMemoryRepository<>();
        IRepository<OrderTicket> orderTicketRepository = new InMemoryRepository<>();
        IRepository<Row> rowRepository = new InMemoryRepository<>();

        // Instantiate services
        CustomerService customerService = new CustomerService();
        AccountService accountService = new AccountService(userRepository, customerService);
        SeatService seatService = new SeatService(seatRepository);
        SectionService sectionService = new SectionService(seatService, sectionRepository, seatRepository);
        VenueService venueService = new VenueService(venueRepository, sectionService);
        EventService eventService = new EventService(eventRepository, venueService);
        ArtistService artistService = new ArtistService(artistRepository, eventRepository);
        AthleteService athleteService = new AthleteService(athleteRepository, eventRepository);
        TicketService ticketService = new TicketService(ticketRepository, seatService, eventService, venueService);
        ShoppingCartTicketService shoppingCartTicketService = new ShoppingCartTicketService(shoppingCartTicketRepository, ticketService);
        ShoppingCartService shoppingCartService = new ShoppingCartService(shoppingCartRepository, shoppingCartTicketService);
        OrderTicketService orderTicketService = new OrderTicketService(orderTicketRepository, ticketService);
        OrderService orderService = new OrderService(orderRepository, shoppingCartService, shoppingCartTicketService,
                new BasicPaymentProcessor(), orderTicketService, ticketService, seatService);
        RowService rowService = new RowService(seatService, rowRepository, seatRepository);

        // Instantiate controllers
        AccountController accountController = new AccountController(accountService);
        EventController eventController = new EventController(eventService);
        VenueController venueController = new VenueController(venueService);
        SeatController seatController = new SeatController(seatService);
        SectionController sectionController = new SectionController(sectionService, sectionRepository);
        ArtistController artistController = new ArtistController(artistService);
        AthleteController athleteController = new AthleteController(athleteService);
        CustomerController customerController = new CustomerController(customerService);
        OrderController orderController = new OrderController(orderService);
        OrderTicketController orderTicketController = new OrderTicketController(orderTicketService);
        ShoppingCartController shoppingCartController = new ShoppingCartController(shoppingCartService, customerService);
        TicketController ticketController = new TicketController(ticketService);
        RowController rowController = new RowController(rowService);
        ShoppingCartTicketController shoppingCartTicketController = new ShoppingCartTicketController(shoppingCartTicketService);

        // Instantiate main Controller
        Controller controller = new Controller(
                accountController, eventController, venueController, sectionController, seatController,
                artistController, athleteController, customerController, orderController, orderTicketController,
                shoppingCartController, ticketController, rowController, shoppingCartTicketController);

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