package service;

import controller.Controller;
import model.*;
import repository.DBRepository;
import repository.FileRepository;
import repository.IRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class EventService {
    private final IRepository<Event> eventRepository;
    private final VenueService venueService;
    private final FileRepository<Concert> concertFileRepository;
    private final FileRepository<SportsEvent> sportsEventFileRepository;
    private final DBRepository<Concert> concertDatabaseRepository;
    private final DBRepository<SportsEvent> sportsEventDatabaseRepository;
    private int lastCreatedEventID;
    static Controller controller = ControllerProvider.getController();

    public EventService(IRepository<Event> eventRepository, VenueService venueService) {
        this.eventRepository = eventRepository;
        this.venueService = venueService;
        this.concertFileRepository = new FileRepository<>("src/repository/data/concerts.csv", Concert::fromCsv);
        this.sportsEventFileRepository = new FileRepository<>("src/repository/data/sportsevents.csv", SportsEvent::fromCsv);
        syncFromCsv();
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.concertDatabaseRepository = new DBRepository<>(entityManagerFactory, Concert.class);
        this.sportsEventDatabaseRepository = new DBRepository<>(entityManagerFactory, SportsEvent.class);
        syncFromDatabase();
    }

    private void syncFromCsv() {
        List<Concert> concerts = concertFileRepository.getAll();
        List<SportsEvent> sportsEvents = sportsEventFileRepository.getAll();
        for (Concert concert : concerts) {
            eventRepository.create(concert);
        }
        for (SportsEvent sportsEvent : sportsEvents) {
            eventRepository.create(sportsEvent);
        }
    }

    private void syncFromDatabase() {
        List<Concert> concerts = concertDatabaseRepository.getAll();
        List<SportsEvent> sportsEvents = sportsEventDatabaseRepository.getAll();
        for (Concert concert : concerts) {
            eventRepository.create(concert);
        }
        for (SportsEvent sportsEvent : sportsEvents) {
            eventRepository.create(sportsEvent);
        }
    }

    public int getLastCreatedEventID() {
        return lastCreatedEventID;
    }

    /**
     * Creates and adds a new concert event to the repository.
     * @param eventName The name of the concert event.
     * @param eventDescription A description of the concert event.
     * @param startDateTime The start date and time of the event.
     * @param endDateTime The end date and time of the event.
     * @param venueID The venue where the concert will take place.
     * @param eventStatus The status of the event (e.g., upcoming, completed).
     * @return true if the concert event was successfully created and added.
     */
    public boolean createConcert(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        int eventID = eventRepository.getAll().size() + 1;
        Concert concert = new Concert(eventID, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
        eventRepository.create(concert);
        concertFileRepository.create(concert);
        concertDatabaseRepository.create(concert);
        this.lastCreatedEventID = eventID;
        return true;
    }

    /**
     * Creates and adds a new sports event to the repository.
     * @param eventName The name of the sports event.
     * @param eventDescription A description of the sports event.
     * @param startDateTime The start date and time of the event.
     * @param endDateTime The end date and time of the event.
     * @param venueID The venue where the sports event will take place.
     * @param eventStatus The status of the event (e.g., upcoming, completed).
     * @return true if the sports event was successfully created and added.
     */
    public boolean createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime, LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        int eventID = eventRepository.getAll().size() + 1;
        SportsEvent sportsEvent = new SportsEvent(eventID, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
        eventRepository.create(sportsEvent);
        sportsEventFileRepository.create(sportsEvent);
        sportsEventDatabaseRepository.create(sportsEvent);
        this.lastCreatedEventID = eventID;
        return true;
    }

    public boolean addArtistToConcert(int eventID, int artistID) {
        Concert concert = (Concert) findEventByID(eventID);
        if (concert != null) {
            Artist artist = new Artist();
            artist.setID(artistID);
            concert.getArtists().add(artist); // Hibernate gestionează automat `lineup`
            concertDatabaseRepository.update(concert);
            return true;
        }
        return false;
    }

    public boolean addAthleteToSportsEvent(int eventID, int athleteID) {
        SportsEvent sportsEvent = (SportsEvent) findEventByID(eventID);
        if (sportsEvent != null) {
            Athlete athlete = new Athlete();
            athlete.setID(athleteID);
            sportsEvent.getAthletes().add(athlete); // Hibernate gestionează automat `lineup`
            sportsEventDatabaseRepository.update(sportsEvent);
            return true;
        }
        return false;
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
            if(event instanceof Concert) {
                concertFileRepository.update((Concert) event);
                concertDatabaseRepository.update((Concert) event);
            }
            else if(event instanceof SportsEvent) {
                sportsEventFileRepository.update((SportsEvent) event);
                sportsEventDatabaseRepository.update((SportsEvent) event);
            }
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
            if(event instanceof Concert) {
                concertFileRepository.delete(eventId);
                concertDatabaseRepository.delete(eventId);
            }
            else if(event instanceof SportsEvent) {
                sportsEventFileRepository.delete(eventId);
                sportsEventDatabaseRepository.delete(eventId);
            }
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
    // TODO
//    public boolean isEventSoldOut(Event event) {
//        int availableTickets = getAvailableTickets(event);
//        return availableTickets == 0;
//    }

    /**
     * Gets the number of available tickets for an event.
     * @param event The event for which to calculate the number of available tickets.
     * @return The number of available tickets for the specified event.
     */
    // TODO
//    public int getAvailableTickets(Event event) {
//        Venue venue = event.getVenue();
//        return venueService.getAvailableSeats(venue, event);
//    }

    /**
     * Retrieves all upcoming events for a specific artist.
     * @param artistID The ID of the artist for whom to retrieve upcoming events.
     * @return A list of upcoming events featuring the specified artist.
     */
    public List<Event> getUpcomingEventsForArtist(int artistID) {
        LocalDateTime now = LocalDateTime.now();
        List<Event> upcomingEvents = new ArrayList<>();
        for (Concert concert : concertDatabaseRepository.getAll()) {
            if (concert.getArtists().stream().anyMatch(artist -> artist.getID() == artistID) && concert.getStartDateTime().isAfter(now)) {
                upcomingEvents.add(concert);
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
        LocalDateTime now = LocalDateTime.now();
        List<Event> upcomingEvents = new ArrayList<>();
        for (SportsEvent sportsEvent : sportsEventDatabaseRepository.getAll()) {
            if (sportsEvent.getAthletes().stream().anyMatch(athlete -> athlete.getID() == athleteID) && sportsEvent.getStartDateTime().isAfter(now)) {
                upcomingEvents.add(sportsEvent);
            }
        }
        return upcomingEvents;
    }

    /**
     * Retrieves all events that take place at a specific venue.
     * @param venueID The venue where the events are happening.
     * @return A list of events that take place at the specified venue.
     */
    public List<Event> getEventsByVenue(int venueID) {
        List<Event> eventsAtVenue = new ArrayList<>();
        List<Event> allEvents = eventRepository.getAll();
        for (Event event : allEvents) {
            if (event.getVenueID() == venueID) { // Comparăm venueID
                eventsAtVenue.add(event);
            }
        }

        return eventsAtVenue;
    }

    /**
     * Retrieves all upcoming events for venues located at a specific location or matching a specific venue name.
     * @param locationOrVenueName The location or venue name to search for.
     * @return A list of upcoming events scheduled at venues that match the location or venue name.
     */
    public List<Event> getEventsByLocation(String locationOrVenueName) {
        List<Venue> matchingVenues = venueService.findVenuesByLocationOrName(locationOrVenueName);
        List<Event> events = new ArrayList<>();
        for (Venue venue : matchingVenues) {
            int venueID = venue.getID();
            for (Event event : getEventsByVenue(venueID)) {
                if (event.getStartDateTime().isAfter(LocalDateTime.now())) {
                    events.add(event);
                }
            }
        }
        return events;
    }

}