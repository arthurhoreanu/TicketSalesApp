//package com.ticketsalesapp.service;
//
//import lombok.RequiredArgsConstructor;
//import com.ticketsalesapp.exception.BusinessLogicException;
//import com.ticketsalesapp.exception.EntityNotFoundException;
//import com.ticketsalesapp.exception.ValidationException;
//import com.ticketsalesapp.model.event.*;
//import com.ticketsalesapp.model.venue.Venue;
//import com.ticketsalesapp.repository.Repository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Service class for managing event-related operations including concerts and sports events.
// */
//@Service
//@RequiredArgsConstructor
//public class ConcertService {
//    private final Repository<Concert> concertRepository;
//    private final Repository<ConcertLineUp> concertLineUpRepository;
//    private final VenueService venueService;
//    private final ArtistService artistService;
//
//    public Concert createConcert(String eventName, String eventDescription,
//                                 LocalDateTime startDateTime, LocalDateTime endDateTime,
//                                 int venueID, EventStatus eventStatus) {
//        validateEventData(eventName, startDateTime, endDateTime);
//        Optional<Venue> venue = venueService.findVenueByID(venueID);
//        if (venue.isEmpty()) {
//            throw new EntityNotFoundException("Venue not found");
//        }
//
//        Concert concert = new Concert();
//        concert.setEventName(eventName);
//        concert.setEventDescription(eventDescription);
//        concert.setStartDateTime(startDateTime);
//        concert.setEndDateTime(endDateTime);
//        concert.setEventStatus(eventStatus);
//        concertRepository.create(concert);
//        return concert;
//    }
//
//    public boolean addArtistToConcert(int concertID, int artistID) {
//        Optional<Concert> concertOpt = Optional.ofNullable(findConcertByID(concertID));
//        Artist artist = artistService.findArtistById(artistID);
//        if (concertOpt.isEmpty() || artist == null) {
//            return false;
//        }
//        ConcertLineUp lineUp = new ConcertLineUp(concertOpt.get(), artist);
//        return concertLineUpRepository.create(lineUp);
//    }
//
//    public void removeArtistFromConcert(int concertID, int artistID) {
//        Optional<Concert> concertOpt = Optional.ofNullable(findConcertByID(concertID));
//        Artist artist = artistService.findArtistById(artistID);
//        if (concertOpt.isEmpty() || artist == null) {
//            return;
//        }
//        ConcertLineUp lineUp = concertLineUpRepository.getAll().stream()
//                .filter(lu -> lu.getConcert().getId() == concertID && lu.getArtist().getId() == artistID)
//                .findFirst()
//                .orElse(null);
//        if (lineUp != null) {
//            concertLineUpRepository.delete(lineUp.getId());
//        }
//    }
//
//    public Concert findConcertByID(int concertID) {
//        return concertRepository.read(concertID)
//                .orElseThrow(() -> new BusinessLogicException("Concert not found"));
//    }
//
//    public List<Concert> getAllConcerts() {
//        return concertRepository.getAll();
//    }
//
//    public List<Artist> getArtistsForConcert(int concertID) {
//        return concertLineUpRepository.getAll().stream()
//                .filter(lu -> lu.getConcert().getId() == concertID)
//                .map(ConcertLineUp::getArtist)
//                .toList();
//    }
//
//    public boolean updateConcert(int concertID, String newName, String newDescription,
//                                 LocalDateTime newStartDateTime, LocalDateTime newEndDateTime,
//                                 EventStatus newStatus) {
//        Optional<Concert> concertOpt = Optional.ofNullable(findConcertByID(concertID));
//        if (concertOpt.isEmpty()) {
//            return false;
//        }
//        Concert concert = concertOpt.get();
//        validateEventData(newName, newStartDateTime, newEndDateTime);
//        concert.setEventName(newName);
//        concert.setEventDescription(newDescription);
//        concert.setStartDateTime(newStartDateTime);
//        concert.setEndDateTime(newEndDateTime);
//        concert.setEventStatus(newStatus);
//        return concertRepository.update(concert);
//    }
//
//    public boolean deleteConcert(int concertID) {
//        return concertRepository.delete(concertID);
//    }
//
//    private void validateEventData(String eventName, LocalDateTime startDateTime,
//                                   LocalDateTime endDateTime) {
//        if (eventName == null || eventName.trim().isEmpty()) {
//            throw new ValidationException("Event name cannot be empty");
//        }
//        if (startDateTime == null || endDateTime == null) {
//            throw new ValidationException("Event dates cannot be null");
//        }
//        if (startDateTime.isAfter(endDateTime)) {
//            throw new ValidationException("Start date must be before end date");
//        }
//        if (startDateTime.isBefore(LocalDateTime.now())) {
//            throw new ValidationException("Event cannot start in the past");
//        }
//    }
//}