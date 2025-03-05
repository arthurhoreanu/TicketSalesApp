package test;

import main.java.com.ticketsalesapp.controller.*;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.model.*;
import main.java.com.ticketsalesapp.model.event.*;
import main.java.com.ticketsalesapp.model.ticket.Cart;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.user.User;
import main.java.com.ticketsalesapp.model.venue.Row;
import main.java.com.ticketsalesapp.model.venue.Seat;
import main.java.com.ticketsalesapp.model.venue.Section;
import main.java.com.ticketsalesapp.model.venue.Venue;
import org.junit.jupiter.api.*;
import main.java.com.ticketsalesapp.repository.factory.*;
import main.java.com.ticketsalesapp.service.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {

    // Service
    private static ArtistService artistService;
    private static AthleteService athleteService;
    private static VenueService venueService;
    private static EventService eventService;
    private static TicketService ticketService;
    private static CartService cartService;
    private static CustomerService customerService;
    private static AdminService adminService;

    // Controller
    private static ArtistController artistController;
    private static AthleteController athleteController;
    private static VenueController venueController;
    private static EventController eventController;
    private static TicketController ticketController;
    private static CartController cartController;
    private static CustomerController customerController;
    private static UserController userController;
    private static Controller controller;

    /**
     * Sets up dependencies before each test.
     * Initializes services and controllers using {@link InMemoryRepositoryFactory}.
     */
    @BeforeEach
    public void setUp() {
        InMemoryRepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();

        // Service
        artistService = new ArtistService(repositoryFactory);
        athleteService = new AthleteService(repositoryFactory);
        venueService = new VenueService(repositoryFactory, repositoryFactory, repositoryFactory, repositoryFactory);
        eventService = new EventService(repositoryFactory, repositoryFactory, repositoryFactory, venueService,
                artistService, athleteService);
        ticketService = new TicketService(repositoryFactory, venueService);
        cartService = new CartService(repositoryFactory);
        customerService = new CustomerService();
        adminService = new AdminService(repositoryFactory, customerService);

        // Controller
        artistController = new ArtistController(artistService);
        athleteController = new AthleteController(athleteService);
        venueController = new VenueController(venueService);
        eventController = new EventController(eventService);
        ticketController = new TicketController(ticketService);
        cartController = new CartController(cartService);
        customerController = new CustomerController(customerService);
        userController = new UserController(adminService);
        Controller mockController = new Controller(artistController, athleteController, venueController, ticketController,
                cartController, customerController, eventController, userController);
        ControllerProvider.initializeController(mockController);
    }

    /**
     * Tests CRUD operations for user accounts.
     */
    @Order(1)
    @DisplayName("Simulate Full CRUD Operations for Admins and Customers")
    @Test
    public void userCRUD() {
        // 1. CREATE
        adminService.createAccount("Admin", "admin", "admin@tsc.com", "password");
        adminService.createAccount("Customer", "customer", "customer@example.com", "password");
        assertEquals(2, adminService.getAllUsers().size(), "Two users should be created.");

        // 2. READ
        User retrievedAdmin = adminService.findUserByID(1);
        User retrievedCustomer = adminService.findUserByID(2);

        assertNotNull(retrievedAdmin, "Admin should be retrievable.");
        assertNotNull(retrievedCustomer, "Customer should be retrievable.");
        assertEquals("admin", retrievedAdmin.getUsername(), "Retrieved Admin username should match.");
        assertEquals("customer", retrievedCustomer.getUsername(), "Retrieved Customer username should match.");

        // 3. DELETE
        adminService.login("admin", "password"); // Admin trebuie să fie logat pentru a șterge
        adminService.deleteAccount(2); // Șterge Customer
        adminService.logout();

        assertNull(adminService.findUserByID(2), "Customer should be deleted.");
        assertEquals(1, adminService.getAllUsers().size(), "Only Admin should remain.");
    }

    /**
     * Tests CRUD operations for artists and athletes.
     */
    @Order(2)
    @DisplayName("Simulate Full CRUD Operations for Artists and Athletes")
    @Test
    public void artistAthleteCRUD() {
        // 1. CREATE
        artistService.createArtist("Taylor Swift", "Pop");
        artistService.createArtist("Muse", "Rock");
        athleteService.createAthlete("Michael Jordan", "Basketball");
        athleteService.createAthlete("Serena Williams", "Tennis");

        assertEquals(2, artistService.getAllArtists().size(), "Two artists should be created.");
        assertEquals(2, athleteService.getAllAthletes().size(), "Two athletes should be created.");

        // 2. READ
        Artist retrievedArtist = artistService.findArtistByID(1);
        Athlete retrievedAthlete = athleteService.findAthleteByID(1);

        assertNotNull(retrievedArtist, "Artist should be retrievable.");
        assertNotNull(retrievedAthlete, "Athlete should be retrievable.");
        assertEquals("Taylor Swift", retrievedArtist.getArtistName(), "Retrieved Artist name should match.");
        assertEquals("Michael Jordan", retrievedAthlete.getAthleteName(), "Retrieved Athlete name should match.");

        // 3. UPDATE
        artistService.updateArtist(1, "Mitski", "Indie");
        athleteService.updateAthlete(1, "Michael Jordan Jr.", "Basketball");

        Artist updatedArtist = artistService.findArtistByID(1);
        Athlete updatedAthlete = athleteService.findAthleteByID(1);

        assertNotNull(updatedArtist, "Updated Artist should still exist.");
        assertNotNull(updatedAthlete, "Updated Athlete should still exist.");
        assertEquals("Mitski", updatedArtist.getArtistName(), "Updated Artist name should match.");
        assertEquals("Michael Jordan Jr.", updatedAthlete.getAthleteName(), "Updated Athlete name should match.");

        // 4. DELETE
        artistService.deleteArtist(2);
        athleteService.deleteAthlete(2);

        assertNull(artistService.findArtistByID(2), "Artist Jane Smith should be deleted.");
        assertNull(athleteService.findAthleteByID(4), "Athlete Serena Williams should be deleted.");
        assertEquals(1, artistService.getAllArtists().size(), "Only one artist should remain.");
        assertEquals(1, athleteService.getAllAthletes().size(), "Only one athlete should remain.");
    }

    /**
     * Tests CRUD operations for venues.
     */
    @Order(3)
    @DisplayName("Simulate Full CRUD Operations for Venue")
    @Test
    public void venueCRUD() {
        // 1. CREATE
        Venue venue = venueService.createVenue("Stadium A", "New York", 50000, true);
        Venue venueWithoutSeats = venueService.createVenue("Stadium B", "Los Angeles", 30000, false);
        assertEquals(2, venueService.getAllVenues().size(), "Two venues should be created.");

        // 2. READ
        Venue retrievedVenue = venueService.findVenueByID(venue.getID());
        Venue retrievedVenueWithoutSeats = venueService.findVenueByID(venueWithoutSeats.getID());
        assertNotNull(retrievedVenue, "Venue A should be retrievable.");
        assertNotNull(retrievedVenueWithoutSeats, "Venue B should be retrievable.");
        assertEquals("Stadium A", retrievedVenue.getVenueName(), "Retrieved Venue A name should match.");
        assertEquals("Stadium B", retrievedVenueWithoutSeats.getVenueName(), "Retrieved Venue B name should match.");

        // 3. UPDATE
        venueService.updateVenue(venue.getID(), "Stadium A Updated", "New York City", 60000, true);
        Venue updatedVenue = venueService.findVenueByID(venue.getID());
        assertNotNull(updatedVenue, "Updated Venue A should still exist.");
        assertEquals("Stadium A Updated", updatedVenue.getVenueName(), "Updated Venue A name should match.");
        assertEquals(60000, updatedVenue.getVenueCapacity(), "Updated Venue A capacity should match.");

        // 4. ADD SECTION
        venueService.addSectionToVenue(venue.getID(), 2, 10000, "Section");
        List<Section> sections = venueService.getSectionsByVenueID(venue.getID());
        assertEquals(2, sections.size(), "Two sections should be added to Venue A.");
        assertEquals("Section 1", sections.get(0).getSectionName(), "First section name should match.");
        assertEquals("Section 2", sections.get(1).getSectionName(), "Second section name should match.");

        // 5. ADD ROW AND SEATS TO SECTION
        Section section = sections.get(0);
        venueService.addRowsToSection(section.getID(), 2, 50);
        List<Row> rows = venueService.findRowsBySection(section.getID());
        assertEquals(2, rows.size(), "Two rows should be added to the first section.");
        venueService.addSeatsToRow(rows.get(0).getID(), 10);
        List<Seat> seats = venueService.getSeatsByRow(rows.get(0).getID());
        assertEquals(10, seats.size(), "Ten seats should be added to the first row.");

        // 6. DELETE ROW, SECTION, AND VENUE
        venueService.deleteRow(rows.get(0).getID());
        assertNull(venueService.findRowByID(rows.get(0).getID()), "Row should be deleted.");
        venueService.deleteSection(section.getID());
        assertNull(venueService.findSectionByID(section.getID()), "Section should be deleted.");
        venueService.deleteVenue(venue.getID());
        assertNull(venueService.findVenueByID(venue.getID()), "Venue should be deleted.");

        assertEquals(1, venueService.getAllVenues().size(), "Only one venue should remain after deletion.");
    }

    /**
     * Tests CRUD operations for events.
     */
    @Order(4)
    @DisplayName("Simulate Full CRUD Operations for Events")
    @Test
    public void eventCRUD() {
        // 1. CREATE VENUE
        Venue venue = venueService.createVenue("Stadium A", "New York", 50000, true);
        assertNotNull(venue, "Venue should be created.");

        // 2. CREATE EVENTS
        LocalDateTime startDateTimeConcert = LocalDateTime.of(2024, 5, 15, 20, 0);
        LocalDateTime endDateTimeConcert = LocalDateTime.of(2024, 5, 15, 23, 0);
        Concert concert = eventService.createConcert("Rock Night", "An amazing rock concert",
                startDateTimeConcert, endDateTimeConcert, venue.getID(), EventStatus.SCHEDULED);

        LocalDateTime startDateTimeSports = LocalDateTime.of(2024, 6, 20, 18, 0);
        LocalDateTime endDateTimeSports = LocalDateTime.of(2024, 6, 20, 21, 0);
        SportsEvent sportsEvent = eventService.createSportsEvent("Basketball Finals", "The championship game",
                startDateTimeSports, endDateTimeSports, venue.getID(), EventStatus.SCHEDULED);

        assertNotNull(concert, "Concert should be created.");
        assertNotNull(sportsEvent, "Sports event should be created.");
        assertEquals(2, eventService.getAllEvents().size(), "Two events should be created.");

        // 3. READ
        Concert retrievedConcert = eventService.findConcertByID(concert.getID());
        SportsEvent retrievedSportsEvent = eventService.findSportsEventByID(sportsEvent.getID());
        assertNotNull(retrievedConcert, "Concert should be retrievable.");
        assertNotNull(retrievedSportsEvent, "Sports Event should be retrievable.");
        assertEquals("Rock Night", retrievedConcert.getEventName(), "Concert name should match.");
        assertEquals("Basketball Finals", retrievedSportsEvent.getEventName(), "Sports Event name should match.");

        // 4. UPDATE
        eventService.updateEvent(concert.getID(), "Rock Night Updated", "A rock concert to remember",
                startDateTimeConcert.plusDays(1), endDateTimeConcert.plusDays(1), EventStatus.CANCELLED);

        Concert updatedConcert = eventService.findConcertByID(concert.getID());
        assertNotNull(updatedConcert, "Updated concert should still exist.");
        assertEquals("Rock Night Updated", updatedConcert.getEventName(), "Updated concert name should match.");
        assertEquals(EventStatus.CANCELLED, updatedConcert.getEventStatus(), "Updated concert status should match.");

        // 5. ADD ARTISTS TO CONCERT
        assertTrue(artistService.createArtist("The Rolling Stones", "Rock"), "Artist should be created.");
        Artist artist = artistService.findArtistByID(1);
        assertNotNull(artist, "Artist should be retrievable.");
        assertTrue(eventService.addArtistToConcert(concert.getID(), artist.getID()), "Artist should be added to concert.");
        List<Artist> concertArtists = eventService.getArtistsByConcert(concert.getID());
        assertEquals(1, concertArtists.size(), "Concert should have one artist.");
        assertEquals("The Rolling Stones", concertArtists.get(0).getArtistName(), "Artist name should match.");

        // 6. ADD ATHLETES TO SPORTS EVENT
        assertTrue(athleteService.createAthlete("LeBron James", "Basketball"), "Athlete should be created.");
        Athlete athlete = athleteService.findAthleteByID(1);
        assertNotNull(athlete, "Athlete should be retrievable.");
        assertTrue(eventService.addAthleteToSportsEvent(sportsEvent.getID(), athlete.getID()), "Athlete should be added to sports event.");
        List<Athlete> sportsEventAthletes = eventService.getAthletesBySportsEvent(sportsEvent.getID());
        assertEquals(1, sportsEventAthletes.size(), "Sports event should have one athlete.");
        assertEquals("LeBron James", sportsEventAthletes.get(0).getAthleteName(), "Athlete name should match.");

        // 7. DELETE
        assertTrue(eventService.deleteEvent(concert.getID()), "Concert should be deleted.");
        assertTrue(eventService.deleteEvent(sportsEvent.getID()), "Sports event should be deleted.");
        assertEquals(0, eventService.getAllEvents().size(), "No events should remain after deletion.");
    }

    /**
     * Tests CRUD operations for tickets.
     */
    @Order(5)
    @DisplayName("Simulate Full CRUD Operations for Tickets")
    @Test
    public void ticketCRUD() {
        // 1. CREATE VENUES
        Venue venueWithSeats = venueService.createVenue("Concert Hall", "Boston", 2000, true);
        Venue venueWithoutSeats = venueService.createVenue("Open Field", "Miami", 5000, false);
        assertNotNull(venueWithSeats, "Venue with seats should be created.");
        assertNotNull(venueWithoutSeats, "Venue without seats should be created.");

        // 2. ADD SECTIONS, ROWS, AND SEATS TO VENUE WITH SEATS
        venueService.addSectionToVenue(venueWithSeats.getID(), 1, 1000, "Main Section");
        Section section = venueService.getSectionsByVenueID(venueWithSeats.getID()).get(0);
        venueService.addRowsToSection(section.getID(), 2, 50);
        List<Row> rows = venueService.findRowsBySection(section.getID());
        venueService.addSeatsToRow(rows.get(0).getID(), 50);
        venueService.addSeatsToRow(rows.get(1).getID(), 50);
        assertEquals(50, venueService.getSeatsByRow(rows.get(0).getID()).size(), "50 seats should be added to the first row.");

        // 3. CREATE EVENTS
        LocalDateTime startDateTimeWithSeats = LocalDateTime.of(2024, 8, 10, 20, 0);
        LocalDateTime endDateTimeWithSeats = LocalDateTime.of(2024, 8, 10, 23, 0);
        Concert concertWithSeats = eventService.createConcert("Orchestra Night", "A night of symphonies",
                startDateTimeWithSeats, endDateTimeWithSeats, venueWithSeats.getID(), EventStatus.SCHEDULED);

        LocalDateTime startDateTimeWithoutSeats = LocalDateTime.of(2024, 9, 5, 18, 0);
        LocalDateTime endDateTimeWithoutSeats = LocalDateTime.of(2024, 9, 5, 21, 0);
        Concert concertWithoutSeats = eventService.createConcert("Open Air Festival", "An amazing music festival",
                startDateTimeWithoutSeats, endDateTimeWithoutSeats, venueWithoutSeats.getID(), EventStatus.SCHEDULED);

        assertNotNull(concertWithSeats, "Concert with seats should be created.");
        assertNotNull(concertWithoutSeats, "Concert without seats should be created.");

        // 4. GENERATE TICKETS
        double basePrice = 100.0;
        List<Ticket> ticketsWithSeats = ticketService.generateTicketsForEvent(concertWithSeats, basePrice, 10, 5, 10);
        List<Ticket> ticketsWithoutSeats = ticketService.generateTicketsForEvent(concertWithoutSeats, basePrice, 20, 10, 30);
        assertEquals(25, ticketsWithSeats.size(), "25 tickets should be generated for venue with seats.");
        assertEquals(60, ticketsWithoutSeats.size(), "60 tickets should be generated for venue without seats.");

        // 5. READ TICKETS
        List<Ticket> availableTicketsWithSeats = ticketService.getAvailableTicketsForEvent(concertWithSeats);
        List<Ticket> availableTicketsWithoutSeats = ticketService.getAvailableTicketsForEvent(concertWithoutSeats);
        assertEquals(25, availableTicketsWithSeats.size(), "All tickets for venue with seats should be available.");
        assertEquals(60, availableTicketsWithoutSeats.size(), "All tickets for venue without seats should be available.");

        // 6. RESERVE TICKET
        Customer customer = new Customer(1, "jane_doe", "jane@example.com", "password456");
        Ticket ticketToReserve = availableTicketsWithSeats.get(0);
        ticketService.reserveTicket(ticketToReserve, customer);
        Ticket reservedTicket = ticketService.findTicketByID(ticketToReserve.getID());
        assertTrue(reservedTicket.isSold(), "Reserved ticket should be marked as sold.");
        assertEquals(customer, reservedTicket.getCustomer(), "Customer should match the reserved ticket.");

        // 7. RELEASE TICKET
        ticketService.releaseTicket(reservedTicket);
        Ticket releasedTicket = ticketService.findTicketByID(ticketToReserve.getID());
        assertFalse(releasedTicket.isSold(), "Released ticket should not be marked as sold.");
        assertNull(releasedTicket.getCustomer(), "Released ticket should not have a customer.");

        // 8. DELETE TICKET
        ticketService.deleteTicket(reservedTicket.getID());
        Ticket deletedTicket = ticketService.findTicketByID(reservedTicket.getID());
        assertNull(deletedTicket, "Deleted ticket should not exist.");

        // 9. CHECK AVAILABILITY
        List<String> ticketAvailability = ticketService.getTicketAvailabilityByType(concertWithSeats);
        assertTrue(ticketAvailability.contains("9 early bird tickets available"), "Early bird ticket availability should match.");
        assertTrue(ticketAvailability.contains("5 VIP tickets available"), "VIP ticket availability should match.");
        assertTrue(ticketAvailability.contains("10 standard tickets available"), "Standard ticket availability should match.");
    }

    /**
     * Tests CRUD operations for shopping carts.
     */
    @Order(6)
    @DisplayName("Simulate Full CRUD Operations for Cart")
    @Test
    public void cartCRUD() {
        // 1. CREATE CUSTOMER, VENUES, EVENTS, AND CART
        Customer customer = new Customer(1, "john_doe", "john@example.com", "password123");

        // Venue with seats
        Venue venueWithSeats = venueService.createVenue("Concert Hall", "Boston", 2000, true);
        venueService.addSectionToVenue(venueWithSeats.getID(), 1, 1000, "Main Section");
        Section section = venueService.getSectionsByVenueID(venueWithSeats.getID()).get(0);
        venueService.addRowsToSection(section.getID(), 2, 50); // Two rows with 50 seats each
        venueService.addSeatsToRow(section.getRows().get(0).getID(), 50);
        venueService.addSeatsToRow(section.getRows().get(1).getID(), 50);

        // Venue without seats
        Venue venueWithoutSeats = venueService.createVenue("Open Field", "Miami", 5000, false);

        assertNotNull(venueWithSeats, "Venue with seats should be created.");
        assertNotNull(venueWithoutSeats, "Venue without seats should be created.");

        // Create events
        LocalDateTime startDateTimeWithSeats = LocalDateTime.of(2024, 8, 10, 20, 0);
        LocalDateTime endDateTimeWithSeats = LocalDateTime.of(2024, 8, 10, 23, 0);
        Concert concertWithSeats = eventService.createConcert("Classical Night", "A night of classical music",
                startDateTimeWithSeats, endDateTimeWithSeats, venueWithSeats.getID(), EventStatus.SCHEDULED);

        LocalDateTime startDateTimeWithoutSeats = LocalDateTime.of(2024, 9, 5, 18, 0);
        LocalDateTime endDateTimeWithoutSeats = LocalDateTime.of(2024, 9, 5, 21, 0);
        Concert concertWithoutSeats = eventService.createConcert("Open Air Festival", "An amazing music festival",
                startDateTimeWithoutSeats, endDateTimeWithoutSeats, venueWithoutSeats.getID(), EventStatus.SCHEDULED);

        assertNotNull(concertWithSeats, "Concert with seats should be created.");
        assertNotNull(concertWithoutSeats, "Concert without seats should be created.");

        // Create cart
        Cart cart = cartService.createCart(customer, concertWithSeats);
        assertNotNull(cart, "Cart should be created.");

        // 2. GENERATE AND ADD TICKETS
        double basePrice = 100.0;
        List<Ticket> ticketsWithSeats = ticketService.generateTicketsForEvent(concertWithSeats, basePrice, 5, 3, 7);
        assertEquals(15, ticketsWithSeats.size(), "15 tickets should be generated for venue with seats.");

        Ticket ticketToAdd = ticketsWithSeats.get(0);
        boolean isAdded = cartService.addTicketToCart(cart, ticketToAdd);
        assertTrue(isAdded, "Ticket should be added to the cart.");

        List<Ticket> cartTickets = cartService.getTicketsInCart(cart);
        assertEquals(1, cartTickets.size(), "Cart should contain one ticket.");
        assertEquals(ticketToAdd, cartTickets.get(0), "Added ticket should match.");

        // 3. REMOVE TICKET FROM CART
        boolean isRemoved = cartService.removeTicketFromCart(cart, ticketToAdd);
        assertTrue(isRemoved, "Ticket should be removed from the cart.");

        cartTickets = cartService.getTicketsInCart(cart);
        assertEquals(0, cartTickets.size(), "Cart should be empty after ticket removal.");

        // 4. PROCESS PAYMENT
        Ticket ticketToBuy = ticketsWithSeats.get(1);
        cartService.addTicketToCart(cart, ticketToBuy);
        List<Ticket> ticketsToBuy = cartService.getTicketsInCart(cart);
        assertEquals(1, ticketsToBuy.size(), "Cart should contain one ticket for purchase.");

        cartService.processPayment(cart, "1234567812345678", "John Doe", 12, 2025, "123");
        assertTrue(cart.isPaymentProcessed(), "Cart payment should be processed.");

        Ticket purchasedTicket = ticketService.findTicketByID(ticketToBuy.getID());
        assertTrue(purchasedTicket.isSold(), "Ticket should be marked as sold.");
        assertEquals(customer, purchasedTicket.getCustomer(), "Customer should match the purchased ticket.");

        // 5. CLEAR CART (AFTER PAYMENT NOT ALLOWED)
        cartService.clearCart(cart);
        assertEquals(0.0, cart.getTotalPrice(), "Total price should be zero.");

    }

    /**
     * Tests the event suggestion feature for customers with favorites.
     */
    @Order(7)
    @DisplayName("Complex Method: Event Suggestions (Success)")
    @Test
    public void SuccessfulEventSuggestion() {
        // 1. CREATE CUSTOMER AND SET AS CURRENT
        adminService.createAccount("Customer", "customer", "customer@gmail.com", "password");
        User customer = adminService.findUserByID(1);
        customerService.setCurrentCustomer((Customer) customer);

        // 2. CREATE ARTISTS, ATHLETES, AND RELATED ENTITIES
        artistService.createArtist("Coldplay", "Pop");
        artistService.createArtist("Adele", "Pop");
        athleteService.createAthlete("LeBron James", "Basketball");
        athleteService.createAthlete("Stephen Curry", "Basketball");

        Artist artist = artistService.findArtistByID(1);
        Artist secondArtist = artistService.findArtistByID(2);
        Athlete athlete = athleteService.findAthleteByID(1);
        Athlete secondAthlete = athleteService.findAthleteByID(2);

        // 3. ADD FAVORITES TO CUSTOMER
        customerService.addFavourite(artist);
        customerService.addFavourite(athlete);

        // 4. CREATE VENUES AND EVENTS
        Venue venue1 = venueService.createVenue("Arena 1", "New York", 3000, true);
        Venue venue2 = venueService.createVenue("Stadium A", "Los Angeles", 5000, false);
        venueService.addSectionToVenue(venue1.getID(), 1, 1000, "Main Section");
        Section section = venueService.getSectionsByVenueID(venue1.getID()).get(0);
        venueService.addRowsToSection(section.getID(), 2, 50);
        venueService.addSeatsToRow(section.getRows().get(0).getID(), 50);

        LocalDateTime date1 = LocalDateTime.now().plusDays(10);
        LocalDateTime date2 = LocalDateTime.now().plusDays(20);
        LocalDateTime date3 = LocalDateTime.now().plusDays(30);
        LocalDateTime date4 = LocalDateTime.now().plusDays(40);

        Concert concert1 = eventService.createConcert("Coldplay Live", "Pop concert", date1, date2, venue1.getID(), EventStatus.SCHEDULED);
        eventService.addArtistToConcert(concert1.getID(), artist.getID());
        Concert concert2 = eventService.createConcert("Adele's Show", "Exclusive concert", date3, date4, venue2.getID(), EventStatus.SCHEDULED);
        eventService.addArtistToConcert(concert2.getID(), secondArtist.getID());

        SportsEvent sportsEvent1 = eventService.createSportsEvent("Basketball Finals", "Exciting game", date1, date2, venue1.getID(), EventStatus.SCHEDULED);
        eventService.addAthleteToSportsEvent(sportsEvent1.getID(), athlete.getID());
        SportsEvent sportsEvent2 = eventService.createSportsEvent("Soccer Match", "Intense competition", date3, date4, venue2.getID(), EventStatus.SCHEDULED);
        eventService.addAthleteToSportsEvent(sportsEvent2.getID(), secondArtist.getID());

        // 5. SUGGEST EVENTS BASED ON FAVORITES

        // Add events for favorite artists
        List<Event> suggestedEvents = new ArrayList<>(eventService.getUpcomingEventsForArtist(artist.getID()));

        // Add related events for same genre
        List<Artist> relatedArtists = artistService.findArtistsByGenre(artist.getGenre());
        for (Artist relatedArtist : relatedArtists) {
            if (!relatedArtist.equals(artist)) {
                suggestedEvents.addAll(eventService.getUpcomingEventsForArtist(relatedArtist.getID()));
            }
        }

        // Add events for favorite athletes
        suggestedEvents.addAll(eventService.getUpcomingEventsForAthlete(athlete.getID()));

        // Add related events for same sport
        List<Athlete> relatedAthletes = athleteService.findAthletesBySport(athlete.getAthleteSport());
        for (Athlete relatedAthlete : relatedAthletes) {
            if (!relatedAthlete.equals(athlete)) {
                suggestedEvents.addAll(eventService.getUpcomingEventsForAthlete(relatedAthlete.getID()));
            }
        }

        // Ensure suggestions are sorted by date
        suggestedEvents.sort(Comparator.comparing(Event::getStartDateTime));

        assertFalse(suggestedEvents.isEmpty(), "Suggested events should not be empty.");
        assertTrue(suggestedEvents.contains(concert1), "Coldplay Live should be in suggested events.");
        assertTrue(suggestedEvents.contains(concert2), "Adele's Show should be in suggested events.");
        assertTrue(suggestedEvents.contains(sportsEvent1), "Basketball Finals should be in suggested events.");
        assertTrue(suggestedEvents.contains(sportsEvent2), "Soccer Match should be in suggested events.");
    }

    /**
     * Tests the event suggestion feature for customers without favorites.
     * Expects a {@link BusinessLogicException}.
     */
    @Order(8)
    @DisplayName("Complex Method: Event Suggestions (Fail with Exception)")
    @Test
    public void failEventSuggestion() {
        // 1. CREATE CUSTOMER WITH NO FAVORITES
        adminService.createAccount("Customer", "customer", "customer@gmail.com", "password");
        User customer = adminService.findUserByID(1);
        customerService.setCurrentCustomer((Customer) customer);

        // 2. VERIFY FAVORITES IS EMPTY
        Set<FavouriteEntity> favourites = customerService.getFavourites();
        assertTrue(favourites.isEmpty(), "Favourites should be empty.");

        // 3. ATTEMPT TO SUGGEST EVENTS AND EXPECT EXCEPTION
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
            List<Event> suggestedEvents = new ArrayList<>();
            for (FavouriteEntity favourite : favourites) {
                if (favourite instanceof Artist artist) {
                    suggestedEvents.addAll(eventService.getUpcomingEventsForArtist(artist.getID()));
                } else if (favourite instanceof Athlete athlete) {
                    suggestedEvents.addAll(eventService.getUpcomingEventsForAthlete(athlete.getID()));
                }
            }

            if (suggestedEvents.isEmpty()) {
                // Manually throw exception if no favourites exist
                throw new BusinessLogicException("Cannot generate suggestions: no favourites available.");
            }
        });

        // 4. ASSERT THE EXCEPTION MESSAGE
        assertEquals("Cannot generate suggestions: no favourites available.", exception.getMessage());
    }

    /**
     * Tests the recommended seat selection feature for available seats.
     */
    @Order(9)
    @DisplayName("Complex Method: Recommended Seat (Success with Exception)")
    @Test
    public void testRecommendClosestSeatSuccess() {
        // 1. Create Venue, Section, Row, and Seats
        Venue venue = venueService.createVenue("Concert Hall", "New York", 1000, true);
        venueService.addSectionToVenue(venue.getID(), 1, 1000, "Main Section");
        Section section = venueService.getSectionsByVenueID(venue.getID()).get(0);
        venueService.addRowsToSection(section.getID(), 1, 50); // Add one row with 50 seats
        Row row = venueService.findRowsBySection(section.getID()).get(0);
        venueService.addSeatsToRow(row.getID(), 10); // Add 10 seats to the row

        // 2. Get the seats in the row
        List<Seat> seats = venueService.getSeatsByRow(row.getID());

        // 3. Reserve some seats
        seats.get(3).setReserved(true); // Reserve seat 4
        seats.get(5).setReserved(true); // Reserve seat 6

        // Case 1: Closest seat to a single selected seat
        List<Integer> selectedSeatNumbers = List.of(4); // Select seat 4
        Seat recommendedSeat = venueService.recommendClosestSeat(section.getID(), row.getID(), selectedSeatNumbers);
        assertNotNull(recommendedSeat, "Recommended seat should not be null.");
        assertEquals(3, recommendedSeat.getNumber(), "Closest seat to 4 should be seat 3.");

        // Case 2: Closest seat to multiple selected seats
        selectedSeatNumbers = List.of(4, 6); // Select seat 4 and 6
        recommendedSeat = venueService.recommendClosestSeat(section.getID(), row.getID(), selectedSeatNumbers);
        assertNotNull(recommendedSeat, "Recommended seat should not be null.");
        assertEquals(3, recommendedSeat.getNumber(), "Closest seat to 4 and 6 should be seat 3.");

        // Case 3: Smaller seat reserved, recommend larger seat
        selectedSeatNumbers = List.of(4); // Select seat 4
        seats.get(2).setReserved(true); // Reserve seat 3 (smaller seat)
        recommendedSeat = venueService.recommendClosestSeat(section.getID(), row.getID(), selectedSeatNumbers);
        assertNotNull(recommendedSeat, "Recommended seat should not be null.");
        assertEquals(5, recommendedSeat.getNumber(),
                "When the smaller seat is reserved, the recommended seat should be the larger one.");

        // Case 4: Selected seat reserved, recommend the next available larger seat
        selectedSeatNumbers = List.of(5); // Select seat 5
        seats.get(3).setReserved(true); // Reserve seat 4 (smaller seat)
        seats.get(5).setReserved(true); // Reserve seat 6 (larger seat)
        recommendedSeat = venueService.recommendClosestSeat(section.getID(), row.getID(), selectedSeatNumbers);
        assertNotNull(recommendedSeat, "Recommended seat should not be null.");
        assertEquals(7, recommendedSeat.getNumber(),
                "When the selected seat is reserved, and the next smaller and larger seats are reserved, the recommended seat should be the next available larger one.");


        // Case 5: Selecting multiple seats, smaller seat reserved, recommend the next larger seat
        selectedSeatNumbers = List.of(5, 6); // Select seats 5 and 6
        seats.get(3).setReserved(true); // Reserve seat 4 (smaller seat)
        recommendedSeat = venueService.recommendClosestSeat(section.getID(), row.getID(), selectedSeatNumbers);
        assertNotNull(recommendedSeat, "Recommended seat should not be null.");
        assertEquals(7, recommendedSeat.getNumber(),
                "When selecting multiple seats and the smaller seat is reserved, the recommended seat should be the next larger one.");

        /// Case 6: Selecting a single seat, smaller seat reserved, recommend the next smaller seat
        selectedSeatNumbers = List.of(10); // Select seat 10
        seats.get(8).setReserved(true); // Reserve seat 9 (smaller seat)
        recommendedSeat = venueService.recommendClosestSeat(section.getID(), row.getID(), selectedSeatNumbers);
        assertNotNull(recommendedSeat, "Recommended seat should not be null.");
        assertEquals(8, recommendedSeat.getNumber(),
                "When selecting a single seat and the smaller seat is reserved, the recommended seat should be the next smaller one.");

    }

    /**
     * Tests failure cases for the recommended seat selection feature.
     */
    @Order(10)
    @DisplayName("Complex Method: Recommended Seat (Failure Cases)")
    @Test
    public void testRecommendClosestSeatFailure() {
        // 1. Create Venue, Section, Row, and Seats
        Venue venue = venueService.createVenue("Concert Hall", "New York", 1000, true);
        venueService.addSectionToVenue(venue.getID(), 1, 1000, "Main Section");
        Section section = venueService.getSectionsByVenueID(venue.getID()).get(0);
        venueService.addRowsToSection(section.getID(), 1, 50); // Add one row with 50 seats
        Row row = venueService.findRowsBySection(section.getID()).get(0);
        venueService.addSeatsToRow(row.getID(), 10); // Add 10 seats to the row

        // 2. Get the seats in the row
        List<Seat> seats = venueService.getSeatsByRow(row.getID());

        // 3. Reserve all seats to simulate no available seats
        seats.forEach(seat -> seat.setReserved(true));

        // Case 1: All seats reserved
        List<Integer> selectedSeatNumbers = List.of(5); // Select seat 5
        Seat recommendedSeat = venueService.recommendClosestSeat(section.getID(), row.getID(), selectedSeatNumbers);
        assertNull(recommendedSeat, "When all seats are reserved, no seat should be recommended.");

        // 4. Unreserve one seat and select it to simulate selecting a reserved seat
        seats.get(4).setReserved(false); // Unreserve seat 5
        selectedSeatNumbers = List.of(5); // Select seat 5 again
        recommendedSeat = venueService.recommendClosestSeat(section.getID(), row.getID(), selectedSeatNumbers);
        assertNull(recommendedSeat, "When the selected seat is reserved, no recommendation should be made.");

        // 5. Select a seat in a non-existent section
        int nonExistentSectionId = 999;
        recommendedSeat = venueService.recommendClosestSeat(nonExistentSectionId, row.getID(), selectedSeatNumbers);
        assertNull(recommendedSeat, "When the section does not exist, no seat should be recommended.");

        // 6. Select a seat in a non-existent row
        int nonExistentRowId = 999;
        recommendedSeat = venueService.recommendClosestSeat(section.getID(), nonExistentRowId, selectedSeatNumbers);
        assertNull(recommendedSeat, "When the row does not exist, no seat should be recommended.");
    }


}