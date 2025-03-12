package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.model.event.Athlete;
import main.java.com.ticketsalesapp.model.event.EventStatus;
import main.java.com.ticketsalesapp.model.event.SportsEvent;
import main.java.com.ticketsalesapp.service.SportsEventService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class SportsEventController {
    private final SportsEventService sportsEventService;

    public SportsEventController(SportsEventService sportsEventService) {
        this.sportsEventService = sportsEventService;
    }

//    public boolean createSportsEvent(String eventName, String eventDescription,
//                                     LocalDateTime startDateTime, LocalDateTime endDateTime,
//                                     int venueID, EventStatus eventStatus) {
//        return sportsEventService.createSportsEvent(eventName, eventDescription,
//                startDateTime, endDateTime, venueID, eventStatus);
//    }

    public boolean addAthleteToEvent(int eventID, int athleteID) {
        return sportsEventService.addAthleteToEvent(eventID, athleteID);
    }

    public boolean removeAthleteFromEvent(int eventID, int athleteID) {
        return sportsEventService.removeAthleteFromEvent(eventID, athleteID);
    }

    public Optional<SportsEvent> findSportsEventByID(int eventID) {
        return sportsEventService.findSportsEventByID(eventID);
    }

    public List<SportsEvent> getAllSportsEvents() {
        return sportsEventService.getAllSportsEvents();
    }

    public List<Athlete> getAthletesForEvent(int eventID) {
        return sportsEventService.getAthletesForEvent(eventID);
    }

    public boolean updateSportsEvent(int eventID, String newName, String newDescription,
                                     LocalDateTime newStartDateTime, LocalDateTime newEndDateTime,
                                     EventStatus newStatus) {
        return sportsEventService.updateSportsEvent(eventID, newName, newDescription,
                newStartDateTime, newEndDateTime, newStatus);
    }

    public boolean deleteSportsEvent(int eventID) {
        return sportsEventService.deleteSportsEvent(eventID);
    }
}