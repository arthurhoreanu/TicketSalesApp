package controller;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Controller(AccountController accountController, EventController eventController, VenueController venueController,
                      SectionController sectionController, SeatController seatController, ArtistController artistController,
                      AthleteController athleteController, CustomerController customerController) {
        this.accountController = accountController;
        this.eventController = eventController;
        this.venueController = venueController;
        this.sectionController = sectionController;
        this.seatController = seatController;
        this.artistController = artistController;
        this.athleteController = athleteController;
        this.customerController = customerController;
    }

    // Rule: newest first
    // Customer related
    public void addFavorite(FavouriteItem item) {
        customerController.addFavorite(item);}
    public void removeFavorite(FavouriteItem item) {
        customerController.removeFavorite(item);}
    public Set<FavouriteItem> getFavorites() {
        return customerController.getFavorites();}

    //Athlete related
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
    public void addSeat(Seat seat) {
        seatController.addSeat(seat);}
    public List<Seat> getAllSeats() {
        return seatController.getAllSeats();}
    public void updateSeat(Seat updatedSeat) {
        seatController.updateSeat(updatedSeat);}
    public boolean deleteSeat(int seatID) {
        return seatController.deleteSeat(seatID);}
    public boolean isSeatReservedForEvent(Seat seat, Event event) {
        return seatController.isSeatReservedForEvent(seat, event);}
    public void reserveSeatForEvent(Seat seat, Event event) {
        seatController.reserveSeatForEvent(seat, event);}
    public void clearSeatReservationForEvent(Seat seat, Event event) {
        seatController.clearSeatReservationForEvent(seat, event);}
    public void clearAllReservations(Seat seat) {
        seatController.clearAllReservations(seat);}
    public List<Seat> getAvailableSeats(Venue venue, Event event) {
        return seatController.getAvailableSeats(venue, event);}

    // Section related
    public List<Seat> getAvailableSeats(Section section, Event event) {
        return sectionController.getAvailableSeats(section, event);}
    public int countAvailableSeats(Section section, Event event) {
        return sectionController.countAvailableSeats(section, event);}
    public void addSeat(Section section, Seat seat) {
        sectionController.addSeat(section, seat);}
    public boolean removeSeat(Section section, Seat seat) {
        return sectionController.removeSeat(section, seat);}
    public boolean isSectionFull(Section section, Event event) {
        return sectionController.isSectionFull(section, event);}
    public void reserveSeats(Section section, List<Seat> seatsToReserve, Event event) {
        sectionController.reserveSeats(section, seatsToReserve, event);}
    public void releaseAllSeats(Section section, Event event) {
        sectionController.releaseAllSeats(section, event);}
    public List<Seat> getReservedSeats(Section section, Event event) {
        return sectionController.getReservedSeats(section, event);}
    public List<Seat> getRecommendedSeats(Section section, Event event) {
        return sectionController.getRecommendedSeats(section, event);}

    // Venue related
    public void addVenue(String name, String location, int capacity, List<Section> sections) {
        venueController.addVenue(name, location, capacity, sections);}
    public void updateVenue(int id, String name, String location, int capacity, List<Section> sections) {
        venueController.updateVenue(id, name, location, capacity, sections);}
    public void deleteVenue(int id) {
        venueController.deleteVenue(id);
    }
    public List<Venue> getAllVenues() {
        return venueController.getAllVenues();
    }
    public Venue findVenueByName(String name) {
        return venueController.findVenueByName(name);
    }

    // Event related
    public List<Event> getUpcomingEventsForArtist(int artistID) {
        return eventController.getUpcomingEventsForArtist(artistID);}
    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
        return eventController.getUpcomingEventsForAthlete(athleteID);}
    public void createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Artist> artists, String genre) {
        eventController.createConcert(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, artists, genre);}
    public void createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Athlete> athletes, String sportName) {
        eventController.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, athletes, sportName);}
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