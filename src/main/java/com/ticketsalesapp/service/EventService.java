package main.java.com.ticketsalesapp.service;

import main.java.com.ticketsalesapp.exception.EntityNotFoundException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.*;
import main.java.com.ticketsalesapp.model.venue.Venue;
import main.java.com.ticketsalesapp.repository.BaseRepository;
import main.java.com.ticketsalesapp.repository.factory.RepositoryFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing event-related operations including concerts and sports events.
 */
public class EventService {
    private final BaseRepository<Event> eventBaseRepository;
    private final BaseRepository<ConcertLineUp> concertLineUpBaseRepository;
    private final BaseRepository<SportsEventLineUp> sportsEventLineUpBaseRepository;
    private final VenueService venueService;
    private final ArtistService artistService;
    private final AthleteService athleteService;

    /**
     * Constructs an EventService with the specified repository factories and services.
     *
     * @param eventRepository             the repository factory for event management.
     * @param concertLineUpRepository     the repository factory for concert lineups.
     * @param sportsEventLineUpRepository the repository factory for sports event lineups.
     * @param venueService                the service for managing venues.
     * @param artistService               the service for managing artists.
     * @param athleteService              the service for managing athletes.
     */
    public EventService(RepositoryFactory eventRepository, RepositoryFactory concertLineUpRepository,
                        RepositoryFactory sportsEventLineUpRepository, VenueService venueService,
                        ArtistService artistService, AthleteService athleteService) {
        this.eventBaseRepository = eventRepository.createEventRepository();
        this.concertLineUpBaseRepository = concertLineUpRepository.createConcertLineUpRepository();
        this.sportsEventLineUpBaseRepository = sportsEventLineUpRepository.createSportsEventLineUpRepository();
        this.venueService = venueService;
        this.artistService = artistService;
        this.athleteService = athleteService;
    }

