package controller;

import model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Controller {
    private final ArtistController artistController;
    private final AthleteController athleteController;
    private final SeatController seatController;
    private final RowController rowController;
    private final SectionController sectionController;
    private final VenueController venueController;
    private final TicketController ticketController;
    private final CartController cartController;
    private final CustomerController customerController;
    private final EventController eventController;
    private final PaymentController paymentController;
    private final PurchaseHistoryController purchaseHistoryController;
    private final UserController userController;

    /**
     * Constructs a new Controller instance that manages various aspects of the application, including user accounts,
     * events, venues, seating, artists, athletes, customers, orders, shopping carts, and tickets. Each of these
     * responsibilities is handled by a specific controller, which is injected into the Controller.
     *
     * @param artistController The controller responsible for managing artists.
     * @param athleteController The controller responsible for managing athletes.
     * @param seatController The controller responsible for managing seats in venues.
     * @param rowController The controller responsible for managing rows in venues.
     * @param sectionController The controller responsible for managing sections within venues.
     * @param venueController The controller responsible for managing venues.
     * @param ticketController The controller responsible for managing tickets.
     * @param cartController The controller responsible for managing shopping carts.
     * @param customerController The controller responsible for managing customers.
     * @param eventController The controller responsible for managing events.
     * @param paymentController The controller responsible for managing payments.
     * @param userController The controller responsible for managing user accounts.
     */
    public Controller(ArtistController artistController, AthleteController athleteController,
                      SeatController seatController, RowController rowController,
                      SectionController sectionController, VenueController venueController,
                      TicketController ticketController, CartController cartController,
                      CustomerController customerController, EventController eventController,
                      PaymentController paymentController, PurchaseHistoryController purchaseHistoryController,
                      UserController userController) {
        this.artistController = artistController;
        this.athleteController = athleteController;
        this.seatController = seatController;
        this.rowController = rowController;
        this.sectionController = sectionController;
        this.venueController = venueController;
        this.ticketController = ticketController;
        this.cartController = cartController;
        this.customerController = customerController;
        this.eventController = eventController;
        this.paymentController = paymentController;
        this.purchaseHistoryController = purchaseHistoryController;
        this.userController = userController;
    }

    // 1. Artist
    public List<Artist> findArtistsByGenre(String genre) {
        return artistController.findArtistsByGenre(genre);}
    public void createArtist(String artistName, String genre) {
        artistController.createArtist(artistName, genre);}
    public void updateArtist(int artistID, String newName, String newGenre) {
        artistController.updateArtist(artistID, newName, newGenre);}
    public void deleteArtist(int artistId) {
        artistController.deleteArtist(artistId);}
    public List<Artist> getAllArtists() {
        return artistController.getAllArtists();}
    public Artist findArtistByName(String artistName) {
        return artistController.findArtistByName(artistName);}
    public Artist findArtistByID(int artistId) {
        return artistController.findArtistByID(artistId); }

    // 2. Athlete
    public List<Athlete> findAthletesBySport(String sport) {
        return athleteController.findAthletesBySport(sport);}
    public void createAthlete(String athleteName, String athleteSport) {
        athleteController.createAthlete(athleteName, athleteSport);}
    public void updateAthlete(int athleteID, String newName, String newSport) {
        athleteController.updateAthlete(athleteID, newName, newSport);}
    public void deleteAthlete(int athleteID) {
        athleteController.deleteAthlete(athleteID);}
    public List<Athlete> getAllAthletes() {
        return athleteController.getAllAthletes();}
    public Athlete findAthleteByName(String athleteName) {
        return athleteController.findAthleteByName(athleteName);}
    public Athlete findAthleteByID(int athleteID) {
        return athleteController.findAthleteByID(athleteID); }

    // 3. Cart
    public Cart createCart(Customer customer, Event event) {
        return cartController.createCart(customer, event);}
    public void addTicketToCart(Cart cart, Ticket ticket) {
        cartController.addTicketToCart(cart, ticket);}
    public void removeTicketFromCart(Cart cart, Ticket ticket) {
        cartController.removeTicketFromCart(cart, ticket);}
    public void clearCart(Cart cart) {
        cartController.clearCart(cart);}
    public void finalizeCart(Cart cart) {
        cartController.finalizeCart(cart);}
    public List<Ticket> getTicketsInCart(Cart cart) {
        return cartController.getTicketsInCart(cart);}
    public Cart findCartByID(int cartID) {
        return cartController.findCartByID(cartID);}

    // 4. Customer
    public void addFavourite(FavouriteEntity item) {
        customerController.addFavourite(item);}
    public void removeFavourite(FavouriteEntity item) {
        customerController.removeFavourite(item);}
    public Set<FavouriteEntity> getFavourites() {
        return customerController.getFavourites();}

    // 5. Event
    public boolean addArtistToConcert(int eventId, int artistId) {
        return eventController.addArtistToConcert(eventId, artistId);}
    public boolean addAthleteToSportsEvent(int eventId, int athleteId) {
        return eventController.addAthleteToSportsEvent(eventId, athleteId);}
    public Event findEventByID(int eventId) {
        return eventController.findEventByID(eventId);}
    public List<Event> getEventsByLocation(String locationOrVenueName) {
        return eventController.getEventsByLocation(locationOrVenueName);}
    public List<Event> getUpcomingEventsForArtist(int artistID) {
        return eventController.getUpcomingEventsForArtist(artistID);}
    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
        return eventController.getUpcomingEventsForAthlete(athleteID);}
    public void createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        eventController.createConcert(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);}
    public void createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        eventController.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);}
    public void updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime,EventStatus newStatus) {
        eventController.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);}
    public void deleteEvent(int eventId) {
        eventController.deleteEvent(eventId);}
    public List<Event> getAllEvents() {
        return eventController.getAllEvents();}
    // TODO
