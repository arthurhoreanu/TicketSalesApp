package controller;

import model.*;

import java.time.LocalDateTime;
import java.util.List;

public class Controller {
    private final AccountController accountController;
    private final EventController eventController;

    public Controller(AccountController accountController, EventController eventController) {
        this.accountController = accountController;
        this.eventController = eventController;
    }

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

}
