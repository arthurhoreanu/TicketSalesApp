package service;

import model.*; // Import everything from the model layer
import repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class EventService {
    private final IRepository<Event> eventRepository;
    private final VenueService venueService;

    public EventService(IRepository<Event> eventRepository, VenueService venueService) {
        this.eventRepository = eventRepository;
        this.venueService = venueService;
    }

    // Method to add a Concert event
    public boolean createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Artist> artists, String genre) {
        int eventID = eventRepository.getAll().size() + 1;
        Concert concert = new Concert(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, artists, genre);
        eventRepository.create(concert);
        return true;
    }

    // Method to add a SportsEvent event
    public boolean createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Athlete> athletes, String sportName) {
        int eventID = eventRepository.getAll().size() + 1;
        SportsEvent sportsEvent = new SportsEvent(eventID, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, athletes, sportName);
        eventRepository.create(sportsEvent);
        return true;
    }

    // Updates an existing event by ID
    public boolean updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        Event event = findEventByID(eventId);
        if (event != null) {
            event.setEventName(newName);
            event.setEventDescription(newDescription);
            event.setStartDateTime(newStartDateTime);
            event.setEndDateTime(newEndDateTime);
            event.setEventStatus(newStatus);
            eventRepository.update(event);
            return true;
        } else {
            return false;
        }
    }

    // Deletes an event by ID
    public boolean deleteEvent(int eventId) {
        Event event = findEventByID(eventId);
        if (event != null) {
            eventRepository.delete(eventId);
            return true;
        } else {
            return false;
        }
    }

    // Retrieves a list of all events
    public List<Event> getAllEvents() {
        return eventRepository.getAll();
    }

    // Retrieves an event by its ID
    private Event findEventByID(int eventId) {
        for (Event event : eventRepository.getAll()) {
            if (event.getID() == eventId) {
                return event;
            }
        }
        return null;
    }

    //     Checks if an event is sold out by calculating available tickets
    public boolean isEventSoldOut(Event event) {
        int availableTickets = getAvailableTickets(event);
        return availableTickets == 0;
    }

    // Gets the number of available tickets for an event
    public int getAvailableTickets(Event event) {
        Venue venue = event.getVenue();
        return venueService.getAvailableSeats(venue, event);
    }

     // Lambda for this function
    /*// Retrieves all events happening at a particular venue
    public List<Event> getEventsByVenue(Venue venue) {
        return eventRepository.getAll().stream().filter(event -> event.getVenue().equals(venue)).toList();
    }*/

    public List<Event> getEventsByVenue(Venue venue) {
        List<Event> eventsAtVenue = new ArrayList<>();
        List<Event> allEvents = eventRepository.getAll();

        for (int i = 0; i < allEvents.size(); i++) {
            Event event = allEvents.get(i);
            if (event.getVenue().equals(venue)) {
                eventsAtVenue.add(event);
            }
        }

        return eventsAtVenue;
    }

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

    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
        List<Event> allEvents = getAllEvents(); // Retrieve all events from EventService
        List<Event> upcomingEvents = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        for (Event event : allEvents) {
            if (event.getStartDateTime().isAfter(now) && event instanceof SportsEvent sportsEvent) { // Check if event is a Concert and is upcoming
                for (Athlete athlete : sportsEvent.getAthletes()) {
                    if (athlete.getID() == athleteID) { // Check if artist is in this event's artist list
                        upcomingEvents.add(event);
                        break; // Break inner loop if artist is found in this event
                    }
                }
            }
        }
        return upcomingEvents;
    }

    public List<Event> getEventsByLocation(String locationOrVenueName) {
        // Get all venues that match either the venue name or location
        List<Venue> matchingVenues = venueService.getVenuesByLocationOrName(locationOrVenueName);

        // Collect events that are scheduled at any of the matching venues and are upcoming
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