    // --- Concert Methods ---
    /**
     * Creates a new concert.
     *
     * @param eventName       the name of the concert.
     * @param eventDescription the description of the concert.
     * @param startDateTime    the start date and time of the concert.
     * @param endDateTime      the end date and time of the concert.
     * @param venueID          the ID of the venue where the concert will be held.
     * @param eventStatus      the status of the concert.
     * @return the created Concert object.
     * @throws ValidationException   if the concert name or times are invalid.
     * @throws EntityNotFoundException if the venue is not found.
     */
    public Concert createConcert(String eventName, String eventDescription, LocalDateTime startDateTime,
                                 LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        if (eventName == null || eventName.isEmpty()) {
            throw new ValidationException("Concert name cannot be null or empty.");
        }
        if (startDateTime == null || endDateTime == null || startDateTime.isAfter(endDateTime)) {
            throw new ValidationException("Invalid start or end time for the concert.");
        }
        Venue venue = venueService.findVenueByID(venueID);
        if (venue == null) {
            throw new EntityNotFoundException("Venue not found with ID: " + venueID);
        }
        Concert concert = new Concert(0, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
        eventBaseRepository.create(concert);
        return concert;
    }

    /**
     * Adds an artist to a concert.
     *
     * @param eventID  the ID of the concert.
     * @param artistID the ID of the artist to add.
     * @return true if the artist is successfully added, false otherwise.
     */
    public boolean addArtistToConcert(int eventID, int artistID) {
        Concert concert = (Concert) findEventByID(eventID);
        if (concert != null) {
            Artist artist = artistService.findArtistByID(artistID);
            if (artist == null) {
                System.out.println("Artist not found.");
                return false;
            }
            ConcertLineUp concertLineUp = new ConcertLineUp(concert, artist);
            concertLineUpBaseRepository.create(concertLineUp);
            return true;
        }
        return false;
    }

    /**
     * Removes an artist from a concert.
     *
     * @param eventID  the ID of the concert.
     * @param artistID the ID of the artist to remove.
     * @return true if the artist is successfully removed, false otherwise.
     */
    public boolean removeArtistFromConcert(int eventID, int artistID) {
        ConcertLineUp lineUp = concertLineUpBaseRepository.getAll().stream()
                .filter(lineUpEntry -> lineUpEntry.getConcert().getID() == eventID &&
                        lineUpEntry.getArtist().getID() == artistID)
                .findFirst()
                .orElse(null);
        if (lineUp != null) {
            concertLineUpBaseRepository.delete(lineUp.getID());
            return true;
        }
        return false;
    }

    /**
     * Finds a concert by its ID.
     *
     * @param concertID the ID of the concert.
     * @return the Concert object, or null if not found.
     */
    public Concert findConcertByID(int concertID) {
        return eventBaseRepository.getAll().stream()
                .filter(event -> event instanceof Concert && event.getID() == concertID)
                .map(event -> (Concert) event)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the list of artists associated with a specific concert.
     *
     * @param concertID the ID of the concert.
     * @return a list of Artist objects.
     */
    public List<Artist> getArtistsByConcert(int concertID) {
        return concertLineUpBaseRepository.getAll().stream()
                .filter(lineUp -> lineUp.getConcert().getID() == concertID)
                .map(ConcertLineUp::getArtist)
                .distinct()
                .toList();
    }

    // --- Sports Event Methods ---

    /**
     * Creates a new sports event.
     *
     * @param eventName       the name of the sports event.
     * @param eventDescription the description of the sports event.
     * @param startDateTime    the start date and time of the sports event.
     * @param endDateTime      the end date and time of the sports event.
     * @param venueID          the ID of the venue where the sports event will be held.
     * @param eventStatus      the status of the sports event.
     * @return the created SportsEvent object.
     * @throws ValidationException   if the sports event name or times are invalid.
     * @throws EntityNotFoundException if the venue is not found.
     */
    public SportsEvent createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime,
                                         LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        if (eventName == null || eventName.isEmpty()) {
            throw new ValidationException("Sports event name cannot be null or empty.");
        }
        if (startDateTime == null || endDateTime == null || startDateTime.isAfter(endDateTime)) {
            throw new ValidationException("Invalid start or end time for the sports event.");
        }
        Venue venue = venueService.findVenueByID(venueID);
        if (venue == null) {
            throw new EntityNotFoundException("Venue not found with ID: " + venueID);
        }
        SportsEvent sportsEvent = new SportsEvent(0, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
        eventBaseRepository.create(sportsEvent);
        return sportsEvent;
    }

    /**
     * Adds an athlete to a sports event.
     *
     * @param eventID   the ID of the sports event.
     * @param athleteID the ID of the athlete to add.
     * @return true if the athlete is successfully added, false otherwise.
     */
    public boolean addAthleteToSportsEvent(int eventID, int athleteID) {
        SportsEvent sportsEvent = (SportsEvent) findEventByID(eventID);
        if (sportsEvent != null) {
            Athlete athlete = athleteService.findAthleteByID(athleteID);
            if (athlete == null) {
                System.out.println("Athlete not found.");
                return false;
            }
            SportsEventLineUp sportsEventLineUp = new SportsEventLineUp(sportsEvent, athlete);
            sportsEventLineUpBaseRepository.create(sportsEventLineUp);
            return true;
        }
        return false;
    }

    /**
     * Removes an athlete from a sports event.
     *
     * @param eventID   the ID of the sports event.
     * @param athleteID the ID of the athlete to remove.
     * @return true if the athlete is successfully removed, false otherwise.
     */
    public boolean removeAthleteFromSportsEvent(int eventID, int athleteID) {
        SportsEventLineUp lineUp = sportsEventLineUpBaseRepository.getAll().stream()
                .filter(lineUpEntry -> lineUpEntry.getSportsEvent().getID() == eventID &&
                        lineUpEntry.getAthlete().getID() == athleteID)
                .findFirst()
                .orElse(null);

        if (lineUp != null) {
            sportsEventLineUpBaseRepository.delete(lineUp.getID());
            return true;
        }
        return false;
    }

    /**
     * Finds a sports event by its ID.
     *
     * @param sportsEventID the ID of the sports event.
     * @return the SportsEvent object, or null if not found.
     */
    public SportsEvent findSportsEventByID(int sportsEventID) {
        return eventBaseRepository.getAll().stream()
                .filter(event -> event instanceof SportsEvent && event.getID() == sportsEventID)
                .map(event -> (SportsEvent) event)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the list of athletes associated with a specific sports event.
     *
     * @param sportsEventID the ID of the sports event.
     * @return a list of Athlete objects.
     */
    public List<Athlete> getAthletesBySportsEvent(int sportsEventID) {
        return sportsEventLineUpBaseRepository.getAll().stream()
                .filter(lineUp -> lineUp.getSportsEvent().getID() == sportsEventID)
                .map(SportsEventLineUp::getAthlete)
                .distinct() // Evită duplicarea atleților
                .toList();
    }

    // --- Common Event Methods ---
    /**
     * Updates an existing event with new details.
     *
     * @param eventId          the ID of the event to update.
     * @param newName          the new name of the event.
     * @param newDescription   the new description of the event.
     * @param newStartDateTime the new start date and time of the event.
     * @param newEndDateTime   the new end date and time of the event.
     * @param newStatus        the new status of the event.
     * @return true if the event is successfully updated, false otherwise.
     * @throws EntityNotFoundException if the event is not found.
     */
    public boolean updateEvent(int eventId, String newName, String newDescription,
                               LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        Event event = findEventByID(eventId);
        if (event == null) {
            throw new EntityNotFoundException("Event not found with ID: " + eventId);
        }
        event.setEventName(newName);
        event.setEventDescription(newDescription);
        event.setStartDateTime(newStartDateTime);
        event.setEndDateTime(newEndDateTime);
        event.setEventStatus(newStatus);
        eventBaseRepository.update(event);
        return true;
    }

    /**
     * Deletes an event by its ID.
     *
     * @param eventId the ID of the event to delete.
     * @return true if the event is successfully deleted, false otherwise.
     * @throws EntityNotFoundException if the event is not found.
     */
    public boolean deleteEvent(int eventId) {
        Event event = findEventByID(eventId);
        if (event == null) {
            throw new EntityNotFoundException("Event not found with ID: " + eventId);
        }

        if (event instanceof Concert) {
            concertLineUpBaseRepository.getAll().stream()
                    .filter(lineUp -> lineUp.getConcert().getID() == eventId)
                    .forEach(lineUp -> concertLineUpBaseRepository.delete(lineUp.hashCode()));
        } else if (event instanceof SportsEvent) {
            sportsEventLineUpBaseRepository.getAll().stream()
                    .filter(lineUp -> lineUp.getSportsEvent().getID() == eventId)
                    .forEach(lineUp -> sportsEventLineUpBaseRepository.delete(lineUp.hashCode()));
        }
        eventBaseRepository.delete(eventId);
        return true;
    }

    /**
     * Finds an event by its ID.
     *
     * @param eventId the ID of the event to find.
     * @return the Event object, or null if not found.
     */
    public Event findEventByID(int eventId) {
        return eventBaseRepository.getAll().stream()
                .filter(event -> event.getID() == eventId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all events.
     *
     * @return a list of all Event objects.
     */
    public List<Event> getAllEvents() {
        return eventBaseRepository.getAll();
    }

    /**
     * Retrieves events associated with a specific venue.
     *
     * @param venueID the ID of the venue.
     * @return a list of Event objects.
     */
    public List<Event> getEventsByVenue(int venueID) {
        return eventBaseRepository.getAll().stream()
                .filter(event -> event.getVenueID() == venueID)
                .toList();
    }
    /**
     * Retrieves events by location or venue name.
     *
     * @param locationOrVenueName the location or venue name to filter events.
     * @return a list of Event objects.
     */
    public List<Event> getEventsByLocation(String locationOrVenueName) {
        List<Venue> venues = venueService.findVenuesByLocationOrName(locationOrVenueName);
        return venues.stream()
                .flatMap(venue -> getEventsByVenue(venue.getID()).stream())
                .toList();
    }

    /**
     * Retrieves upcoming events for a specific artist.
     *
     * @param artistID the ID of the artist.
     * @return a list of upcoming Event objects.
     */
    public List<Event> getUpcomingEventsForArtist(int artistID) {
        return concertLineUpBaseRepository.getAll().stream()
                .filter(lineUp -> lineUp.getArtist().getID() == artistID &&
                        lineUp.getConcert().getStartDateTime().isAfter(LocalDateTime.now()))
                .map(ConcertLineUp::getConcert)
                .filter(event -> event instanceof Concert)
                .map(event -> (Event) event)
                .toList();
    }

    /**
     * Retrieves upcoming events for a specific athlete.
     *
     * @param athleteID the ID of the athlete.
     * @return a list of upcoming Event objects.
     */
    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
        return sportsEventLineUpBaseRepository.getAll().stream()
                .filter(lineUp -> lineUp.getAthlete().getID() == athleteID &&
                        lineUp.getSportsEvent().getStartDateTime().isAfter(LocalDateTime.now()))
                .map(SportsEventLineUp::getSportsEvent)
                .filter(event -> event instanceof SportsEvent)
                .map(event -> (Event) event)
                .toList();
    }
}