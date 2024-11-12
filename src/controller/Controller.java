package controller;

import model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Controller {
    private final AccountController accountController;
    private final EventController eventController;
    private final VenueController venueController;
    private final SectionController sectionController;
    private final SeatController seatController;
    private final ArtistController artistController;
    private final AthleteController athleteController;
    private final CustomerController customerController;
    private final OrderController orderController;
    private final ShoppingCartController shoppingCartController;
    private final TicketController ticketController;



    public Controller(AccountController accountController, EventController eventController, VenueController venueController,
                      SectionController sectionController, SeatController seatController, ArtistController artistController,
                      AthleteController athleteController, CustomerController customerController, OrderController orderController, ShoppingCartController shoppingCartController, TicketController ticketController) {
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
    }

    // Rule: newest first
    // Customer related
    public void addFavorite(FavouriteEntity item) {
        customerController.addFavorite(item);}
    public void removeFavorite(FavouriteEntity item) {
        customerController.removeFavorite(item);}
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
    public List<Event> getEventsByAthlete(Athlete athlete) {
        return athleteController.getEventsByAthlete(athlete);}
    public List<Event> getsEventByVenue(Venue venue) {
        return eventController.getEventsByVenue(venue);}

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
    public List<Event> getEventsByArtist(Artist artist) {
        return artistController.getEventsByArtist(artist);}

    // Seat related
    public void createSeat(int seatID, Section section, int rowNumber, int sitNumber, Event reservedForEvent) {
        seatController.createSeat(seatID, section, rowNumber, sitNumber, reservedForEvent);
    }
    public void findSeatById(int seatID) {
        seatController.findSeatById(seatID);
    }
    public void checkSeatReservation(Seat seat, Event event) {
        seatController.checkSeatReservation(seat, event);
    }
    public void reserveSeatForEvent(Seat seat, Event event) {
        seatController.reserveSeatForEvent(seat, event);
    }
    public void clearSeatReservationForEvent(Seat seat, Event event) {
        seatController.clearSeatReservationForEvent(seat, event);
    }
    public void recommendFrontRowSeat(List<Seat> availableSeats) {
        seatController.recommendFrontRowSeat(availableSeats);
    }


    // Section related
    public void getAvailableSeats(Section section, Event event) {
        sectionController.getAvailableSeats(section, event);
    }
    public void recommendSeat(Customer customer, Section section, Event event) {
        sectionController.recommendSeat(customer, section, event);
    }
    public Section createSectionWithSeats(String sectionName, int sectionId, int sectionCapacity, int rowCount, int seatsPerRow, Venue venue) {
        return sectionController.createSectionWithSeats(sectionName, sectionId, sectionCapacity, rowCount, seatsPerRow, venue);
    }
    public void getSectionInfo(Section section, Event event) {
        sectionController.getSectionInfo(section, event);
    }

    // Venue related
    public void addVenue(String name, String location, int capacity, List<Section> sections) {
        venueController.addVenue(name, location, capacity, sections);
    }
    public void createVenueWithSectionsAndSeats(String name, String location, int capacity, int sectionCapacity, int rowCount, int seatsPerRow) {
        venueController.createVenueWithSectionsAndSeats(name, location, capacity, sectionCapacity, rowCount, seatsPerRow);
    }
    public void updateVenue(int id, String newName, String newLocation, int newCapacity, List<Section> newSections) {
        venueController.updateVenue(id, newName, newLocation, newCapacity, newSections);
    }
    public void deleteVenue(int id) {
        venueController.deleteVenue(id);
    }
    public List<Venue> getAllVenues() {
        return venueController.getAllVenues();
    }
    public Venue findVenueByName(String name) {
        return venueController.findVenueByName(name);
    }
    public void getVenuesByLocationOrName(String locationOrVenueName) {
        venueController.getVenuesByLocationOrName(locationOrVenueName);
    }
    public void getAvailableSeats(Venue venue, Event event) {
        venueController.getAvailableSeats(venue, event);
    }
    public void recommendSeat(Customer customer, Venue venue, Event event) {
        venueController.recommendSeat(customer, venue, event);
    }
    public Venue findVenueById(int id){
        return venueController.findVenueById(id);
    }

    // Ticket related
    public List<Ticket> getTicketsByEvent(int eventId) {
        return ticketController.getTicketsByEvent(eventId);}
    public void generateTicketsForEvent(Event event, double standardPrice, double vipPrice) {
        ticketController.generateTicketsForEvent(event, standardPrice, vipPrice);}
    public void getAvailableTicketsForEvent(Event event) {
        ticketController.getAvailableTicketsForEvent(event);
    }
    public void reserveTicket(Ticket ticket, String purchaserName) {
        ticketController.reserveTicket(ticket, purchaserName);}
    public void releaseTicket(Ticket ticket) {
        ticketController.releaseTicket(ticket);
    }
    public Ticket getTicketById(int ticketId) {
        return ticketController.getTicketById(ticketId);
    }
    public void deleteTicket(int ticketId) {
        ticketController.deleteTicket(ticketId);
    }
    public void calculateTotalPrice(List<Ticket> tickets) {
        ticketController.calculateTotalPrice(tickets);
    }

    // Shopping Cart related
    public void addTicketToCart(Ticket ticket) {
        shoppingCartController.addTicketToCart(ticket);
    }

    public void removeTicketFromCart(Ticket ticket) {
        shoppingCartController.removeTicketFromCart(ticket);
    }

    public void clearCart() {
        shoppingCartController.clearCart();
    }

    public Order checkout() {
       return shoppingCartController.checkout();
    }

    public void updateTotalPrice() {
        shoppingCartController.updateTotalPrice();
    }

    public double getTotalPrice() {
        return shoppingCartController.getTotalPrice();
    }


    // Order related
    public void createOrder(Customer customer) {
        orderController.createOrder(customer);
    }
    public void processOrderPayment(Order order, String cardNumber, int cvv, String cardOwner, String expirationDate) {
        orderController.processOrderPayment(order, cardNumber, cvv, cardOwner, expirationDate);
    }
    public void cancelOrder(int orderId) {
        orderController.cancelOrder(orderId);
    }
    public void getOrderHistory(Customer customer) {
        orderController.getOrderHistory(customer);
    }
    public void getOrderById(int orderId) {
        orderController.getOrderById(orderId);
    }
    public void orderAllTicketsFromCart(Customer customer) {
        orderController.orderAllTicketsFromCart(customer);
    }
    public Order orderTicketsForEvent(Customer customer, Event event) {
        return orderTicketsForEvent(customer, event);
    }


    // Event related
    public  Event findEventByID(int eventId) {
        return eventController.findEventByID(eventId);}
    public List<Event> getEventsByLocation(String locationOrVenueName) {
        return eventController.getEventsByLocation(locationOrVenueName);}
    public List<Event> getUpcomingEventsForArtist(int artistID) {
        return eventController.getUpcomingEventsForArtist(artistID);}
    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
        return eventController.getUpcomingEventsForAthlete(athleteID);}
    public void createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Artist> artists) {
        eventController.createConcert(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, artists);}
    public void createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Athlete> athletes) {
        eventController.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, athletes);}
    public void updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime,EventStatus newStatus) {
        eventController.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);}
    public void deleteEvent(int eventId) {
        eventController.deleteEvent(eventId);}
    public List<Event> getAllEvents() {
        return eventController.getAllEvents();}
    public boolean isEventSoldOut(Event event) {
        return eventController.isEventSoldOut(event);}

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