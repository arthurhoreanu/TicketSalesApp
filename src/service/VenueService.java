package service;

import model.Event;
import model.Section;
import model.Venue;
import model.Seat;
import repository.IRepository;
import model.Customer;

import java.util.List;
import java.util.ArrayList;

public class VenueService {
    private final IRepository<Venue> venueRepository;
    private final SectionService sectionService;

    public VenueService(IRepository<Venue> venueRepository, SectionService sectionService) {
        this.venueRepository = venueRepository;
        this.sectionService = sectionService;
    }

    // Adds a new venue to the repository
    public boolean addVenue(String name, String location, int capacity, List<Section> sections) {
        int newId = venueRepository.getAll().size() + 1;

        for (Venue venue : venueRepository.getAll()) {
            if (venue.getVenueName().equalsIgnoreCase(name) && venue.getLocation().equalsIgnoreCase(location)) {
                return false;
            }
        }

        Venue venue = new Venue(newId, name, location, capacity, sections);
        venueRepository.create(venue);
        return true;
    }

    // Creates a new venue with sections and seats
    public Venue createVenueWithSectionsAndSeats(String name, String location, int capacity, int sectionCapacity, int rowCount, int seatsPerRow) {
        int newId = venueRepository.getAll().size() + 1;
        List<Section> sections = new ArrayList<>();

        // Create sections with seats
        for (int sectionId = 1; sectionId <= capacity / sectionCapacity; sectionId++) {
            Section section = sectionService.createSectionWithSeats("Section " + sectionId, sectionId, sectionCapacity, rowCount, seatsPerRow, null);
            sections.add(section);
        }

        Venue venue = new Venue(newId, name, location, capacity, sections);

        // Set the venue reference for each section and add the sections
        for (Section section : sections) {
            section.setVenue(venue);
        }

        venueRepository.create(venue);
        return venue;
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
            return true;
        }
        return false;
    }

    // Deletes a venue by ID
    public boolean deleteVenue(int id) {
        if (findVenueById(id) != null) {
            venueRepository.delete(id);
            return true;
        }
        return false;
    }

    // Retrieves a list of all venues
    public List<Venue> getAllVenues() {
        return venueRepository.getAll();
    }

    // Manually finds a venue by its ID
    public Venue findVenueById(int id) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getID() == id)
                .findFirst()
                .orElse(null);
    }

    public Venue findVenueByName(String name) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getVenueName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // Checks available seats for a specific event in the entire venue
    public int getAvailableSeats(Venue venue, Event event) {
        return venue.getSections().stream()
                .mapToInt(section -> sectionService.getAvailableSeats(section, event).size())
                .sum();
    }

    public List<Seat> getAvailableSeatsList(Venue venue, Event event) {
        List<Seat> availableSeats = new ArrayList<>();
        for (Section section : venue.getSections()) {
            availableSeats.addAll(sectionService.getAvailableSeats(section, event));
        }
        return availableSeats;
    }


    // Recommends a seat in a venue based on customer preferences
    public Seat recommendSeat(Customer customer, Venue venue, Event event) {
        List<Section> sections = venue.getSections();

        sections.sort((s1, s2) -> {
            int preference1 = customer.getPreferredSections().getOrDefault(s1.getID(), 0);
            int preference2 = customer.getPreferredSections().getOrDefault(s2.getID(), 0);
            return Integer.compare(preference2, preference1);
        });

        for (Section section : sections) {
            Seat recommendedSeat = sectionService.recommendSeat(customer, section, event);
            if (recommendedSeat != null) {
                return recommendedSeat;
            }
        }
        return null; // No preferred seat available
    }

    public List<Venue> getVenuesByLocationOrName(String locationOrVenueName) {
        List<Venue> matchingVenues = new ArrayList<>();
        for (Venue venue : getAllVenues()) {
            if (venue.getVenueName().equalsIgnoreCase(locationOrVenueName) ||
                    venue.getLocation().equalsIgnoreCase(locationOrVenueName)) {
                matchingVenues.add(venue);
            }
        }
        return matchingVenues;
    }

}
