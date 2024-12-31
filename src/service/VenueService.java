package service;

import model.*;
import repository.IRepository;
import repository.factory.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VenueService {
    private final IRepository<Venue> venueRepository;
    private final SectionService sectionService;

    public VenueService(RepositoryFactory repositoryFactory, SectionService sectionService) {
        this.venueRepository = repositoryFactory.createVenueRepository();
        this.sectionService = sectionService;
    }

    /**
     * Creates a new Venue and saves it to both repositories.
     */
    public Venue createVenue(String name, String location, int capacity, boolean hasSeats) {
       Venue venue = new Venue(0, name, location, capacity, hasSeats);
       venueRepository.create(venue);
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
        sectionService.deleteSectionByVenue(venueId);
        venueRepository.delete(venueId);
        return true;
    }

    /**
     * Adds a Section to a Venue.
     */
    public void addSectionToVenue(int venueId, int numberOfSections, int sectionCapacity, String defaultSectionName) {
        Venue venue = venueRepository.read(venueId);
        if (venue == null) {
            throw new IllegalArgumentException("Venue not found");
        }
        for (int i = 0; i < numberOfSections; i++) {
            Section section = sectionService.createSection(venue, sectionCapacity, defaultSectionName + " " + (i + 1));
            venue.addSection(section); // Update Venue in-memory state
        }
        // Update venue repository after adding sections
        venueRepository.update(venue);
    }

    /**
     * Retrieves all Sections associated with a specific Venue.
     */
    public List<Section> getSectionsByVenueID(int venueId) {
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
                                .toList()
                );
            }
        }
        return availableSeats;
    }
}