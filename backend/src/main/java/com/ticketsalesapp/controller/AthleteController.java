package com.ticketsalesapp.controller;

import com.ticketsalesapp.model.event.Athlete;
import com.ticketsalesapp.service.AthleteService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AthleteController {

    private final AthleteService athleteService;

    /**
     * Constructor for AthleteController.
     * @param athleteService The instance of AthleteService used for athlete-related operations.
     */
    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    /**
     * Creates a new athlete with the specified name and sport.
     * @param athleteName The name of the athlete to add.
     * @param sport The sport associated with the athlete.
     */
    public void createAthlete(String athleteName, String sport) {
        athleteService.createAthlete(athleteName, sport);
    }

    /**
     * Updates an existing athlete's information.
     * @param athleteId The ID of the athlete to update.
     * @param newName The new name for the athlete.
     * @param newSport The new sport for the athlete.
     */
    public void updateAthlete(int athleteId, String newName, String newSport) {
        athleteService.updateAthlete(athleteId, newName, newSport);
    }

    /**
     * Deletes an athlete with the specified ID.
     * @param athleteId The ID of the athlete to delete.
     */
    public void deleteAthlete(int athleteId) {
        athleteService.deleteAthlete(athleteId);
    }

    /**
     * Retrieves a list of all athletes in the system.
     * @return A list of all athletes.
     */
    public List<Athlete> getAllAthletes() {
        return athleteService.getAllAthletes();
    }

    /**
     * Retrieves an athlete by their ID.
     * @param athleteId The ID of the athlete to retrieve.
     * @return The athlete with the specified ID, or null if no athlete is found.
     */
    public Athlete findAthleteById(int athleteId) {
        return athleteService.findAthleteById(athleteId);
    }

    /**
     * Searches for an athlete by their name.
     * @param athleteName The name of the athlete to search for.
     * @return The athlete if found, or null if no athlete matches the specified name.
     */
    public Optional<Athlete> findAthleteByName(String athleteName) {
        return athleteService.findAthleteByName(athleteName);
    }

    /**
     * Finds athletes that match a specified sport.
     * @param sport The sport to filter athletes by.
     * @return A list of athletes who participate in the specified sport.
     */
    public List<Athlete> findAthletesBySport(String sport) {
        return athleteService.findAthletesBySport(sport);
    }

    public void displayAllAthletes() {
        List<Athlete> athletes = athleteService.getAllAthletes();
        if (athletes.isEmpty()) {
            System.out.println("There are no athletes in the database.");
        }
        athletes.forEach(System.out::println);
    }
}
