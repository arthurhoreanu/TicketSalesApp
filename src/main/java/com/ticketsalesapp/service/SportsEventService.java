package main.java.com.ticketsalesapp.service;

import lombok.RequiredArgsConstructor;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.EntityNotFoundException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.Athlete;
import main.java.com.ticketsalesapp.model.event.EventStatus;
import main.java.com.ticketsalesapp.model.event.SportsEvent;
import main.java.com.ticketsalesapp.model.event.SportsEventLineUp;
import main.java.com.ticketsalesapp.model.venue.Venue;
import main.java.com.ticketsalesapp.repository.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SportsEventService {
    private final Repository<SportsEvent> sportsEventRepository;
    private final Repository<SportsEventLineUp> sportsEventLineUpRepository;
    private final VenueService venueService;
    private final AthleteService athleteService;

//    public boolean createSportsEvent(String eventName, String eventDescription,
//                                     LocalDateTime startDateTime, LocalDateTime endDateTime,
//                                     int venueID, EventStatus eventStatus) {
//        validateEventData(eventName, startDateTime, endDateTime);
//        Venue venue = venueService.findVenueByID(venueID);
//        if (venue == null) {
//            throw new EntityNotFoundException("Venue not found");
//        }
//
//        SportsEvent sportsEvent = new SportsEvent();
//        sportsEvent.setEventName(eventName);
//        sportsEvent.setEventDescription(eventDescription);
//        sportsEvent.setStartDateTime(startDateTime);
//        sportsEvent.setEndDateTime(endDateTime);
//        sportsEvent.setVenueID(venueID); // Adăugat setarea venue ID
//        sportsEvent.setEventStatus(eventStatus);
//        return sportsEventRepository.create(sportsEvent);
//    }

    public boolean addAthleteToEvent(int eventID, int athleteID) {
        Optional<SportsEvent> sportsEventOpt = findSportsEventByID(eventID);
        Athlete athlete = athleteService.findAthleteByID(athleteID);

        if (sportsEventOpt.isEmpty() || athlete == null) {
            return false;
        }

        SportsEventLineUp lineUp = new SportsEventLineUp(sportsEventOpt.get(), athlete);
        return sportsEventLineUpRepository.create(lineUp);
    }

    public boolean removeAthleteFromEvent(int eventID, int athleteID) {
        Optional<SportsEvent> eventOpt = findSportsEventByID(eventID);
        Athlete athlete = athleteService.findAthleteByID(athleteID);

        if (eventOpt.isEmpty() || athlete == null) {
            return false;
        }

        SportsEventLineUp lineUp = sportsEventLineUpRepository.getAll().stream()
                .filter(lu -> lu.getSportsEvent().getID() == eventID && lu.getAthlete().getID() == athleteID)
                .findFirst()
                .orElse(null);

        if (lineUp != null) {
            return sportsEventLineUpRepository.delete(lineUp.getID());
        }
        return false;
    }

    public Optional<SportsEvent> findSportsEventByID(int eventID) {
        return Optional.ofNullable(sportsEventRepository.read(eventID)
                .orElseThrow(() -> new BusinessLogicException("Sports event not found")));
    }

    public List<SportsEvent> getAllSportsEvents() {
        return sportsEventRepository.getAll();
    }

    public List<Athlete> getAthletesForEvent(int eventID) {
        return sportsEventLineUpRepository.getAll().stream()
                .filter(lu -> lu.getSportsEvent().getID() == eventID)
                .map(SportsEventLineUp::getAthlete)
                .toList();
    }

    public boolean updateSportsEvent(int eventID, String newName, String newDescription,
                                     LocalDateTime newStartDateTime, LocalDateTime newEndDateTime,
                                     EventStatus newStatus) {
        Optional<SportsEvent> eventOpt = findSportsEventByID(eventID);
        if (eventOpt.isEmpty()) {
            return false;
        }
        SportsEvent event = eventOpt.get();
        validateEventData(newName, newStartDateTime, newEndDateTime);
        event.setEventName(newName);
        event.setEventDescription(newDescription);
        event.setStartDateTime(newStartDateTime);
        event.setEndDateTime(newEndDateTime);
        event.setEventStatus(newStatus);
        return sportsEventRepository.update(event);
    }

    public boolean deleteSportsEvent(int eventID) {
        return sportsEventRepository.delete(eventID);
    }

    private void validateEventData(String eventName, LocalDateTime startDateTime,
                                   LocalDateTime endDateTime) {
        if (eventName == null || eventName.trim().isEmpty()) {
            throw new ValidationException("Event name cannot be empty");
        }
        if (startDateTime == null || endDateTime == null) {
            throw new ValidationException("Event dates cannot be null");
        }
        if (startDateTime.isAfter(endDateTime)) {
            throw new ValidationException("Start date must be before end date");
        }
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Event cannot start in the past");
        }
    }
}