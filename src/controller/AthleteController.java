package controller;

import model.Athlete;
import model.Event;
import service.AthleteService;

import java.util.List;

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
     * Displays a success message if the athlete is added successfully, otherwise displays a failure message.
     * @param athleteName The name of the athlete to add.
     * @param athleteSport The sport associated with the athlete.
     */
    public void createAthlete(String athleteName, String athleteSport) {
        int athleteID = athleteService.getAllAthletes().size() + 1; // Generate unique ID based on current size
        boolean success = athleteService.createAthlete(athleteName, athleteSport);
        if (success) {
            System.out.println("Athlete added successfully.");
        } else {
            System.out.println("Athlete could not be added.");
        }
    }

    /**
     * Updates an existing athlete's information.
     * Displays a success message if the update is successful, or an error message if the athlete was not found or could not be updated.
     * @param athleteID The ID of the athlete to update.
     * @param newName The new name for the athlete.
     * @param newSport The new sport for the athlete.
     */
    public void updateAthlete(int athleteID, String newName, String newSport) {
        boolean success = athleteService.updateAthlete(athleteID, newName, newSport);
        if (success) {
            System.out.println("Athlete updated successfully.");
        } else {
            System.out.println("Athlete not found or could not be updated.");
        }
    }

    /**
     * Deletes an athlete with the specified ID.
     * Displays a success message if the deletion is successful, or an error message if the athlete was not found or could not be deleted.
     * @param athleteID The ID of the athlete to delete.
     */
    public void deleteAthlete(int athleteID) {
        boolean success = athleteService.deleteAthlete(athleteID);
        if (success) {
            System.out.println("Athlete deleted successfully.");
        } else {
            System.out.println("Athlete not found or could not be deleted.");
        }
    }

    /**
     * Retrieves a list of all athletes in the system.
     * @return A list of all athletes.
     */
    public List<Athlete> getAllAthletes() {
        return athleteService.getAllAthletes();
    }

    /**
     * Searches for an athlete by their name.
     * @param athleteName The name of the athlete to search for.
     * @return The athlete if found, or null if no athlete matches the specified name.
     */
    public Athlete findAthleteByName(String athleteName) {
        return athleteService.findAthleteByName(athleteName);
    }

    /**
     * Retrieves an athlete by their ID.
     * @param athleteID The ID of the athlete to retrieve.
     * @return The athlete with the specified ID, or null if no athlete is found.
     */
    public Athlete findAthleteByID(int athleteID) {
        return athleteService.findAthleteByID(athleteID);
    }

    /**
     * Finds athletes that match a specified sport.
     * @param sport The sport to filter athletes by.
     * @return A list of athletes who participate in the specified sport.
     */
    public List<Athlete> findAthletesBySport(String sport) {
        return athleteService.findAthletesBySport(sport);
    }
}
