package main.java.com.ticketsalesapp.service;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.Athlete;
import main.java.com.ticketsalesapp.repository.Repository;
import main.java.com.ticketsalesapp.repository.factory.RepositoryFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AthleteService {

    private final Repository<Athlete> athleteRepository;

    public AthleteService(RepositoryFactory repositoryFactory) {
        this.athleteRepository = repositoryFactory.createAthleteRepository();
    }

    /**
     * Creates a new athlete and adds them to the repository.
     * @param athleteName The name of the athlete.
     * @param sport The sport genre the athlete specializes in.
     */
    public void createAthlete(String athleteName, String sport) {
        validateInput(athleteName, "Athlete name cannot be empty.");
        validateInput(sport, "Sport cannot be empty.");
        if (findAthleteByName(athleteName).isPresent()) {
            throw new BusinessLogicException("Athlete with name '" + athleteName + "' already exists.");
        }
        Athlete athlete = new Athlete(generateNewId(), athleteName, sport);
        athleteRepository.create(athlete);
    }

    /**
     * Updates an existing athlete's details.
     * @param athleteId The ID of the athlete to be updated.
     * @param newName The new name for the athlete.
     * @param newSport The new sport genre for the athlete.
     */
    public void updateAthlete(int athleteId, String newName, String newSport) {
        Athlete athlete = findAthleteById(athleteId);
        if (newName == null || newName.isBlank()) {
            throw new ValidationException("Athlete name cannot be null or empty.");
        }
        athlete.setAthleteName(newName);
        athlete.setAthleteSport(newSport);
        athleteRepository.update(athlete);
    }

    /**
     * Deletes an athlete from the repository by their ID.
     * @param athleteId The ID of the athlete to be deleted.
     */
    public void deleteAthlete(int athleteId) {
        findAthleteById(athleteId);
        athleteRepository.delete(athleteId);
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
     * @param athleteId The ID of the athlete to be found.
     * @return The athlete with the specified ID, or null if no athlete was found.
     */
    public Athlete findAthleteById(int athleteId) {
        return athleteRepository.read(athleteId)
                .orElseThrow(() -> new BusinessLogicException("Athlete not found"));
    }

    /**
     * Finds an athlete by their name.
     * @param athleteName The name of the athlete to be found.
     * @return The athlete with the specified name, or null if no athlete was found.
     */
    public Optional<Athlete> findAthleteByName(String athleteName) {
        return athleteRepository.getAll().stream()
                .filter(artist -> artist.getAthleteName().equalsIgnoreCase(athleteName))
                .findFirst();
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

    private int generateNewId() {
        return athleteRepository.getAll().stream()
                .mapToInt(Athlete::getId)
                .max()
                .orElse(0) + 1;
    }

    private void validateInput(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(errorMessage);
        }
    }
}
