package service;

import model.Athlete;
import model.Event;
import model.SportsEvent;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AthleteService {
    private final IRepository<Athlete> athleteRepository;
    private final IRepository<Event> eventRepository;

    public AthleteService(IRepository<Athlete> athleteRepository, IRepository<Event> eventRepository) {
        this.athleteRepository = athleteRepository;
        this.eventRepository = eventRepository;
    }

    public boolean createAthlete(String athleteName, String genre) {
        int newID = athleteRepository.getAll().size() + 1;
        Athlete athlete = new Athlete(newID, athleteName, genre);
        athleteRepository.create(athlete);
        return true;
    }

    public boolean updateAthlete(int athelteID, String newName, String newGenre) {
        Athlete athlete = findAthleteByID(athelteID);
        if (athlete != null) {
            athlete.setAthleteName(newName);
            athlete.setAthleteSport(newGenre);
            athleteRepository.update(athlete);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAthlete(int athelteID) {
        Athlete athlete = findAthleteByID(athelteID);
        if (athlete != null) {
            athleteRepository.delete(athelteID);
            return true;
        } else {
            return false;
        }
    }

    public List<Athlete> getAllAthletes() {
        return athleteRepository.getAll();
    }

    public Athlete findAthleteByID(int athleteID) {
        return athleteRepository.getAll().stream().filter(athlete -> athlete.getID() == athleteID).findFirst().orElse(null);
    }

    public Athlete findAthleteByName(String athleteName) {
        return athleteRepository.getAll().stream().filter(athlete -> athlete.getAthleteName().equalsIgnoreCase(athleteName)).findFirst().orElse(null);
    }

    public List<Event> getEventsByAthlete(Athlete athlete) {
        return eventRepository.getAll().stream().filter(event -> event instanceof SportsEvent)  // Filter only Concert events
                .filter(event -> ((SportsEvent) event).getAthletes().equals(athlete))  // Match the artist
                .collect(Collectors.toList());
    }

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
