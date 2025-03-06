package main.java.com.ticketsalesapp.service;

import lombok.RequiredArgsConstructor;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.Athlete;
import main.java.com.ticketsalesapp.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AthleteService {
    private final Repository<Athlete> athleteRepository;

    /**
     * Creates a new athlete and adds them to the repository.
     * @param athleteName The name of the athlete.
     * @param sport The sport genre the athlete specializes in.
     * @return true if the athlete was successfully created and added to the repository, false otherwise.
     */
    public boolean createAthlete(String athleteName, String sport) {
        if (findAthleteByName(athleteName) != null) {
            throw new BusinessLogicException("Athlete with name '" + athleteName + "' already exists.");
        }
        Athlete athlete = new Athlete(0, athleteName, sport);
        athleteRepository.create(athlete);
        return true;
    }

    /**
     * Updates an existing athlete's details.
     * @param athleteID The ID of the athlete to be updated.
     * @param newName The new name for the athlete.
     * @param newSport The new sport genre for the athlete.
     * @return true if the athlete was found and successfully updated, false if the athlete was not found.
     */
    public boolean updateAthlete(int athleteID, String newName, String newSport) {
        Athlete athlete = findAthleteByID(athleteID);
        if (athlete == null) {
            throw new ValidationException("Athlete with ID " + athleteID + " does not exist.");
        }
        if (newName == null || newName.isBlank()) {
            throw new ValidationException("Athlete name cannot be null or empty.");
        }
        athlete.setAthleteName(newName);
        athlete.setAthleteSport(newSport);
        athleteRepository.update(athlete);
        return true;
    }

    /**
     * Deletes an athlete from the repository by their ID.
     * @param athleteID The ID of the athlete to be deleted.
     * @return true if the athlete was found and successfully deleted, false if the athlete was not found.
     */
    public boolean deleteAthlete(int athleteID) {
        Athlete athlete = findAthleteByID(athleteID);
        if (athlete == null) {
            throw new ValidationException("Athlete with ID " + athleteID + " does not exist.");
        }
        athleteRepository.delete(athleteID);
        return true;
    }

    /**
     * Retrieves a list of all athletes from the repository.
     * @return A list of all athletes in the repository.
     */
    public List<Athlete> getAllAthletes() {
        return athleteRepository.getAll();
    }

    /**
     * Finds an athlete by their ID.
     * @param athleteID The ID of the athlete to be found.
     * @return The athlete with the specified ID, or null if no athlete was found.
     */
    public Athlete findAthleteByID(int athleteID) {
        return athleteRepository.getAll().stream().filter(athlete -> athlete.getID() == athleteID).findFirst().orElse(null);
    }

    /**
     * Finds an athlete by their name.
     * @param athleteName The name of the athlete to be found.
     * @return The athlete with the specified name, or null if no athlete was found.
     */
    public Athlete findAthleteByName(String athleteName) {
        return athleteRepository.getAll().stream().filter(athlete -> athlete.getAthleteName().equalsIgnoreCase(athleteName)).findFirst().orElse(null);
    }


    /**
     * Finds all athletes participating in a specific sport.
     * @param sport The sport to filter athletes by.
     * @return A list of athletes who participate in the specified sport.
     */
    public List<Athlete> findAthletesBySport(String sport) {
        List<Athlete> athletesInSport = new ArrayList<>();
        for (Athlete athlete : athleteRepository.getAll()) {
            if (athlete.getAthleteSport().equalsIgnoreCase(sport)) {
                athletesInSport.add(athlete);
            }
        }
        return athletesInSport;
    }
}
