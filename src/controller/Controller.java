package controller;

import model.*;

import java.time.LocalDateTime;
import java.util.List;

public class Controller {
    private final AccountController accountController;
    private final EventController eventController;
    private final VenueController venueController;
    private final SectionController sectionController;
    private final SeatController seatController;

    public Controller(AccountController accountController, EventController eventController, VenueController venueController,
    SectionController sectionController, SeatController seatController) {
        this.accountController = accountController;
        this.eventController = eventController;
        this.venueController = venueController;
        this.sectionController = sectionController;
        this.seatController = seatController;
    }

    // User related
    public User getCurrentUser() {
        return accountController.getCurrentUser();
    }
    public List<User> getAllUsers() {
        return accountController.getAllUsers();
    }
    public boolean isUsernameTaken(String username) {
        return accountController.isUsernameTaken(username);
    }
    public boolean domainEmail(String email) {
        return accountController.domainEmail(email);
    }
    public void createAccount(String role, String username, String email, String password) {
        accountController.createAccount(role, username, email, password);
    }
    public void login(String username, String password) {
        accountController.login(username, password);
    }
    public void logout() {
        accountController.logout();
    }
    public void deleteAccount(int id) {
        accountController.deleteAccount(id);
    }

    // Event related
    public void createConcert(int eventId, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, Artist artist, String genre) {
        eventController.createConcert(eventId, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, artist, genre);
    }
    public void createSportsEvent(int eventId, String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Athlete> athletes, String sportName) {
        eventController.createSportsEvent(eventId, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, athletes, sportName);
    }
    public void updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        eventController.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);
    }
    public void deleteEvent(int eventId) {
        eventController.deleteEvent(eventId);
    }
    public List<Event> getAllEvents() {
        return eventController.getAllEvents();
    }
    //    public boolean isEventSoldOut(Event event) { return eventController.isEventSoldOut(event);}
    public List<Event> getsEventByVenue(Venue venue) {
        return eventController.getEventsByVenue(venue);
    }

    // Section related
    public List<Seat> getAvailableSeats(Section section, Event event) { return sectionController.getAvailableSeats(section, event);}
    public int countAvailableSeats(Section section, Event event) {return sectionController.countAvailableSeats(section, event);}
    public void addSeat(Section section, Seat seat) {sectionController.addSeat(section, seat);}
    public boolean removeSeat(Section section, Seat seat) {return sectionController.removeSeat(section, seat);}
    public boolean isSectionFull(Section section, Event event) {return sectionController.isSectionFull(section, event);}
    public void reserveSeats(Section section, List<Seat> seatsToReserve, Event event) {sectionController.reserveSeats(section, seatsToReserve, event);}
    public void releaseAllSeats(Section section, Event event) {sectionController.releaseAllSeats(section, event);}
    public List<Seat> getReservedSeats(Section section, Event event) {return sectionController.getReservedSeats(section, event);}
    public List<Seat> getRecommendedSeats(Section section, Event event) {return sectionController.getRecommendedSeats(section, event);}

    // Seat related
    public void addSeat(Seat seat) {seatController.addSeat(seat);}
    public List<Seat> getAllSeats() {return seatController.getAllSeats();}
    public boolean updateSeat(Seat updatedSeat) {return seatController.updateSeat(updatedSeat);}
    public boolean deleteSeat(int seatID) {return seatController.deleteSeat(seatID);}
    public boolean isSeatReservedForEvent(Seat seat, Event event) {return seatController.isSeatReservedForEvent(seat, event);}
    public void reserveSeatForEvent(Seat seat, Event event) {seatController.reserveSeatForEvent(seat, event);}
    public void clearSeatReservationForEvent(Seat seat, Event event) {seatController.clearSeatReservationForEvent(seat, event);}
    public void clearAllReservations(Seat seat) {seatController.clearAllReservations(seat);}
    public List<Seat> getAvailableSeats(Venue venue, Event event) {return seatController.getAvailableSeats(venue, event);}
}
