package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.service.ConcertService;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import main.java.com.ticketsalesapp.model.event.Artist;
import main.java.com.ticketsalesapp.model.event.Concert;
import main.java.com.ticketsalesapp.model.event.EventStatus;

@Component
public class ConcertController {
    private final ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    public Concert createConcert(String eventName, String eventDescription,
                                 LocalDateTime startDateTime, LocalDateTime endDateTime,
                                 int venueID, EventStatus eventStatus) {
        return concertService.createConcert(eventName, eventDescription,
                startDateTime, endDateTime, venueID, eventStatus);
    }

    public boolean addArtistToConcert(int concertID, int artistID) {
        return concertService.addArtistToConcert(concertID, artistID);
    }

    public void removeArtistFromConcert(int concertID, int artistID) {
        concertService.removeArtistFromConcert(concertID, artistID);
    }

    public Concert findConcertByID(int concertID) {
        return concertService.findConcertByID(concertID);
    }

    public List<Concert> getAllConcerts() {
        return concertService.getAllConcerts();
    }

    public List<Artist> getArtistsForConcert(int concertID) {
        return concertService.getArtistsForConcert(concertID);
    }

    public boolean updateConcert(int concertID, String newName, String newDescription,
                                 LocalDateTime newStartDateTime, LocalDateTime newEndDateTime,
                                 EventStatus newStatus) {
        return concertService.updateConcert(concertID, newName, newDescription,
                newStartDateTime, newEndDateTime, newStatus);
    }

    public boolean deleteConcert(int concertID) {
        return concertService.deleteConcert(concertID);
    }

    public double getBasePriceForEvent(int eventId) {
        Concert concert = concertService.findConcertByID(eventId);
        if (concert != null) {
            return concert.getBasePrice();
        } else {
            throw new IllegalArgumentException("Event not found for ID: " + eventId);
        }
    }
}