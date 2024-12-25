package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;
import repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VenueService {
    private final IRepository<Venue> venueRepository;
    private final FileRepository<Venue> venueFileRepository;
    private final SectionService sectionService;

    /**
     * Constructs a VenueService with the specified dependencies.
     *
     * @param venueRepository the unified repository for managing venues.
     * @param sectionService  the service for managing sections within venues.
     */
    public VenueService(IRepository<Venue> venueRepository, SectionService sectionService) {
        this.venueRepository = venueRepository;
        this.sectionService = sectionService;

        // Initialize file repository for CSV operations
        this.venueFileRepository = new FileRepository<>("src/repository/data/venues.csv", Venue::fromCsv);
        syncFromCsv();
    }

    /**
     * Syncs venues from the CSV file into the unified repository.
     */
    private void syncFromCsv() {
        List<Venue> venues = venueFileRepository.getAll();
        for (Venue venue : venues) {
            venueRepository.create(venue);
        }
    }

    /**
     * Creates a new Venue and saves it to both repositories.
     */
    public Venue createVenue(String name, String location, int capacity, boolean hasSeats) {
        // Check for duplicates
        boolean duplicate = venueRepository.getAll().stream()
                .anyMatch(v -> v.getVenueName().equalsIgnoreCase(name) && v.getLocation().equalsIgnoreCase(location));
        if (duplicate) {
            return null; // Duplicate found
        }

        int newID = venueRepository.getAll().size() + 1;
        Venue venue = new Venue(newID, name, location, capacity, hasSeats);
        venueRepository.create(venue);          // Add to in-memory repository
        venueFileRepository.create(venue);     // Add to CSV
        return venue;
    }

    /**
     * Retrieves a Venue by its ID.
     */
    public Venue findVenueByID(int venueId) {
        return venueRepository.read(venueId);
    }

    /**
     * Finds Venues by location or name.
     */
    public List<Venue> findVenuesByLocationOrName(String keyword) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getVenueName().equalsIgnoreCase(keyword) || venue.getLocation().equalsIgnoreCase(keyword))
                .collect(Collectors.toList());
    }

    /**
     * Finds a venue by its name.
     *
     * @param name the name of the venue to find.
     * @return the Venue object if found, null otherwise.
     */
    public Venue findVenueByName(String name) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getVenueName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all Venues from the in-memory repository.
     */
    public List<Venue> getAllVenues() {
        return venueRepository.getAll();
    }

    /**
     * Updates an existing Venue.
     */
    public Venue updateVenue(int venueId, String name, String location, int capacity, boolean hasSeats) {
        Venue venue = venueRepository.read(venueId);
        if (venue == null) {
            return null; // Venue not found
        }

        venue.setVenueName(name);
        venue.setLocation(location);
        venue.setVenueCapacity(capacity);
        venue.setHasSeats(hasSeats);

        venueRepository.update(venue);         // Update in-memory repository
        venueFileRepository.update(venue);    // Update CSV
        return venue;
    }

    /**
     * Deletes a Venue and its associated Sections.
     */
    public boolean deleteVenue(int venueId) {
        Venue venue = venueRepository.read(venueId);
        if (venue == null) {
            return false; // Venue not found
        }

        for (Section section : venue.getSections()) {
            sectionService.deleteSection(section.getID()); // Clean up related sections
        }

        venueRepository.delete(venueId);      // Remove from in-memory repository
        venueFileRepository.delete(venueId); // Remove from CSV
        return true;
    }

    /**
     * Adds a Section to a Venue.
     */
    public Section addSectionToVenue(int venueId, String sectionName, int sectionCapacity) {
        Venue venue = venueRepository.read(venueId);
        if (venue == null || sectionCapacity > venue.getVenueCapacity()) {
            return null; // Venue not found or invalid capacity
        }

        Section section = new Section(0, sectionName, sectionCapacity, venue);
        venue.addSection(section);            // Update relationship in-memory
        venueRepository.update(venue);        // Update in-memory repository
        venueFileRepository.update(venue);   // Update CSV
        sectionService.createSection(section); // Persist the new section
        return section;
    }

    /**
     * Retrieves all Sections associated with a specific Venue.
     */
    public List<Section> getSectionsByVenueId(int venueId) {
        Venue venue = venueRepository.read(venueId);
        return (venue != null) ? venue.getSections() : new ArrayList<>();
    }

    /**
     * Retrieves all available Seats in a Venue for a specific Event.
     */
    public List<Seat> getAvailableSeatsInVenue(int venueId, int eventId) {
        Venue venue = venueRepository.read(venueId);
        if (venue == null) {
            return new ArrayList<>(); // Venue not found
        }

        List<Seat> availableSeats = new ArrayList<>();
        for (Section section : venue.getSections()) {
            for (Row row : section.getRows()) {
                availableSeats.addAll(
                        row.getSeats().stream()
                                .filter(seat -> !seat.isReserved() && seat.getTicket() != null
                                        && seat.getTicket().getEvent().getID() == eventId)
                                .collect(Collectors.toList())
                );
            }
        }

        return availableSeats;
    }

}