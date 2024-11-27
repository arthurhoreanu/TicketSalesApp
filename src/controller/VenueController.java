package controller;

import model.Customer;
import model.Event;
import model.Seat;
import model.Section;
import model.Venue;
import service.VenueService;

import java.util.List;

/**
 * The VenueController class provides methods for managing venues, including creating, updating, and retrieving venue information.
 * It also provides methods for managing seating arrangements and recommendations for events within venues.
 */
public class VenueController {
    private final VenueService venueService;

    /**
     * Constructs a VenueController with the specified VenueService.
     *
     * @param venueService the service for venue-related operations
     */
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    /**
     * Adds a new venue to the repository.
     *
     * @param name      the name of the venue
     * @param location  the location of the venue
     * @param capacity  the capacity of the venue
     * @param sections  the list of sections within the venue
     */
    public void addVenue(String name, String location, int capacity, List<Section> sections) {
        boolean isAdded = venueService.addVenue(name, location, capacity, sections);
        if (isAdded) {
            System.out.println("Venue successfully added: " + name + " at " + location);
        } else {
            System.out.println("Venue with the same name and location already exists. Addition failed.");
        }
    }

    /**
     * Creates a new venue with specified sections and seats.
     *
     * @param name           the name of the venue
     * @param location       the location of the venue
     * @param capacity       the capacity of the venue
     * @param sectionCapacity the capacity of each section
     * @param rowCount       the number of rows in each section
     * @param seatsPerRow    the number of seats per row
     */

    /**
     * Updates an existing venue by its ID.
     *
     * @param id           the ID of the venue to update
     * @param newName      the new name of the venue
     * @param newLocation  the new location of the venue
     * @param newCapacity  the new capacity of the venue
     * @param newSections  the updated list of sections
     */
    public void updateVenue(int id, String newName, String newLocation, int newCapacity, List<Section> newSections) {
        boolean isUpdated = venueService.updateVenue(id, newName, newLocation, newCapacity, newSections);
        if (isUpdated) {
            System.out.println("Venue updated successfully: " + newName + " at " + newLocation);
        } else {
            System.out.println("Venue with ID " + id + " not found. Update failed.");
        }
    }

    /**
     * Deletes a venue by its ID.
     *
     * @param id the ID of the venue to delete
     */
    public void deleteVenue(int id) {
        boolean isDeleted = venueService.deleteVenue(id);
        if (isDeleted) {
            System.out.println("Venue with ID " + id + " has been successfully deleted.");
        } else {
            System.out.println("Venue with ID " + id + " not found. Deletion failed.");
        }
    }

    /**
     * Retrieves a list of all venues.
     *
     * @return a list of all venues
     */
    public List<Venue> getAllVenues() {
        return venueService.getAllVenues();
    }

    /**
     * Finds a venue by its name.
     *
     * @param name the name of the venue
     * @return the venue with the specified name, or null if not found
     */
    public Venue findVenueByName(String name) {
        return venueService.findVenueByName(name);
    }

    /**
     * Finds venues by location or name.
     *
     * @param locationOrVenueName the location or name of the venue(s) to find
     */
    public void getVenuesByLocationOrName(String locationOrVenueName) {
        List<Venue> venues = venueService.getVenuesByLocationOrName(locationOrVenueName);
        if (venues.isEmpty()) {
            System.out.println("No venues found for location or name: " + locationOrVenueName);
        } else {
            System.out.println("Matching venues:");
            venues.forEach(venue -> System.out.println("- " + venue.getVenueName() + " at " + venue.getLocation()));
        }
    }

    /**
     * Checks and displays the number of available seats for a specific event in a venue.
     *
     * @param venue the venue to check for available seats
     * @param event the event for which seats are checked
     */
    public void getAvailableSeats(Venue venue, Event event) {
        int availableSeats = venueService.getAvailableSeats(venue, event);
        if (availableSeats > 0) {
            System.out.println("Available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "': " + availableSeats);
        } else {
            System.out.println("No available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "'.");
        }
    }

    /**
     * Recommends a seat in a venue for a customer based on preferences and event information.
     *
     * @param customer the customer for whom the seat is recommended
     * @param venue    the venue where the seat is located
     * @param event    the event for which the seat is recommended
     */
    public Seat recommendSeat(Customer customer, Venue venue, Event event) {
        return venueService.recommendSeat(customer, venue, event);
    }

    /**
     * Finds a venue by its ID.
     *
     * @param id the ID of the venue to find
     * @return the venue with the specified ID, or null if not found
     */
    public Venue findVenueById(int id) {
        Venue venue = venueService.findVenueByID(id);
        if (venue != null) {
            System.out.println("Venue found: " + venue.getVenueName() + " at " + venue.getLocation());
        } else {
            System.out.println("Venue with ID " + id + " not found.");
        }
        return venue;
    }

    /**
     * Retrieves and displays a list of available seats in the venue for a specific event.
     *
     * @param venue the venue to check for available seats
     * @param event the event for which seats are checked
     */
    public void getAvailableSeatsList(Venue venue, Event event) {
        List<Seat> availableSeats = venueService.getAvailableSeatsList(venue, event);
        if (availableSeats.isEmpty()) {
            System.out.println("No available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "'.");
        } else {
            System.out.println("Available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "':");
            availableSeats.forEach(seat -> System.out.println("- Row " + seat.getRowNumber() + ", Seat " + seat.getSeatNumber()));
        }
    }
}
