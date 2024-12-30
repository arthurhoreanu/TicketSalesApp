package controller;

import model.*;
import service.VenueService;

import java.util.List;

/**
 * Controller for managing venue-related operations.
 */
public class VenueController {
    private final VenueService venueService;

    /**
     * Constructs a VenueController with the specified VenueService.
     *
     * @param venueService The service for managing venues.
     */
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    /**
     * Creates a new venue.
     */
    public Venue createVenue(String name, String location, int capacity, boolean hasSeats) {
        Venue venue = venueService.createVenue(name, location, capacity, hasSeats);
        if (venue != null) {
            System.out.println("Venue created successfully: " + venue);
        } else {
            System.out.println("Failed to create venue.");
        }
        return venue;
    }

    /**
     * Retrieves a venue by its ID.
     */
    public Venue findVenueByID(int venueId) {
        Venue venue = venueService.findVenueByID(venueId);
        if (venue != null) {
            System.out.println("Venue found: " + venue);
        } else {
            System.out.println("Venue with ID " + venueId + " not found.");
        }
        return venue;
    }

    /**
     * Finds venues by location or name.
     */
    public List<Venue> findVenuesByLocationOrName(String keyword) {
        List<Venue> venues = venueService.findVenuesByLocationOrName(keyword);
        if (!venues.isEmpty()) {
            System.out.println("Venues found:");
            venues.forEach(System.out::println);
        } else {
            System.out.println("No venues found for the keyword: " + keyword);
        }
        return venues;
    }

    /**
     * Finds a venue by its name.
     */
    public Venue findVenueByName(String name) {
        Venue venue = venueService.findVenueByName(name);
        if (venue != null) {
            System.out.println("Venue found: " + venue);
        } else {
            System.out.println("Venue with name '" + name + "' not found.");
        }
        return venue;
    }

    /**
     * Retrieves all venues.
     */
    public List<Venue> getAllVenues() {
        List<Venue> venues = venueService.getAllVenues();
        if (!venues.isEmpty()) {
            System.out.println("All venues:");
            venues.forEach(System.out::println);
        } else {
            System.out.println("No venues available.");
        }
        return venues;
    }

    /**
     * Updates an existing venue.
     */
    public Venue updateVenue(int venueId, String name, String location, int capacity, boolean hasSeats) {
        Venue updatedVenue = venueService.updateVenue(venueId, name, location, capacity, hasSeats);
        if (updatedVenue != null) {
            System.out.println("Venue updated successfully: " + updatedVenue);
        } else {
            System.out.println("Failed to update venue. Venue with ID " + venueId + " not found.");
        }
        return updatedVenue;
    }

    /**
     * Deletes a venue by its ID.
     */
    public boolean deleteVenue(int venueId) {
        boolean deleted = venueService.deleteVenue(venueId);
        if (deleted) {
            System.out.println("Venue with ID " + venueId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete venue. Venue with ID " + venueId + " not found.");
        }
        return deleted;
    }

    /**
     * Adds multiple sections to a venue.
     */
    public void addSectionsToVenue(int venueId, int numberOfSections, int sectionCapacity, String defaultSectionName) {
        try {
            venueService.addSectionsToVenue(venueId, numberOfSections, sectionCapacity, defaultSectionName);
            System.out.println("Added " + numberOfSections + " sections to Venue ID " + venueId);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Retrieves all sections by venue ID.
     */
    public List<Section> getSectionsByVenueID(int venueId) {
        List<Section> sections = venueService.getSectionsByVenueID(venueId);
        if (!sections.isEmpty()) {
            System.out.println("Sections for venue ID " + venueId + ":");
            sections.forEach(System.out::println);
        } else {
            System.out.println("No sections found for venue ID " + venueId + ".");
        }
        return sections;
    }

    /**
     * Retrieves all available seats in a venue for a specific event.
     */
    public List<Seat> getAvailableSeatsInVenue(int venueId, int eventId) {
        List<Seat> availableSeats = venueService.getAvailableSeatsInVenue(venueId, eventId);
        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats for venue ID " + venueId + " and event ID " + eventId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats for venue ID " + venueId + " and event ID " + eventId + ".");
        }
        return availableSeats;
    }
}
