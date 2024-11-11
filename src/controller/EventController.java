package controller;

import model.*;
import service.EventService;

import java.time.LocalDateTime;
import java.util.List;

public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    public void createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Artist> artists, String genre) {
        boolean success = eventService.createConcert(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, artists, genre);
        if (success) {
            System.out.println("Event created successfully");
        } else {
            System.out.println("Event creation failed");
        }
    }

    public void createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Athlete> athletes, String sportName) {
        boolean success = eventService.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, athletes, sportName);
        if (success) {
            System.out.println("Event created successfully");
        } else {
            System.out.println("Event creation failed");
        }
    }

    public void updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        boolean success = eventService.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);
        if (success) {
            System.out.println("Event updated successfully");
        } else {
            System.out.println("Event update failed");
        }
    }

    public void deleteEvent(int eventId) {
        boolean success = eventService.deleteEvent(eventId);
        if (success) {
            System.out.println("Event deleted successfully");
        } else {
            System.out.println("Event deletion failed");
        }
    }

    public List<Event> getAllEvents() {return eventService.getAllEvents();
    }

    public boolean isEventSoldOut(Event event) {
        return eventService.isEventSoldOut(event);
    }

    public int getAvailableTickets(Event event) {
        return eventService.getAvailableTickets(event);
    }

    public List<Event> getEventsByVenue(Venue venue) {
        return eventService.getEventsByVenue(venue);
    }

    public List<Event> getUpcomingEventsForArtist(int artistID) { return eventService.getUpcomingEventsForArtist(artistID);}

    public List<Event> getUpcomingEventsForAthlete(int athleteID) { return eventService.getUpcomingEventsForAthlete(athleteID);}

    public List<Event> getEventsByLocation(String locationOrVenueName) {
        return eventService.getEventsByLocation(locationOrVenueName);}
}
