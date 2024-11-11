package controller;

import model.Athlete;
import model.Event;
import service.AthleteService;

import java.util.List;

public class AthleteController {
    private final AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    public void createAthlete(String athleteName, String athleteSport) {
        int artistID = athleteService.getAllAthletes().size() + 1;
        boolean success = athleteService.createAthlete(athleteName, athleteSport);
        if (success) {
            System.out.println("Athlete added successfully");
        } else {
            System.out.println("Athlete could not be added");
        }
    }

    public void updateAthlete(int athleteID, String newName, String newSport) {
        boolean success = athleteService.updateAthlete(athleteID, newName, newSport);
        if (success) {
            System.out.println("Athlete updated successfully.");
        } else {
            System.out.println("Athlete not found or could not be updated.");
        }
    }

    public void deleteAthlete(int athleteID) {
        boolean success = athleteService.deleteAthlete(athleteID);
        if (success) {
            System.out.println("Athlete deleted successfully.");
        } else {
            System.out.println("Athlete not found or could not be deleted.");
        }
    }

    public List<Athlete> getAllAthletes() {
        return athleteService.getAllAthletes();
    }

    public Athlete findAthleteByName(String athleteName) {
        return athleteService.findAthleteByName(athleteName);
    }

    public Athlete findAthleteByID(int athleteID) { return athleteService.findAthleteByID(athleteID); }

    public List<Event> getEventsByAthlete(Athlete athlete) {
        return athleteService.getEventsByAthlete(athlete);
    }

    public List<Athlete> findAthletesBySport(String sport) {
        return athleteService.findAthletesBySport(sport);}
}
