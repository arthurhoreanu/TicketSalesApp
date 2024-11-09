package service;

import model.Athlete;
import model.Concert;
import model.Event;
import model.SportsEvent;
import repository.IRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AthleteService {
    private final IRepository<Athlete> atheleteRepository;
    private final IRepository<Event> eventRepository;

    public AthleteService(IRepository<Athlete> atheleteRepository, IRepository<Event> eventRepository) {
        this.atheleteRepository = atheleteRepository;
        this.eventRepository = eventRepository;
    }

    public boolean createAthlete(String athleteName, String genre) {
        int newID = atheleteRepository.getAll().size() + 1;
        Athlete athlete = new Athlete(newID, athleteName, genre);
        atheleteRepository.create(athlete);
        return true;
    }

    public boolean updateAthlete(int athelteID, String newName, String newGenre) {
        Athlete athlete = findAthleteByID(athelteID);
        if (athlete != null) {
            athlete.setAthleteName(newName);
            athlete.setAthleteSport(newGenre);
            atheleteRepository.update(athlete);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAthlete(int athelteID) {
        Athlete athlete = findAthleteByID(athelteID);
        if (athlete != null) {
            atheleteRepository.delete(athelteID);
            return true;
        } else {
            return false;
        }
    }

    public List<Athlete> getAllAthletes() {
        return atheleteRepository.getAll();
    }

    private Athlete findAthleteByID(int athleteID) {
        return atheleteRepository.getAll().stream().filter(athlete -> athlete.getID() == athleteID).findFirst().orElse(null);
    }

    public Athlete findAthleteByName(String athleteName) {
        return atheleteRepository.getAll().stream().filter(athlete -> athlete.getAthleteName().equalsIgnoreCase(athleteName)).findFirst().orElse(null);
    }

    public List<Event> getEventsByAthlete(Athlete athlete) {
        return eventRepository.getAll().stream().filter(event -> event instanceof SportsEvent)  // Filter only Concert events
                .filter(event -> ((SportsEvent) event).getAthletes().equals(athlete))  // Match the artist
                .collect(Collectors.toList());
    }
}
