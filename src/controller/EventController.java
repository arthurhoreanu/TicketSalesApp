package controller;

import model.*;
import service.EventService;

import java.time.LocalDateTime;
import java.util.List;

public class EventController {
    private final EventService eventService;

    /**
     * Constructor for EventController.
     * @param eventService The instance of EventService used for event-related operations.
     */
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Creates a new Concert event.
     * @return true if the event was successfully created, false otherwise.
     */
    public Concert createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        return eventService.createConcert(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

    /**
     * Creates a new SportsEvent.
     * @return true if the event was successfully created, false otherwise.
     */
    public SportsEvent createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        return eventService.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

    /**
     * Updates an existing event.
     * @return true if the event was successfully updated, false otherwise.
     */
    public boolean updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        boolean success = eventService.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);
        if (success) {
            System.out.println("Event updated successfully.");
        } else {
            System.out.println("Failed to update event. Event ID not found.");
        }
        return success;
    }

    /**
     * Deletes an event by its ID.
     * @return true if the event was successfully deleted, false otherwise.
     */
    public boolean deleteEvent(int eventId) {
        boolean success = eventService.deleteEvent(eventId);
        if (success) {
            System.out.println("Event deleted successfully.");
        } else {
            System.out.println("Failed to delete event. Event ID not found.");
        }
        return success;
    }

    /**
     * Retrieves a list of all events.
     * @return A list of all events.
     */
    public List<Event> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        if (!events.isEmpty()) {
            System.out.println("All Events:");
            events.forEach(System.out::println);
        } else {
            System.out.println("No events found.");
        }
        return events;
    }

    /**
     * Retrieves upcoming events for a specific artist.
     * @return A list of upcoming events featuring the specified artist.
     */
    public List<Event> getUpcomingEventsForArtist(int artistID) {
        List<Event> events = eventService.getUpcomingEventsForArtist(artistID);
        if (!events.isEmpty()) {
            System.out.println("Upcoming Events for Artist ID " + artistID + ":");
            events.forEach(System.out::println);
        } else {
            System.out.println("No upcoming events found for Artist ID " + artistID + ".");
        }
        return events;
    }

    /**
     * Retrieves upcoming events for a specific athlete.
     * @return A list of upcoming events featuring the specified athlete.
     */
    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
        List<Event> events = eventService.getUpcomingEventsForAthlete(athleteID);
        if (!events.isEmpty()) {
            System.out.println("Upcoming Events for Athlete ID " + athleteID + ":");
            events.forEach(System.out::println);
        } else {
            System.out.println("No upcoming events found for Athlete ID " + athleteID + ".");
        }
        return events;
    }

    /**
     * Retrieves events by location or venue name.
     * Filters results to include only upcoming events.
     * @return A list of upcoming events at the specified location or venue.
     */
    public List<Event> getEventsByLocation(String locationOrVenueName) {
        List<Event> events = eventService.getEventsByLocation(locationOrVenueName);
        if (!events.isEmpty()) {
            System.out.println("Upcoming Events at location/venue '" + locationOrVenueName + "':");
            events.forEach(System.out::println);
        } else {
            System.out.println("No upcoming events found at location/venue '" + locationOrVenueName + "'.");
        }
        return events;
    }

    /**
     * Finds an event by its ID.
     * @return The event with the specified ID, or null if not found.
     */
    public Event findEventByID(int eventId) {
        Event event = eventService.findEventByID(eventId);
        if (event != null) {
            System.out.println("Event Found: " + event);
        } else {
            System.out.println("Event with ID " + eventId + " not found.");
        }
        return event;
    }

    /**
     * Adds an artist to a concert.
     * @return true if the artist was successfully added, false otherwise.
     */
    public boolean addArtistToConcert(int eventId, int artistId) {
        boolean success = eventService.addArtistToConcert(eventId, artistId);
        if (success) {
            System.out.println("Artist with ID " + artistId + " added to Concert with ID " + eventId + " successfully.");
        } else {
            System.out.println("Failed to add Artist with ID " + artistId + " to Concert with ID " + eventId + ".");
        }
        return success;
    }

    /**
     * Adds an athlete to a sports event.
     * @return true if the athlete was successfully added, false otherwise.
     */
    public boolean addAthleteToSportsEvent(int eventId, int athleteId) {
        boolean success = eventService.addAthleteToSportsEvent(eventId, athleteId);
        if (success) {
            System.out.println("Athlete with ID " + athleteId + " added to Sports Event with ID " + eventId + " successfully.");
        } else {
            System.out.println("Failed to add Athlete with ID " + athleteId + " to Sports Event with ID " + eventId + ".");
        }
        return success;
    }
}
