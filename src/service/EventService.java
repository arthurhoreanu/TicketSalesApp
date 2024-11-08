package service;

import model.*;
import repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;

public class EventService {
    private final IRepository<Event> eventRepository;
    //private final VenueService venueService;

    public EventService(IRepository<Event> eventRepository
                        //VenueService venueService
                        ) {
        this.eventRepository = eventRepository;
        //this.venueService = venueService;
    }

    // Method to add a Concert event
    // Arthur's TODO do we keep tickets here?
    public boolean createConcert(int eventId, String eventName, String eventDescription, LocalDateTime startDateTime,
                                 LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, Artist artist,
                                 String genre) {
        eventId = eventRepository.getAll().size() + 1;
        Concert concert = new Concert(eventId, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, artist, genre);
        eventRepository.create(concert);
        System.out.println("Concert added successfully.");
        return true;
    }

    // Method to add a SportsEvent event
    public boolean createSportsEvent(int eventId, String eventName, String eventDescription, LocalDateTime startDateTime,
                                     LocalDateTime endDateTime, Venue venue, EventStatus eventStatus, List<Ticket> tickets, List<Athlete> athletes,
                                     String sportName) {
        eventId = eventRepository.getAll().size() + 1;
        SportsEvent sportsEvent = new SportsEvent(eventId, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, athletes, sportName);
        eventRepository.create(sportsEvent);
        System.out.println("Sports event added successfully.");
        return true;
    }

    // Updates an existing event by ID
    public boolean updateEvent(int eventId, String newName, String newDescription, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        Event event = findEventById(eventId);
        if (event != null) {
            event.setEventName(newName);
            event.setEventDescription(newDescription);
            event.setStartDateTime(newStartDateTime);
            event.setEndDateTime(newEndDateTime);
            event.setEventStatus(newStatus);
            eventRepository.update(event);
            System.out.println("Event updated successfully.");
            return true;
        } else {
            System.out.println("Event not found.");
            return false;
        }
    }

    // Deletes an event by ID
    public boolean deleteEvent(int eventId) {
        Event event = findEventById(eventId);
        if (event != null) {
            eventRepository.delete(eventId);
            System.out.println("Event deleted successfully.");
            return true;
        } else {
            System.out.println("Event not found.");
            return false;
        }
    }

    // Retrieves a list of all events
    public List<Event> getAllEvents() {
        return eventRepository.getAll();
    }

    // Retrieves an event by its ID
    private Event findEventById(int eventId) {
        for (Event event : eventRepository.getAll()) {
            if (event.getID() == eventId) {
                return event;
            }
        }
        return null;
    }

    // Checks if an event is sold out by calculating available tickets
//    public boolean isEventSoldOut(Event event) {
//        int availableTickets = getAvailableTickets(event);
//        return availableTickets == 0;
//    }

    // Gets the number of available tickets for an event
//    public int getAvailableTickets(Event event) {
//        Venue venue = event.getVenue();
//        return venueService.getAvailableSeats(venue, event);
//    }

    // Retrieves all events happening at a particular venue
    public List<Event> getEventsByVenue(Venue venue) {
        return eventRepository.getAll().stream()
                .filter(event -> event.getVenue().equals(venue))
                .toList();
    }
}
