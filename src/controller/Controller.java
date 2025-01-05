package controller;

import model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Controller {
    private final ArtistController artistController;
    private final AthleteController athleteController;
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
     * @param venueController The controller responsible for managing venues.
     * @param ticketController The controller responsible for managing tickets.
     * @param cartController The controller responsible for managing shopping carts.
     * @param customerController The controller responsible for managing customers.
     * @param eventController The controller responsible for managing events.
     * @param paymentController The controller responsible for managing payments.
     * @param userController The controller responsible for managing user accounts.
     */
    public Controller(ArtistController artistController, AthleteController athleteController,
                     VenueController venueController, TicketController ticketController,
                      CartController cartController, CustomerController customerController,
                      EventController eventController, PaymentController paymentController,
                      PurchaseHistoryController purchaseHistoryController, UserController userController) {
        this.artistController = artistController;
        this.athleteController = athleteController;
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
    public boolean createArtist(String artistName, String genre) {
        return artistController.createArtist(artistName, genre);}
    public boolean updateArtist(int artistID, String newName, String newGenre) {
        return artistController.updateArtist(artistID, newName, newGenre);}
    public boolean deleteArtist(int artistId) {
        return artistController.deleteArtist(artistId);}
    public List<Artist> getAllArtists() {
        return artistController.getAllArtists();}
    public Artist findArtistByName(String artistName) {
        return artistController.findArtistByName(artistName);}
    public Artist findArtistByID(int artistId) {
        return artistController.findArtistByID(artistId);}

    // 2. Athlete
    public boolean createAthlete(String athleteName, String athleteSport) {
        return athleteController.createAthlete(athleteName, athleteSport);}
    public boolean updateAthlete(int athleteID, String newName, String newSport) {
        return athleteController.updateAthlete(athleteID, newName, newSport);}
    public boolean deleteAthlete(int athleteID) {
        return athleteController.deleteAthlete(athleteID);}
    public List<Athlete> getAllAthletes() {
        return athleteController.getAllAthletes();}
    public Athlete findAthleteByName(String athleteName) {
        return athleteController.findAthleteByName(athleteName);}
    public Athlete findAthleteByID(int athleteID) {
        return athleteController.findAthleteByID(athleteID);}
    public List<Athlete> findAthletesBySport(String sport) {
        return athleteController.findAthletesBySport(sport);}


    // 3. Venue
    /// Seat
    public Seat createSeat(int rowId, int seatNumber) {
        return venueController.createSeat(rowId, seatNumber);}
    public void deleteSeat(int seatId) {
        venueController.deleteSeat(seatId);}
    public Seat findSeatByID(int seatId) {
        return venueController.findSeatByID(seatId);}
    public List<Seat> getAllSeats() {
        return venueController.getAllSeats();}
    public void reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        venueController.reserveSeat(seatId, event, customer, price, ticketType);}
    public void unreserveSeat(int seatId) {
        venueController.unreserveSeat(seatId);}
    public boolean isSeatReservedForEvent(int seatId, int eventId) {
        return venueController.isSeatReservedForEvent(seatId, eventId);}

    /// Row
    public Row createRow(Section section, int rowCapacity) {
        return venueController.createRow(section, rowCapacity);}
    public Row updateRow(int rowId, int rowCapacity) {
        return venueController.updateRow(rowId, rowCapacity);}
    public void deleteRow(int rowId) {
        venueController.deleteRow(rowId);}
    public void deleteRowsBySection(int sectionId) {
        venueController.deleteRowsBySection(sectionId);}
    public Row findRowByID(int rowId) {
        return venueController.findRowByID(rowId);}
    public List<Row> getAllRows() {
        return venueController.getAllRows();}
    public void addSeatsToRow(int rowId, int numberOfSeats) {
        venueController.addSeatsToRow(rowId, numberOfSeats);}
    public List<Seat> getSeatsByRow(int rowId) {
        return venueController.getSeatsByRow(rowId);}
    public List<Row> findRowsBySection(int sectionId) {
        return venueController.findRowsBySection(sectionId);}
    public List<Seat> getAvailableSeatsInRow(int rowId, int eventId) {
        return venueController.getAvailableSeatsInRow(rowId, eventId);}
    public Seat recommendClosestSeat(int rowId, int seatNumber) {
        return venueController.recommendClosestSeat(rowId, seatNumber);}

    /// Section
    public Section createSection(Venue venue, int sectionCapacity, String sectionName) {
        return venueController.createSection(venue, sectionCapacity, sectionName);}
    public Section updateSection(int sectionId, String sectionName, int sectionCapacity) {
        return venueController.updateSection(sectionId, sectionName, sectionCapacity);}
    public void deleteSection(int sectionId) {
        venueController.deleteSection(sectionId);}
    public Section findSectionByID(int sectionId) {
        return venueController.findSectionByID(sectionId);}
    public List<Section> getAllSections() {
        return venueController.getAllSections();}
    public List<Section> findSectionsByName(String name) {
        return venueController.findSectionsByName(name);}
    public List<Seat> getAvailableSeatsInSection(int sectionId, int eventId) {
        return venueController.getAvailableSeatsInSection(sectionId, eventId);}

    /// Venue
    public Venue createVenue(String name, String location, int capacity, boolean hasSeats) {
        return venueController.createVenue(name, location, capacity, hasSeats);}
    public Venue findVenueByID(int venueId) {
        return venueController.findVenueByID(venueId);}
    public List<Venue> findVenuesByLocationOrName(String keyword) {
        return venueController.findVenuesByLocationOrName(keyword);}
    public Venue findVenueByName(String name) {
        return venueController.findVenueByName(name);}
    public List<Venue> getAllVenues() {
        return venueController.getAllVenues();}
    public Venue updateVenue(int venueId, String name, String location, int capacity, boolean hasSeats) {
        return venueController.updateVenue(venueId, name, location, capacity, hasSeats);}
    public boolean deleteVenue(int venueId) {
        return venueController.deleteVenue(venueId);}
    public void addSectionToVenue(int venueId, int numberOfSections, int sectionCapacity, String defaultSectionName) {
        venueController.addSectionToVenue(venueId, numberOfSections, sectionCapacity, defaultSectionName);}
    public List<Section> getSectionsByVenueID(int venueId) {
        return venueController.getSectionsByVenueID(venueId);}
    public List<Seat> getAvailableSeatsInVenue(int venueId, int eventId) {
        return venueController.getAvailableSeatsInVenue(venueId, eventId);
    }

    // 4. Ticket
    public List<Ticket> generateTicketsForEvent(Event event, double basePrice, int earlyBirdCount, int vipCount, int standardCount) {
        return ticketController.generateTicketsForEvent(event, basePrice, earlyBirdCount, vipCount, standardCount);}
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

    public double getBasePriceForEvent(int eventId) {
        return eventController.getBasePriceForEvent(eventId);
    }

    // 5. Cart
    public Cart createCart(Customer customer, Event event) {
        return cartController.createCart(customer, event);}
    public boolean addTicketToCart(Cart cart, Ticket ticket) {
        return cartController.addTicketToCart(cart, ticket);}
    public boolean removeTicketFromCart(Cart cart, Ticket ticket) {
        return cartController.removeTicketFromCart(cart, ticket);}
    public void clearCart(Cart cart) {
        cartController.clearCart(cart);}
    public void finalizeCart(Cart cart) {
        cartController.finalizeCart(cart);}
    public List<Ticket> getTicketsInCart(Cart cart) {
        return cartController.getTicketsInCart(cart);}
    public Cart findCartByID(int cartID) {
        return cartController.findCartByID(cartID);}

    // 6. Customer
    public boolean addFavourite(FavouriteEntity item) {
        return customerController.addFavourite(item);}
    public boolean removeFavourite(FavouriteEntity item) {
        return customerController.removeFavourite(item);}
    public Set<FavouriteEntity> getFavourites() {
        return customerController.getFavourites();}
    public void setCurrentCustomer(Customer customer) {
        customerController.setCurrentCustomer(customer);}
    public Customer getCurrentCustomer() {
        return customerController.getCurrentCustomer();}

    // 7. Event
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
    public Concert createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        return eventController.createConcert(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);}
    public SportsEvent createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        return eventController.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);}
    public boolean updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        return eventController.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);}
    public boolean deleteEvent(int eventId) {
        return eventController.deleteEvent(eventId);}
    public List<Event> getAllEvents() {
        return eventController.getAllEvents();}
    public Concert findConcertByID(int concertID) {
        return eventController.findConcertByID(concertID);}
    public SportsEvent findSportsEventByID(int sportsEventID) {
        return eventController.findSportsEventByID(sportsEventID);}
    public boolean removeArtistFromConcert(int eventID, int artistID) {
        return eventController.removeArtistFromConcert(eventID, artistID);}
    public boolean removeAthleteFromSportsEvent(int eventID, int athleteID) {
        return eventController.removeAthleteFromSportsEvent(eventID, athleteID);}
    public List<Artist> getArtistsByConcert(int concertID) {
        return eventController.getArtistsByConcert(concertID);}
    public List<Athlete> getAthletesBySportsEvent(int sportsEventID) {
        return eventController.getAthletesBySportsEvent(sportsEventID);}
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
    public boolean createAccount(String role, String username, String email, String password) {
        return userController.createAccount(role, username, email, password);}
    public boolean login(String username, String password) {
        return userController.login(username, password);}
    public boolean logout() {
        return userController.logout();}
    public boolean deleteAccount(int id) {
        return userController.deleteAccount(id);}

}