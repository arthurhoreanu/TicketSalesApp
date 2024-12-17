package controller;

import model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Controller {
    private final AccountController accountController;
    private final EventController eventController;
    private final VenueController venueController;
    public final SectionController sectionController;
    private final SeatController seatController;
    private final ArtistController artistController;
    private final AthleteController athleteController;
    private final CustomerController customerController;
    private final OrderController orderController;
    private final ShoppingCartController shoppingCartController;
    private final TicketController ticketController;
    private final RowController rowController;

    /**
     * Constructs a new Controller instance that manages various aspects of the application, including user accounts,
     * events, venues, seating, artists, athletes, customers, orders, shopping carts, and tickets. Each of these
     * responsibilities is handled by a specific controller, which is injected into the Controller.
     *
     * @param accountController The controller responsible for managing user accounts.
     * @param eventController The controller responsible for managing events.
     * @param venueController The controller responsible for managing venues.
     * @param sectionController The controller responsible for managing sections within venues.
     * @param seatController The controller responsible for managing seats in venues.
     * @param artistController The controller responsible for managing artists.
     * @param athleteController The controller responsible for managing athletes.
     * @param customerController The controller responsible for managing customers.
     * @param orderController The controller responsible for managing orders.
     * @param shoppingCartController The controller responsible for managing shopping carts.
     * @param ticketController The controller responsible for managing tickets.
     */
    public Controller(AccountController accountController, EventController eventController, VenueController venueController,
                      SectionController sectionController, SeatController seatController, ArtistController artistController,
                      AthleteController athleteController, CustomerController customerController, OrderController orderController,
                      ShoppingCartController shoppingCartController, TicketController ticketController, RowController rowController) {
        this.accountController = accountController;
        this.eventController = eventController;
        this.venueController = venueController;
        this.sectionController = sectionController;
        this.seatController = seatController;
        this.artistController = artistController;
        this.athleteController = athleteController;
        this.customerController = customerController;
        this.orderController = orderController;
        this.shoppingCartController = shoppingCartController;
        this.ticketController = ticketController;
        this.rowController = rowController;
    }

    // Rule: newest first

    // Row related
    public Row findRowByID(int rowID) {
        return rowController.findRowByID(rowID);}
    public void createRowWithSeats(int rowCapacity, Section section) {
        rowController.createRowWithSeats(rowCapacity, section);}
    public void updateRow(int rowID, int newCapacity) {
        rowController.updateRow(rowID, newCapacity);}
    public void deleteRow(int rowID) {
        rowController.deleteRow(rowID);}
    public void getAllRows() {
        rowController.getAllRows();}
    public void findRowsBySection(Section section) {
        rowController.findRowsBySection(section);}
    public void getAvailableSeatsInRow(Row row, Event event) {
        rowController.getAvailableSeatsInRow(row, event);}
    public void getSeatsByRow(Row row) {
        rowController.getSeatsByRow(row);}

    // Customer related
    public void addFavourite(FavouriteEntity item) {
        customerController.addFavourite(item);}
    public void removeFavourite(FavouriteEntity item) {
        customerController.removeFavourite(item);}
    public Set<FavouriteEntity> getFavourites() {
        return customerController.getFavourites();}

    //Athlete related
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

    //Artist related
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

    // Seat related
    public void createSeat(int seatID, Row row, boolean isReserved, Event reservedForEvent) {
        seatController.createSeat(seatID, row, isReserved, reservedForEvent);}
    public Seat findSeatByID(int seatID) {
        return seatController.findSeatByID(seatID);}
    // TODO has the same name as the one from row
    public void getSeatsByRow_SEAT(Row row) {
        seatController.getSeatsByRow_SEAT(row);}
    public void checkSeatReservation(Seat seat, Event event) {
        seatController.checkSeatReservation(seat, event);}
    public void reserveSeatForEvent(Seat seat, Event event) {
        seatController.reserveSeatForEvent(seat, event);}
    public void clearSeatReservationForEvent(Seat seat, Event event) {
        seatController.clearSeatReservationForEvent(seat, event);}
    // TODO has the same name as the one from row
    public void getAvailableSeatsInRow_SEAT(Row row, Event event) {
        seatController.getAvailableSeatsInRow_SEAT(row, event);}
    public void recommendFrontRowSeat(List<Seat> availableSeats) {
        seatController.recommendFrontRowSeat(availableSeats);}
    public void deleteSeatByID(int seatID) {
        seatController.deleteSeatByID(seatID);}
    public void getAllSeats() {
        seatController.getAllSeats();}

    // Section related
    public void createSection(String sectionName, int sectionCapacity, Venue venue) {
        sectionController.createSection(sectionName, sectionCapacity, venue);}
    public void updateSection(int sectionID, String newSectionName, int newSectionCapacity) {
        sectionController.updateSection(sectionID, newSectionName, newSectionCapacity);}
    public void deleteSection(int sectionID) {
        sectionController.deleteSection(sectionID);}
    public void getAllSections() {
        sectionController.getAllSections();}
    public Section findSectionByID(int sectionID) {
        return sectionController.findSectionByID(sectionID);}
    public void getSectionInfo(String sectionName) {
        sectionController.getSectionInfo(sectionName);}

    // Venue related
    public void createVenue(String name, String location, int capacity) {
        venueController.createVenue(name, location, capacity);}
    public void updateVenue(int venueId, String newName, String newLocation, int newCapacity) {
        venueController.updateVenue(venueId, newName, newLocation, newCapacity);}
    public void deleteVenue(int id) {
        venueController.deleteVenue(id);}
    public List<Venue> getAllVenues() {
        return venueController.getAllVenues();}
    public Venue findVenueByID(int id){
        return venueController.findVenueByID(id);}
    public Venue findVenueByName(String name) {
        return venueController.findVenueByName(name);}
    public List<Venue> findVenuesByLocationOrName(String locationOrVenueName) {
        return venueController.findVenuesByLocationOrName(locationOrVenueName);}
    public void getAvailableSeats(Venue venue, Event event) {
        venueController.getAvailableSeats(venue, event);}
//    public Seat recommendSeat(Customer customer, Venue venue, Event event) {
//        return venueController.recommendSeat(customer, venue, event);}

    // Ticket related
    public List<Ticket> getTicketsByEvent(Event event) {
        return ticketController.getTicketsByEvent(event);}
    public void generateTicketsForEvent(Event event, double standardPrice, double vipPrice) {
        ticketController.generateTicketsForEvent(event, standardPrice, vipPrice);}
    public List<Ticket> getAvailableTicketsForEvent(Event event) {
        return ticketController.getAvailableTicketsForEvent(event);
    }
    public void reserveTicket(Ticket ticket, String purchaserName) {
        ticketController.reserveTicket(ticket, purchaserName);}
    public void releaseTicket(Ticket ticket) {
        ticketController.releaseTicket(ticket);}
    public Ticket findTicketByID(int ticketId) {
        return ticketController.findTicketByID(ticketId);
    }
    public void deleteTicket(int ticketId) {
        ticketController.deleteTicket(ticketId);}
    public double calculateTotalPrice(List<Ticket> tickets) {
      return ticketController.calculateTotalPrice(tickets);}

    // Shopping Cart related
    public void addTicketToCart(Ticket ticket) {
        shoppingCartController.addTicketToCart(ticket);}
    public void removeTicketFromCart(Ticket ticket) {
        shoppingCartController.removeTicketFromCart(ticket);}
    public void clearCart() {
        shoppingCartController.clearCart();}
    public Order checkout() {
       return shoppingCartController.checkout();}
    public void updateTotalPrice() {
        shoppingCartController.updateTotalPrice();}
    public double getTotalPrice() {
        return shoppingCartController.getTotalPrice();}

    // Order related
    public void createOrder(Customer customer) {
        orderController.createOrder(customer);}
    public void processOrderPayment(Order order, String cardNumber, int cvv, String cardOwner, String expirationDate) {
        orderController.processOrderPayment(order, cardNumber, cvv, cardOwner, expirationDate);}
    public void cancelOrder(int orderId) {
        orderController.cancelOrder(orderId);}
    public void getOrderHistory(Customer customer) {
        orderController.getOrderHistory(customer);}
    public void getOrderByID(int orderID) {
        orderController.getOrderByID(orderID);}
    public void orderAllTicketsFromCart(Customer customer) {
        orderController.orderAllTicketsFromCart(customer);}
    public Order orderTicketsForEvent(Customer customer, Event event) {
        return orderTicketsForEvent(customer, event);}

    // Event related
    public boolean addArtistToConcert(int eventId, int artistId) {
        return eventController.addArtistToConcert(eventId, artistId);
    }
    public boolean addAthleteToSportsEvent(int eventId, int athleteId) {
        return eventController.addAthleteToSportsEvent(eventId, athleteId);
    }
    public int getLastCreatedEventID() {
        return  eventController.getLastCreatedEventID();
    }
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

    // Account related
    public User getCurrentUser() {
        return accountController.getCurrentUser();}
    public List<User> getAllUsers() {
        return accountController.getAllUsers();}
    public boolean isUsernameTaken(String username) {
        return accountController.isUsernameTaken(username);}
    public boolean domainEmail(String email) {
        return accountController.domainEmail(email);}
    public void createAccount(String role, String username, String email, String password) {
        accountController.createAccount(role, username, email, password);}
    public void login(String username, String password) {
        accountController.login(username, password);}
    public void logout() {
        accountController.logout();}
    public void deleteAccount(int id) {
        accountController.deleteAccount(id);}
}