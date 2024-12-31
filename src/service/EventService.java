package service;

import model.*;
import repository.IRepository;
import repository.factory.RepositoryFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    private final IRepository<Event> eventRepository;
    private final IRepository<ConcertLineUp> concertLineUpRepository;
    private final IRepository<SportsEventLineUp> sportsEventLineUpRepository;
    private final VenueService venueService;

    public EventService(RepositoryFactory eventRepository, RepositoryFactory concertLineUpRepository,
                        RepositoryFactory sportsEventLineUpRepository, VenueService venueService) {
        this.eventRepository = eventRepository.createEventRepository();
        this.concertLineUpRepository = concertLineUpRepository.createConcertLineUpRepository();
        this.sportsEventLineUpRepository = sportsEventLineUpRepository.createSportsEventLineUpRepository();
        this.venueService = venueService;
    }

    // --- Concert Methods ---
    public Concert createConcert(String eventName, String eventDescription, LocalDateTime startDateTime,
                                 LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        Concert concert = new Concert(0, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
        eventRepository.create(concert);
        return concert;
    }

    public boolean addArtistToConcert(int eventID, int artistID) {
        Concert concert = (Concert) findEventByID(eventID);
        if (concert != null) {
            Artist artist = new Artist();
            artist.setID(artistID);
            ConcertLineUp concertLineUp = new ConcertLineUp(concert, artist);
            concertLineUpRepository.create(concertLineUp);
            return true;
        }
        return false;
    }

    public boolean removeArtistFromConcert(int eventID, int artistID) {
        ConcertLineUp lineUp = concertLineUpRepository.getAll().stream()
                .filter(lineUpEntry -> lineUpEntry.getConcert().getID() == eventID &&
                        lineUpEntry.getArtist().getID() == artistID)
                .findFirst()
                .orElse(null);
        if (lineUp != null) {
            concertLineUpRepository.delete(lineUp.hashCode());
            return true;
        }
        return false;
    }

    public Concert findConcertByID(int concertID) {
        return eventRepository.getAll().stream()
                .filter(event -> event instanceof Concert && event.getID() == concertID)
                .map(event -> (Concert) event)
                .findFirst()
                .orElse(null);
    }

    public List<Artist> getArtistsByConcert(int concertID) {
        return concertLineUpRepository.getAll().stream()
                .filter(lineUp -> lineUp.getConcert().getID() == concertID)
                .map(ConcertLineUp::getArtist)
                .distinct() // Evită duplicarea artiștilor
                .toList();
    }

    // --- Sports Event Methods ---
    public SportsEvent createSportsEvent(String eventName, String eventDescription, LocalDateTime startDateTime,
                                         LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        SportsEvent sportsEvent = new SportsEvent(0, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
        eventRepository.create(sportsEvent);
        return sportsEvent;
    }

    public boolean addAthleteToSportsEvent(int eventID, int athleteID) {
        SportsEvent sportsEvent = (SportsEvent) findEventByID(eventID);
        if (sportsEvent != null) {
            Athlete athlete = new Athlete();
            athlete.setID(athleteID);
            SportsEventLineUp sportsEventLineUp = new SportsEventLineUp(sportsEvent, athlete);
            sportsEventLineUpRepository.create(sportsEventLineUp);
            return true;
        }
        return false;
    }

    public boolean removeAthleteFromSportsEvent(int eventID, int athleteID) {
        SportsEventLineUp lineUp = sportsEventLineUpRepository.getAll().stream()
                .filter(lineUpEntry -> lineUpEntry.getSportsEvent().getID() == eventID &&
                        lineUpEntry.getAthlete().getID() == athleteID)
                .findFirst()
                .orElse(null);

        if (lineUp != null) {
            sportsEventLineUpRepository.delete(lineUp.hashCode());
            return true;
        }
        return false;
    }

    public SportsEvent findSportsEventByID(int sportsEventID) {
        return eventRepository.getAll().stream()
                .filter(event -> event instanceof SportsEvent && event.getID() == sportsEventID)
                .map(event -> (SportsEvent) event)
                .findFirst()
                .orElse(null);
    }

    public List<Athlete> getAthletesBySportsEvent(int sportsEventID) {
        return sportsEventLineUpRepository.getAll().stream()
                .filter(lineUp -> lineUp.getSportsEvent().getID() == sportsEventID)
                .map(SportsEventLineUp::getAthlete)
                .distinct() // Evită duplicarea atleților
                .toList();
    }

    // --- Common Event Methods ---
    public boolean updateEvent(int eventId, String newName, String newDescription,
                               LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, EventStatus newStatus) {
        Event event = findEventByID(eventId);
        if (event != null) {
            event.setEventName(newName);
            event.setEventDescription(newDescription);
            event.setStartDateTime(newStartDateTime);
            event.setEndDateTime(newEndDateTime);
            event.setEventStatus(newStatus);
            eventRepository.update(event);
            return true;
        }
        return false;
    }

    public boolean deleteEvent(int eventId) {
        Event event = findEventByID(eventId);
        if (event != null) {
            if (event instanceof Concert) {
                concertLineUpRepository.getAll().stream()
                        .filter(lineUp -> lineUp.getConcert().getID() == eventId)
                        .forEach(lineUp -> concertLineUpRepository.delete(lineUp.hashCode()));
            } else if (event instanceof SportsEvent) {
                sportsEventLineUpRepository.getAll().stream()
                        .filter(lineUp -> lineUp.getSportsEvent().getID() == eventId)
                        .forEach(lineUp -> sportsEventLineUpRepository.delete(lineUp.hashCode()));
            }
            eventRepository.delete(eventId);
            return true;
        }
        return false;
    }

    public Event findEventByID(int eventId) {
        return eventRepository.getAll().stream()
                .filter(event -> event.getID() == eventId)
                .findFirst()
                .orElse(null);
    }

    public List<Event> getAllEvents() {
        return eventRepository.getAll();
    }

    public List<Event> getEventsByVenue(int venueID) {
        return eventRepository.getAll().stream()
                .filter(event -> event.getVenueID() == venueID)
                .toList();
    }

    public List<Event> getEventsByLocation(String locationOrVenueName) {
        List<Venue> venues = venueService.findVenuesByLocationOrName(locationOrVenueName);
        return venues.stream()
                .flatMap(venue -> getEventsByVenue(venue.getID()).stream())
                .toList();
    }

    public List<Event> getUpcomingEventsForArtist(int artistID) {
        return concertLineUpRepository.getAll().stream()
                .filter(lineUp -> lineUp.getArtist().getID() == artistID &&
                        lineUp.getConcert().getStartDateTime().isAfter(LocalDateTime.now()))
                .map(ConcertLineUp::getConcert)
                .filter(event -> event instanceof Concert)
                .map(event -> (Event) event)
                .toList();
    }

    public List<Event> getUpcomingEventsForAthlete(int athleteID) {
        return sportsEventLineUpRepository.getAll().stream()
                .filter(lineUp -> lineUp.getAthlete().getID() == athleteID &&
                        lineUp.getSportsEvent().getStartDateTime().isAfter(LocalDateTime.now()))
                .map(SportsEventLineUp::getSportsEvent)
                .filter(event -> event instanceof SportsEvent)
                .map(event -> (Event) event)
                .toList();
    }
}