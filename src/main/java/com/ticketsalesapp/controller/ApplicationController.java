package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.model.*;
import main.java.com.ticketsalesapp.model.event.*;
import main.java.com.ticketsalesapp.model.ticket.Cart;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.model.ticket.TicketType;
import main.java.com.ticketsalesapp.model.user.Admin;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.venue.Row;
import main.java.com.ticketsalesapp.model.venue.Seat;
import main.java.com.ticketsalesapp.model.venue.Section;
import main.java.com.ticketsalesapp.model.venue.Venue;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Main application controller that serves as a facade for all specific controllers.
 * This design was chosen because:
 * - It provides a single point of access for the UI layer
 * - Keeps the specific controllers focused on their domain
 * - Makes it easy to maintain and extend functionality
 */

@Component
public class ApplicationController {
    private final ArtistController artistController;
    private final AthleteController athleteController;
    private final VenueController venueController;
    private final TicketController ticketController;
    private final CartController cartController;
    private final CustomerController customerController;
    private final ConcertController concertController;
    private final AdminController adminController;
    private final SportsEventController sportsEventController;

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
     * @param concertController The controller responsible for managing events.
     * @param adminController The controller responsible for managing user accounts.
     */
    public ApplicationController(ArtistController artistController, AthleteController athleteController,
                                 VenueController venueController, TicketController ticketController,
                                 CartController cartController, CustomerController customerController,
                                 ConcertController concertController, AdminController adminController, SportsEventController sportsEventController) {
        this.artistController = artistController;
        this.athleteController = athleteController;
        this.venueController = venueController;
        this.ticketController = ticketController;
        this.cartController = cartController;
        this.customerController = customerController;
        this.concertController = concertController;
        this.adminController = adminController;
        this.sportsEventController = sportsEventController;
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
    public Optional<Seat> findSeatByID(int seatId) {
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
    public Optional<Row> updateRow(int rowId, int rowCapacity) {
        return venueController.updateRow(rowId, rowCapacity);}
    public void deleteRow(int rowId) {
        venueController.deleteRow(rowId);}
    public void deleteRowsBySection(int sectionId) {
        venueController.deleteRowsBySection(sectionId);}
    public Optional<Row> findRowByID(int rowId) {
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
    public Seat recommendClosestSeat(int sectionId, int rowId, List<Integer> selectedSeatNumbers) {
        return venueController.recommendClosestSeat(sectionId, rowId, selectedSeatNumbers);}

    /// Section
    public Section createSection(Venue venue, int sectionCapacity, String sectionName) {
        return venueController.createSection(venue, sectionCapacity, sectionName);}
    public Section updateSection(int sectionId, String sectionName, int sectionCapacity) {
        return venueController.updateSection(sectionId, sectionName, sectionCapacity);}
    public void deleteSection(int sectionId) {
        venueController.deleteSection(sectionId);}
    public Optional<Section> findSectionByID(int sectionId) {
        return venueController.findSectionByID(sectionId);}
    public List<Section> getAllSections() {
        return venueController.getAllSections();}
    public List<Section> findSectionsByName(String name) {
        return venueController.findSectionsByName(name);}
    public List<Seat> getAvailableSeatsInSection(int sectionId, int eventId) {
        return venueController.getAvailableSeatsInSection(sectionId, eventId);}
    public void loadSectionsForVenue(Venue venue){
        venueController.loadSectionsForVenue(venue);}

    /// Venue
    public Venue createVenue(String name, String location, int capacity, boolean hasSeats) {
        return venueController.createVenue(name, location, capacity, hasSeats);}
    public Optional<Venue> findVenueByID(int venueId) {
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
    public Optional<Ticket> findTicketByID(int ticketID) {
        return ticketController.findTicketByID(ticketID);}
    public double calculateTotalPrice(List<Ticket> tickets) {
        return ticketController.calculateTotalPrice(tickets);}
    public List<String> getTicketAvailabilityByType(Event event) {
        return ticketController.getTicketAvailabilityByType(event);}
    public double getBasePriceForEvent(int eventId) {
        return concertController.getBasePriceForEvent(eventId);}
    public List<Ticket> getAvailableTicketsByType(Event event, TicketType ticketType) {
        return ticketController.getAvailableTicketsByType(event, ticketType);}
    public List<Ticket> getTicketsByCustomer(Customer customer) {
        return ticketController.getTicketsByCustomer(customer);}

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
    public Optional<Cart> findCartByID(int cartID) {
        return cartController.findCartByID(cartID);}
    public void processPayment(Cart cart, String cardNumber, String cardholderName, int expiryMonth, int expiryYear, String cvv) {
        cartController.processPayment(cart, cardNumber, cardholderName, expiryMonth, expiryYear, cvv);}

    // 6. Customer
    public void customerLogin(String username, String password) {
        customerController.login(username, password);}
    public void customerLogout() {
        customerController.logout();}
    public boolean createCustomer(String username, String email, String password) {
        return customerController.createCustomer(username, email, password);}
    public Customer getCurrentCustomer() {
        return customerController.getCurrentCustomer();}
    public Customer findCustomerById(int id) {
        return customerController.findCustomerById(id);}
    public void addFavourite(FavouriteEntity item) {
        customerController.addFavourite(item);}
    public void removeFavourite(FavouriteEntity item) {
        customerController.removeFavourite(item);}
    public Set<FavouriteEntity> getFavourites() {
        return customerController.getFavourites();}


    // 7. Concert
    public boolean addArtistToConcert(int eventId, int artistId) {
        return concertController.addArtistToConcert(eventId, artistId);}
    public Concert findConcertByID(int eventId) {
        return concertController.findConcertByID(eventId);}
//    public List<Event> getEventsByLocation(String locationOrVenueName) {
//        return concertController.getEventsByLocation(locationOrVenueName);}
//    public List<Event> getUpcomingEventsForArtist(int artistID) {
//        return concertController.getUpcomingEventsForArtist(artistID);}
//    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
//        return concertController.getUpcomingEventsForAthlete(athleteID);}
    public Concert createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        return concertController.createConcert(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);}
    public void updateConcert(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        concertController.updateConcert(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);}
    public void deleteConcert(int eventId) {
        concertController.deleteConcert(eventId);}
    public List<Concert> getAllConcerts() {
        return concertController.getAllConcerts();}
    public void removeArtistFromConcert(int eventID, int artistID) {
        concertController.removeArtistFromConcert(eventID, artistID);}
    public List<Artist> getArtistsByConcert(int concertID) {
        return concertController.getArtistsForConcert(concertID);}

    // 8. SportsEvent
    public boolean addAthleteToSportsEvent(int eventId, int athleteId) {
        return sportsEventController.addAthleteToEvent(eventId, athleteId);}
    public List<Athlete> getAthletesBySportsEvent(int sportsEventID) {
        return sportsEventController.getAthletesForEvent(sportsEventID);}
    public void removeAthleteFromSportsEvent(int eventID, int athleteID) {
        sportsEventController.removeAthleteFromEvent(eventID, athleteID);}
    public Optional<SportsEvent> findSportsEventByID(int sportsEventID) {
        return sportsEventController.findSportsEventByID(sportsEventID);}
    public boolean createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        return sportsEventController.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);}


    // 9. Admin
    public boolean adminLogin(String username, String password) {
        return adminController.login(username, password);}
    public boolean adminLogout() {
        return adminController.logout();}
    public boolean createAdmin(String username, String email, String password) {
        return adminController.createAdmin(username, email, password);}
    public Admin getCurrentAdmin() {
        return adminController.getCurrentAdmin();}
    public Admin findAdminById(int id) {
        return adminController.findAdminById(id);}

}