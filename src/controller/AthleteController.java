package controller;

import model.Athlete;
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
     * @param athleteName The name of the athlete to add.
     * @param athleteSport The sport associated with the athlete.
     * @return true if the athlete was successfully added, false otherwise.
     */
    public boolean createAthlete(String athleteName, String athleteSport) {
        boolean success = athleteService.createAthlete(athleteName, athleteSport);
        if (success) {
            System.out.println("Athlete added successfully.");
        } else {
            System.out.println("Athlete could not be added.");
        }
        return success;
    }

    /**
     * Updates an existing athlete's information.
     * @param athleteID The ID of the athlete to update.
     * @param newName The new name for the athlete.
     * @param newSport The new sport for the athlete.
     * @return true if the athlete was successfully updated, false otherwise.
     */
    public boolean updateAthlete(int athleteID, String newName, String newSport) {
        boolean success = athleteService.updateAthlete(athleteID, newName, newSport);
        if (success) {
            System.out.println("Athlete updated successfully.");
        } else {
            System.out.println("Athlete not found or could not be updated.");
        }
        return success;
    }

    /**
     * Deletes an athlete with the specified ID.
     * @param athleteID The ID of the athlete to delete.
     * @return true if the athlete was successfully deleted, false otherwise.
     */
    public boolean deleteAthlete(int athleteID) {
        boolean success = athleteService.deleteAthlete(athleteID);
        if (success) {
            System.out.println("Athlete deleted successfully.");
        } else {
            System.out.println("Athlete not found or could not be deleted.");
        }
        return success;
    }

    /**
     * Retrieves a list of all athletes in the system.
     * @return A list of all athletes.
     */
    public List<Athlete> getAllAthletes() {
        List<Athlete> athletes = athleteService.getAllAthletes();
        if (!athletes.isEmpty()) {
            System.out.println("List of all athletes:");
            athletes.forEach(System.out::println);
        } else {
            System.out.println("No athletes found.");
        }
        return athletes;
    }

    /**
     * Searches for an athlete by their name.
     * @param athleteName The name of the athlete to search for.
     * @return The athlete if found, or null if no athlete matches the specified name.
     */
    public Athlete findAthleteByName(String athleteName) {
        Athlete athlete = athleteService.findAthleteByName(athleteName);
        if (athlete != null) {
            System.out.println("Athlete found: " + athlete);
        } else {
            System.out.println("Athlete with name '" + athleteName + "' not found.");
        }
        return athlete;
    }

    /**
     * Retrieves an athlete by their ID.
     * @param athleteID The ID of the athlete to retrieve.
     * @return The athlete with the specified ID, or null if no athlete is found.
     */
    public Athlete findAthleteByID(int athleteID) {
        Athlete athlete = athleteService.findAthleteByID(athleteID);
        if (athlete != null) {
            System.out.println("Athlete found: " + athlete);
        } else {
            System.out.println("Athlete with ID " + athleteID + " not found.");
        }
        return athlete;
    }

    /**
     * Finds athletes that match a specified sport.
     * @param sport The sport to filter athletes by.
     * @return A list of athletes who participate in the specified sport.
     */
    public List<Athlete> findAthletesBySport(String sport) {
        List<Athlete> athletes = athleteService.findAthletesBySport(sport);
        if (!athletes.isEmpty()) {
            System.out.println("Athletes participating in sport '" + sport + "':");
            athletes.forEach(System.out::println);
        } else {
            System.out.println("No athletes found for sport '" + sport + "'.");
        }
        return athletes;
    }
}
