package com.ticketsalescompany.controller;

import com.ticketsalescompany.model.*;
import com.ticketsalescompany.service.VenueService;

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
     */
    public void createVenue(String name, String location, int capacity) {
        boolean isAdded = venueService.createVenue(name, location, capacity);
        if (isAdded) {
            System.out.println("Venue successfully added: " + name + " at " + location);
        } else {
            System.out.println("Venue with the same name and location already exists. Addition failed.");
        }
    }

    /**
     * Updates an existing venue by its ID.
     *
     * @param venueId      the ID of the venue to update
     * @param newName      the new name of the venue
     * @param newLocation  the new location of the venue
     * @param newCapacity  the new capacity of the venue
     */
    public void updateVenue(int venueId, String newName, String newLocation, int newCapacity) {
        boolean isUpdated = venueService.updateVenue(venueId, newName, newLocation, newCapacity);
        if (isUpdated) {
            System.out.println("Venue updated successfully: " + newName + " at " + newLocation);
        } else {
            System.out.println("Venue with ID " + venueId + " not found. Update failed.");
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
     * Finds a venue by its ID.
     *
     * @param id the ID of the venue to find
     * @return the venue with the specified ID, or null if not found
     */
    public Venue findVenueByID(int id) {
        Venue venue = venueService.findVenueByID(id);
        if (venue != null) {
            System.out.println("Venue found: " + venue.getVenueName() + " at " + venue.getLocation());
        } else {
            System.out.println("Venue with ID " + id + " not found.");
        }
        return venue;
    }

    /**
     * Finds a venue by its name.
     *
     * @param name the name of the venue
     * @return the venue with the specified name, or null if not found
     */
    public Venue findVenueByName(String name) {
        Venue venue = venueService.findVenueByName(name);
        if (venue == null) {
            System.out.println("Venue with name '" + name + "' not found.");
        }
        return venue;
    }

    /**
     * Finds venues by location or name.
     *
     * @param locationOrVenueName the location or name of the venue(s) to find
     * @return a list of matching venues
     */
    public List<Venue> findVenuesByLocationOrName(String locationOrVenueName) {
        List<Venue> venues = venueService.findVenuesByLocationOrName(locationOrVenueName);
        if (venues.isEmpty()) {
            System.out.println("No venues found for location or name: " + locationOrVenueName);
        } else {
            System.out.println("Matching venues:");
            venues.forEach(venue -> System.out.println("- " + venue.getVenueName() + " at " + venue.getLocation()));
        }
        return venues;
    }

    /**
     * Retrieves a list of available seats for a specific event in a venue.
     *
     * @param venue the venue to check
     * @param event the event for which seats are checked
     * @return a list of available seats
     */
    public List<Seat> getAvailableSeats(Venue venue, Event event) {
        List<Seat> availableSeats = venueService.getAvailableSeats(venue, event);
        if (availableSeats.isEmpty()) {
            System.out.println("No available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "'.");
        } else {
            System.out.println("Available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "':");
            availableSeats.forEach(seat -> System.out.println("- Row " + seat.getRow() + ", Seat " + seat.getID()));
        }
        return availableSeats;
    }

    /**
     * Recommends a seat in a venue for a customer based on preferences and event information.
     *
     * @param customer the customer for whom the seat is recommended
     * @param venue    the venue where the seat is located
     * @param event    the event for which the seat is recommended
     * @return the recommended seat
     */
//    public Seat recommendSeat(Customer customer, Venue venue, Event event) {
//        Seat seat = venueService.recommendSeat(customer, venue, event);
//        if (seat == null) {
//            System.out.println("No seat recommendations available for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "'.");
//        } else {
//            System.out.println("Recommended seat for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "': Row " + seat.getRowNumber() + ", Seat " + seat.getSeatNumber());
//        }
//        return seat;
//    }
}