//    public boolean isEventSoldOut(Event event) {
//        return eventController.isEventSoldOut(event);}

    // 6. Payment
    public void processPayment(Cart cart, String cardNumber, String cardholderName,
                               int expiryMonth, int expiryYear, String cvv, String currency) {
        paymentController.processPayment(cart, cardNumber, cardholderName, expiryMonth, expiryYear, cvv, currency);
    }

    // 7. Purchase History
    public void createPurchaseHistory(Cart cart) {
        purchaseHistoryController.createPurchaseHistory(cart);}
    public List<PurchaseHistory> getPurchaseHistoryForCustomer(Customer customer) {
        return purchaseHistoryController.getPurchaseHistoryForCustomer(customer);}
    public List<PurchaseHistory> getAllPurchaseHistories() {
        return purchaseHistoryController.getAllPurchaseHistories();}
    public PurchaseHistory findPurchaseHistoryByID(int id) {
        return purchaseHistoryController.findPurchaseHistoryByID(id);}

    // 8. User
    public Customer findCustomerByID(int customerID) {
        return userController.findCustomerByID(customerID);}
    public User getCurrentUser() {
        return userController.getCurrentUser();}
    public List<User> getAllUsers() {
        return userController.getAllUsers();}
    public boolean isUsernameTaken(String username) {
        return userController.isUsernameTaken(username);}
    public boolean domainEmail(String email) {
        return userController.domainEmail(email);}
    public void createAccount(String role, String username, String email, String password) {
        userController.createAccount(role, username, email, password);}
    public void login(String username, String password) {
        userController.login(username, password);}
    public void logout() {
        userController.logout();}
    public void deleteAccount(int id) {
        userController.deleteAccount(id);}

    // Row related
    public Row createRow(int sectionId, int rowCapacity) {
        return rowController.createRow(sectionId, rowCapacity);}
    public void updateRow(int rowId, int rowCapacity) {
        rowController.updateRow(rowId, rowCapacity);}
    public void deleteRow(int rowId) {
        rowController.deleteRow(rowId);}
    public Row findRowByID(int rowId) {
        return rowController.findRowByID(rowId);}
    public List<Row> getAllRows() {
        return rowController.getAllRows();}
    public void addSeatsToRow(int rowId, int numberOfSeats) {
        rowController.addSeatsToRow(rowId, numberOfSeats);}
    public List<Seat> getSeatsByRowID(int rowId) {
        return rowController.getSeatsByRowID(rowId);}
    public List<Row> findRowsBySection(int sectionId) {
        return rowController.findRowsBySection(sectionId);}
    public void getAvailableSeatsInRow(int rowId, int eventId) {
        rowController.getAvailableSeatsInRow(rowId, eventId);}

    // Seat related
    public void createSeat(int rowId, int seatNumber) {
        seatController.createSeat(rowId, seatNumber);}
    public void deleteSeatById(int seatId) {
        seatController.deleteSeatById(seatId);}
    public Seat findSeatByID(int seatId) {
        return seatController.findSeatByID(seatId);}
    public void getAllSeats() {
        seatController.getAllSeats();}
    public List<Seat> getSeatsByRow(int rowId) {
        return seatController.getSeatsByRow(rowId);
    }
    public List<Seat> getAvailableSeatsInSection(int sectionId) {
        return seatController.getAvailableSeatsInSection(sectionId);
    }
    public List<Seat> getAvailableSeatsInVenue(int venueId) {
        return seatController.getAvailableSeatsInVenue(venueId);}
    public void reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        seatController.reserveSeat(seatId, event, customer, price, ticketType);}
    public void isSeatReservedForEvent(int seatId, int eventId) {
        seatController.isSeatReservedForEvent(seatId, eventId);
    }
    public void unreserveSeat(int seatId) {
        seatController.unreserveSeat(seatId);}
    public Seat recommendClosestSeat(int rowId, int seatNumber) {
        return seatController.recommendClosestSeat(rowId, seatNumber);
    }

    // Section related
    public void createSection(String sectionName, int sectionCapacity, Venue venue) {
        sectionController.createSection(sectionName, sectionCapacity, venue);}
    public void updateSection(int sectionId, String sectionName, int sectionCapacity) {
        sectionController.updateSection(sectionId, sectionName, sectionCapacity);}
    public void deleteSection(int sectionId) {
        sectionController.deleteSection(sectionId);}
    public Section findSectionByID(int sectionId) {
        return sectionController.findSectionByID(sectionId);}
    public void getAllSections() {
        sectionController.getAllSections();}
    public void findSectionsByName(String name) {
        sectionController.findSectionsByName(name);}
    public void getAvailableSeats(int sectionId, int eventId) {
        sectionController.getAvailableSeats(sectionId, eventId);}

    // Venue related
    public void createVenue(String name, String location, int capacity, boolean hasSeats) {
        venueController.createVenue(name, location, capacity, hasSeats);}
    public Venue findVenueByID(int venueId) {
        return venueController.findVenueByID(venueId);}
    public List<Venue> findVenuesByLocationOrName(String keyword) {
        return venueController.findVenuesByLocationOrName(keyword);}
    public Venue findVenueByName(String name) {
        return venueController.findVenueByName(name);}
    public List<Venue> getAllVenues() {
        return venueController.getAllVenues();}
    public void updateVenue(int venueId, String name, String location, int capacity, boolean hasSeats) {
        venueController.updateVenue(venueId, name, location, capacity, hasSeats);}
    public void deleteVenue(int venueId) {
        venueController.deleteVenue(venueId);}
    public void addSectionToVenue(int venueId, String sectionName, int sectionCapacity) {
        venueController.addSectionToVenue(venueId, sectionName, sectionCapacity);}
    public List<Section> getSectionsByVenueID(int venueId) {
        return venueController.getSectionsByVenueID(venueId);}
    public void getAvailableSeatsInVenue(int venueId, int eventId) {
        venueController.getAvailableSeatsInVenue(venueId, eventId);}

    // Ticket related
    public List<Ticket> generateTicketsForEvent(Event event, double basePrice) {
        return ticketController.generateTicketsForEvent(event, basePrice);}
    public void reserveTicket(Ticket ticket, Customer customer) {
        ticketController.reserveTicket(ticket, customer);}
    public void releaseTicket(Ticket ticket) {
        ticketController.releaseTicket(ticket);}
    public List<Ticket> getTicketsByEvent(Event event) {
        return ticketController.getTicketsByEvent(event);}
    public List<Ticket> getAvailableTicketsForEvent(Event event) {
        return ticketController.getAvailableTicketsForEvent(event);}
    public List<Ticket> findTicketsByCartID(int cartID) {
        return ticketController.findTicketsByCartID(cartID);}
    public void deleteTicket(int ticketID) {
        ticketController.deleteTicket(ticketID);}
    public Ticket findTicketByID(int ticketID) {
        return ticketController.findTicketByID(ticketID);}
    public double calculateTotalPrice(List<Ticket> tickets) {
        return ticketController.calculateTotalPrice(tickets);}
}