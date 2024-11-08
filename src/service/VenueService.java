package service;

import model.Event;
import model.Section;
import model.Venue;
import repository.IRepository;

import java.util.List;

public class VenueService {
    private final IRepository<Venue> venueRepository;
    private final SectionService sectionService;

    public VenueService(IRepository<Venue> venueRepository, SectionService sectionService) {
        this.venueRepository = venueRepository;
        this.sectionService = sectionService;
    }

    // Adds a new venue to the repository
    public boolean addVenue(String name, String location, int capacity, List<Section> sections) {
        // Generate a unique ID for the new venue
        int newId = venueRepository.getAll().size() + 1;

        // Check if a venue with the same name and location already exists
        List<Venue> venues = venueRepository.getAll();
        for (int i = 0; i < venues.size(); i++) {
            Venue venue = venues.get(i);
            if (venue.getVenueName().equalsIgnoreCase(name) && venue.getLocation().equalsIgnoreCase(location)) {
                System.out.println("Venue with this name and location already exists.");
                return false;
            }
        }

        // Create a new Venue object
        Venue newVenue = new Venue(newId, name, location, capacity, sections);
        venueRepository.create(newVenue);
        System.out.println("Venue added successfully.");
        return true;
    }

    // Updates an existing venue by ID
    public boolean updateVenue(int id, String newName, String newLocation, int newCapacity, List<Section> newSections) {
        Venue venue = findVenueById(id);
        if (venue != null) {
            venue.setVenueName(newName);
            venue.setLocation(newLocation);
            venue.setVenueCapacity(newCapacity);
            venue.sections = newSections;
            venueRepository.update(venue);
            System.out.println("Venue updated successfully.");
            return true;
        } else {
            System.out.println("Venue not found.");
            return false;
        }
    }

    // Deletes a venue by ID
    public boolean deleteVenue(int id) {
        Venue venue = findVenueById(id);
        if (venue != null) {
            venueRepository.delete(id);
            System.out.println("Venue deleted successfully.");
            return true;
        } else {
            System.out.println("Venue not found.");
            return false;
        }
    }

    // Retrieves a list of all venues
    public List<Venue> getAllVenues() {
        return venueRepository.getAll();
    }

    // Manually finds a venue by its ID
    private Venue findVenueById(int id) {
        for (Venue venue : venueRepository.getAll()) {
            if (venue.getID() == id) {
                return venue;
            }
        }
        return null;
    }

    // Checks available seats for a specific event in the entire venue
    public int getAvailableSeats(Venue venue, Event event) {
        int totalAvailableSeats = 0;
        List<Section> sections = venue.getSections();
        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            totalAvailableSeats += sectionService.getAvailableSeats(section, event).size(); // Get the size of the list
        }
        return totalAvailableSeats;
    }
}
//TODO RECOMMENDED SEAT METHOD!!!