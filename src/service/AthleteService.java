package service;

import model.Athlete;
import model.Event;
import model.SportsEvent;
import repository.DBRepository;
import repository.FileRepository;
import repository.IRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AthleteService {
    private final IRepository<Athlete> athleteRepository;
    private final IRepository<Event> eventRepository;
    private final FileRepository<Athlete> athleteFileRepository;

    public AthleteService(IRepository<Athlete> athleteRepository, IRepository<Event> eventRepository) {
        this.athleteRepository = athleteRepository;
        this.eventRepository = eventRepository;
        this.athleteFileRepository = new FileRepository<>("src/repository/data/athletes.csv", Athlete::fromCsv);
        syncFromCsv();
    }

    private void syncFromCsv() {
        List<Athlete> athletes = athleteRepository.getAll();
        for (Athlete athlete : athletes) {
            athleteRepository.create(athlete);
        }
    }

    private void syncFromDatabase() {
        List<Athlete> athletes = athleteRepository.getAll();
        for (Athlete athlete : athletes) {
            athleteRepository.create(athlete);
        }
    }

    /**
     * Creates a new athlete and adds them to the repository.
     * @param athleteName The name of the athlete.
     * @param genre The sport genre the athlete specializes in.
     * @return true if the athlete was successfully created and added to the repository, false otherwise.
     */
    public boolean createAthlete(String athleteName, String genre) {
        int newID = athleteRepository.getAll().size() + 1;
        Athlete athlete = new Athlete(newID, athleteName, genre);
        athleteRepository.create(athlete);
        athleteFileRepository.create(athlete);
        return true;
    }

    /**
     * Updates an existing athlete's details.
     * @param athelteID The ID of the athlete to be updated.
     * @param newName The new name for the athlete.
     * @param newGenre The new sport genre for the athlete.
     * @return true if the athlete was found and successfully updated, false if the athlete was not found.
     */
    public boolean updateAthlete(int athelteID, String newName, String newGenre) {
        Athlete athlete = findAthleteByID(athelteID);
        if (athlete != null) {
            athlete.setAthleteName(newName);
            athlete.setAthleteSport(newGenre);
            athleteRepository.update(athlete);
            athleteFileRepository.update(athlete);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes an athlete from the repository by their ID.
     * @param athelteID The ID of the athlete to be deleted.
     * @return true if the athlete was found and successfully deleted, false if the athlete was not found.
     */
    public boolean deleteAthlete(int athelteID) {
        Athlete athlete = findAthleteByID(athelteID);
        if (athlete != null) {
            athleteRepository.delete(athelteID);
            athleteFileRepository.delete(athelteID);
            return true;
        } else {
            return false;
        }
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
