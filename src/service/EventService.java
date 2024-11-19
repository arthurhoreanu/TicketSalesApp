package service;

import controller.Controller;
import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class EventService {
    private final IRepository<Event> eventRepository;
    private final VenueService venueService;
    private final FileRepository<Event> eventFileRepository;

    public EventService(IRepository<Event> eventRepository, VenueService venueService) {
        this.eventRepository = eventRepository;
        this.venueService = venueService;
        this.eventFileRepository = new FileRepository<>("src/repository/data/events.csv", Event::fromCsvFormat);
        List<Event> eventsFromFile = eventFileRepository.getAll();
        for (Event event : eventsFromFile) {
            eventRepository.create(event);
        }
    }

    /**
     * Creates and adds a new concert event to the repository.
     * @param eventName The name of the concert event.
     * @param eventDescription A description of the concert event.
     * @param startDateTime The start date and time of the event.
     * @param endDateTime The end date and time of the event.
     * @param venue The venue where the concert will take place.
     * @param eventStatus The status of the event (e.g., upcoming, completed).
     * @param artists The list of artists performing in the concert.
     * @return true if the concert event was successfully created and added.
     */
    public boolean createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Artist> artists) {
        int eventID = eventRepository.getAll().size() + 1;
        Concert concert = new Concert(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, artists);
        eventRepository.create(concert);
        eventFileRepository.create(concert);
        return true;
    }

    /**
     * Creates and adds a new sports event to the repository.
     * @param eventName The name of the sports event.
     * @param eventDescription A description of the sports event.
     * @param startDateTime The start date and time of the event.
     * @param endDateTime The end date and time of the event.
     * @param venue The venue where the sports event will take place.
     * @param eventStatus The status of the event (e.g., upcoming, completed).
     * @param athletes The list of athletes participating in the sports event.
     * @return true if the sports event was successfully created and added.
     */
    public boolean createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Athlete> athletes) {
        int eventID = eventRepository.getAll().size() + 1;
        SportsEvent sportsEvent = new SportsEvent(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, athletes);
        eventRepository.create(sportsEvent);
        eventFileRepository.create(sportsEvent);
        return true;
    }

    /**
     * Updates an existing event with new details.
     * @param eventId The ID of the event to be updated.
     * @param newName The new name for the event.
     * @param newDescription The new description for the event.
     * @param newStartDateTime The new start date and time for the event.
     * @param newEndDateTime The new end date and time for the event.
     * @param newStatus The new status for the event.
     * @return true if the event was successfully updated; false if the event was not found.
     */
    public boolean updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        Event event = findEventByID(eventId);
        if (event != null) {
            event.setEventName(newName);
            event.setEventDescription(newDescription);
            event.setStartDateTime(newStartDateTime);
            event.setEndDateTime(newEndDateTime);
            event.setEventStatus(newStatus);
            eventRepository.update(event);
            eventFileRepository.update(event);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes an event from the repository by its ID.
     * @param eventId The ID of the event to be deleted.
     * @return true if the event was successfully deleted; false if the event was not found.
     */
    public boolean deleteEvent(int eventId) {
        Event event = findEventByID(eventId);
        if (event != null) {
            eventRepository.delete(eventId);
            eventFileRepository.delete(eventId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves a list of all events from the repository.
     * @return A list of all events.
     */
    public List<Event> getAllEvents() {
        return eventRepository.getAll();
    }

    /**
     * Finds an event by its ID.
     * @param eventId The ID of the event to be retrieved.
     * @return The event with the specified ID, or null if not found.
     */
    public Event findEventByID(int eventId) {
        for (Event event : eventRepository.getAll()) {
            if (event.getID() == eventId) {
                return event;
            }
        }
        return null;
    }

    /**
     * Checks if an event is sold out by calculating available tickets.
     * @param event The event to be checked for sold-out status.
     * @return true if the event is sold out (i.e., no available tickets), false otherwise.
     */
    public boolean isEventSoldOut(Event event) {
        int availableTickets = getAvailableTickets(event);
        return availableTickets == 0;
    }

    /**
     * Gets the number of available tickets for an event.
     * @param event The event for which to calculate the number of available tickets.
     * @return The number of available tickets for the specified event.
     */
    public int getAvailableTickets(Event event) {
        Venue venue = event.getVenue();
        return venueService.getAvailableSeats(venue, event);
    }

    /**
     * Retrieves all events that take place at a specific venue.
     * @param venue The venue where the events are happening.
     * @return A list of events that take place at the specified venue.
     */
    public List<Event> getEventsByVenue(Venue venue) {
        List<Event> eventsAtVenue = new ArrayList<>();
        List<Event> allEvents = eventRepository.getAll();

        for (Event event : allEvents) {
            if (event.getVenue().equals(venue)) {
                eventsAtVenue.add(event);
            }
        }

        return eventsAtVenue;
    }

    /**
     * Retrieves all upcoming events for a specific artist.
     * @param artistID The ID of the artist for whom to retrieve upcoming events.
     * @return A list of upcoming events featuring the specified artist.
     */
    public List<Event> getUpcomingEventsForArtist(int artistID) {
        List<Event> allEvents = getAllEvents(); // Retrieve all events from EventService
        List<Event> upcomingEvents = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        for (Event event : allEvents) {
            if (event.getStartDateTime().isAfter(now) && event instanceof Concert concert) { // Check if event is a Concert and is upcoming
                for (Artist artist : concert.getArtists()) {
                    if (artist.getID() == artistID) { // Check if artist is in this event's artist list
                        upcomingEvents.add(event);
                        break; // Break inner loop if artist is found in this event
                    }
                }
            }
        }
        return upcomingEvents;
    }

    /**
     * Retrieves all upcoming events for a specific athlete.
     * @param athleteID The ID of the athlete for whom to retrieve upcoming events.
     * @return A list of upcoming events featuring the specified athlete.
     */
    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
        List<Event> allEvents = getAllEvents(); // Retrieve all events from EventService
        List<Event> upcomingEvents = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        for (Event event : allEvents) {
            if (event.getStartDateTime().isAfter(now) && event instanceof SportsEvent sportsEvent) { // Check if event is a SportsEvent and is upcoming
                for (Athlete athlete : sportsEvent.getAthletes()) {
                    if (athlete.getID() == athleteID) { // Check if athlete is in this event's athlete list
                        upcomingEvents.add(event);
                        break; // Break inner loop if athlete is found in this event
                    }
                }
            }
        }
        return upcomingEvents;
    }

    /**
     * Retrieves all upcoming events for venues located at a specific location or matching a specific venue name.
     * @param locationOrVenueName The location or venue name to search for.
     * @return A list of upcoming events scheduled at venues that match the location or venue name.
     */
    public List<Event> getEventsByLocation(String locationOrVenueName) {
        List<Venue> matchingVenues = venueService.getVenuesByLocationOrName(locationOrVenueName);

        List<Event> events = new ArrayList<>();
        for (Venue venue : matchingVenues) {
            for (Event event : getEventsByVenue(venue)) {
                if (event.getStartDateTime().isAfter(LocalDateTime.now())) {
                    events.add(event); // Only add upcoming events
                }
            }
        }
        return events;
    }
}
