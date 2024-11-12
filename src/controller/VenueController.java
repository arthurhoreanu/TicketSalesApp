// TODO JavaDocs

package controller;

import model.Customer;
import model.Event;
import model.Seat;
import model.Section;
import model.Venue;
import service.VenueService;

import java.util.List;

public class VenueController {
    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    // Adds a new venue to the repository
    public void addVenue(String name, String location, int capacity, List<Section> sections) {
        boolean isAdded = venueService.addVenue(name, location, capacity, sections);
        if (isAdded) {
            System.out.println("Venue successfully added: " + name + " at " + location);
        } else {
            System.out.println("Venue with the same name and location already exists. Addition failed.");
        }
    }

    // Creates a new venue with sections and seats
    public void createVenueWithSectionsAndSeats(String name, String location, int capacity, int sectionCapacity, int rowCount, int seatsPerRow) {
        Venue venue = venueService.createVenueWithSectionsAndSeats(name, location, capacity, sectionCapacity, rowCount, seatsPerRow);
        System.out.println("Venue '" + venue.getVenueName() + "' created with " + venue.getSections().size() + " sections, each containing " + seatsPerRow + " seats per row.");
    }

    // Updates an existing venue
    public void updateVenue(int id, String newName, String newLocation, int newCapacity, List<Section> newSections) {
        boolean isUpdated = venueService.updateVenue(id, newName, newLocation, newCapacity, newSections);
        if (isUpdated) {
            System.out.println("Venue updated successfully: " + newName + " at " + newLocation);
        } else {
            System.out.println("Venue with ID " + id + " not found. Update failed.");
        }
    }

    // Deletes a venue by ID
    public void deleteVenue(int id) {
        boolean isDeleted = venueService.deleteVenue(id);
        if (isDeleted) {
            System.out.println("Venue with ID " + id + " has been successfully deleted.");
        } else {
            System.out.println("Venue with ID " + id + " not found. Deletion failed.");
        }
    }

    // Retrieves a list of all venues
    public List<Venue> getAllVenues() {
        return venueService.getAllVenues();
    }

    // Finds a venue by name
    public Venue findVenueByName(String name) {
        return venueService.findVenueByName(name);
    }

    // Finds venues by location or name
    public void getVenuesByLocationOrName(String locationOrVenueName) {
        List<Venue> venues = venueService.getVenuesByLocationOrName(locationOrVenueName);
        if (venues.isEmpty()) {
            System.out.println("No venues found for location or name: " + locationOrVenueName);
        } else {
            System.out.println("Matching venues:");
            venues.forEach(venue -> System.out.println("- " + venue.getVenueName() + " at " + venue.getLocation()));
        }
    }

    // Checks the number of available seats for a specific event in a venue
    public void getAvailableSeats(Venue venue, Event event) {
        int availableSeats = venueService.getAvailableSeats(venue, event);
        if (availableSeats > 0) {
            System.out.println("Available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "': " + availableSeats);
        } else {
            System.out.println("No available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "'.");
        }
    }

    // Recommends a seat in a venue based on customer preferences
    public void recommendSeat(Customer customer, Venue venue, Event event) {
        Seat recommendedSeat = venueService.recommendSeat(customer, venue, event);
        if (recommendedSeat != null) {
            System.out.println("Recommended seat for customer '" + customer.getUsername() + "' in venue '" + venue.getVenueName() + "' for event '" + event.getEventName() + "': Row " + recommendedSeat.getRowNumber() + ", Seat " + recommendedSeat.getSeatNumber());
        } else {
            System.out.println("No preferred seat available for customer '" + customer.getUsername() + "' in venue '" + venue.getVenueName() + "' for event '" + event.getEventName() + "'.");
        }
    }

    // Finds a venue by its ID
    public Venue findVenueById(int id) {
        Venue venue = venueService.findVenueByID(id);
        if (venue != null) {
            System.out.println("Venue found: " + venue.getVenueName() + " at " + venue.getLocation());
        } else {
            System.out.println("Venue with ID " + id + " not found.");
        }
        return venue;
    }

    // Retrieves a list of available seats in the venue for a specific event
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
