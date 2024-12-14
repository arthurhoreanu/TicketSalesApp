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

    public int getLastCreatedEventID() {
        return eventService.getLastCreatedEventID();
    }

    /**
     * Creates a new Concert event.
     * Displays a message indicating whether the creation was successful or not.
     * @param eventName The name of the event.
     * @param eventDescription The description of the event.
     * @param startDateTime The start date and time of the event.
     * @param endDateTime The end date and time of the event.
     * @param venueID The venue where the event will take place.
     * @param eventStatus The status of the event (e.g., Scheduled, Postponed).
     */
    public void createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        boolean success = eventService.createConcert(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
        if (success) {
            System.out.println("Event created successfully");
        } else {
            System.out.println("Event creation failed");
        }
    }

    /**
     * Creates a new SportsEvent.
     * Displays a message indicating whether the creation was successful or not.
     * @param eventName The name of the event.
     * @param eventDescription The description of the event.
     * @param startDateTime The start date and time of the event.
     * @param endDateTime The end date and time of the event.
     * @param venueID The venue where the event will take place.
     * @param eventStatus The status of the event.
     */
    public void createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        boolean success = eventService.createSportsEvent(eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
        if (success) {
            System.out.println("Event created successfully");
        } else {
            System.out.println("Event creation failed");
        }
    }

    /**
     * Updates an existing event.
     * Displays a message indicating whether the update was successful or not.
     * @param eventId The ID of the event to be updated.
     * @param newName The new name for the event.
     * @param newDescription The new description for the event.
     * @param newStartDateTime The new start date and time for the event.
     * @param newEndDateTime The new end date and time for the event.
     * @param newStatus The new status of the event.
     */
    public void updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        boolean success = eventService.updateEvent(eventId, newName, newDescription, newStartDateTime, newEndDateTime, newStatus);
        if (success) {
            System.out.println("Event updated successfully");
        } else {
            System.out.println("Event update failed");
        }
    }

    /**
     * Deletes an event by its ID.
     * Displays a message indicating whether the deletion was successful or not.
     * @param eventId The ID of the event to be deleted.
     */
    public void deleteEvent(int eventId) {
        boolean success = eventService.deleteEvent(eventId);
        if (success) {
            System.out.println("Event deleted successfully");
        } else {
            System.out.println("Event deletion failed");
        }
    }

    /**
     * Retrieves a list of all events.
     * @return A list of all events.
     */
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Checks if an event is sold out by checking available tickets.
     * @param event The event to be checked.
     * @return true if the event is sold out, otherwise false.
     */
//    public boolean isEventSoldOut(Event event) {
//        return eventService.isEventSoldOut(event);
//    }

    /**
     * Retrieves events taking place at a specified venue.
     * @param venue The venue to retrieve events for.
     * @return A list of events taking place at the specified venue.
     */
//    public List<Event> getEventsByVenue(Venue venue) {
//        return eventService.getEventsByVenue(venue);
//    }

    /**
     * Retrieves upcoming events for a specific artist.
     * @param artistID The ID of the artist.
     * @return A list of upcoming events featuring the specified artist.
     */
    public List<Event> getUpcomingEventsForArtist(int artistID) {
        return eventService.getUpcomingEventsForArtist(artistID);
    }

    /**
     * Retrieves upcoming events for a specific athlete.
     * @param athleteID The ID of the athlete.
     * @return A list of upcoming events featuring the specified athlete.
     */
    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
        return eventService.getUpcomingEventsForAthlete(athleteID);
    }

    /**
     * Retrieves events by location or venue name.
     * Filters results to include only upcoming events.
     * @param locationOrVenueName The name of the location or venue.
     * @return A list of upcoming events at the specified location or venue.
     */
//    public List<Event> getEventsByLocation(String locationOrVenueName) {
//        return eventService.getEventsByLocation(locationOrVenueName);
//    }

    /**
     * Finds an event by its ID.
     * @param eventId The ID of the event to find.
     * @return The event with the specified ID, or null if not found.
     */
    public Event findEventByID(int eventId) {
        return eventService.findEventByID(eventId);
    }

    public boolean addArtistToConcert(int eventId, int artistId) {
        return eventService.addArtistToConcert(eventId, artistId);
    }

    public boolean addAthleteToSportsEvent(int eventId, int athleteId) {
        return eventService.addAthleteToSportsEvent(eventId, athleteId);
    }
}
