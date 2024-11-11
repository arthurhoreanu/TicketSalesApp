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
    public String addVenue(String name, String location, int capacity, List<Section> sections) {
        boolean isAdded = venueService.addVenue(name, location, capacity, sections);
        if (isAdded) {
            return "Venue successfully added: " + name + " at " + location;
        } else {
            return "Venue with the same name and location already exists. Addition failed.";
        }
    }

    // Creates a new venue with sections and seats
    public String createVenueWithSectionsAndSeats(String name, String location, int capacity, int sectionCapacity, int rowCount, int seatsPerRow) {
        Venue venue = venueService.createVenueWithSectionsAndSeats(name, location, capacity, sectionCapacity, rowCount, seatsPerRow);
        return "Venue '" + venue.getVenueName() + "' created with " + venue.getSections().size() + " sections, each containing " + seatsPerRow + " seats per row.";
    }

    // Updates an existing venue
    public String updateVenue(int id, String newName, String newLocation, int newCapacity, List<Section> newSections) {
        boolean isUpdated = venueService.updateVenue(id, newName, newLocation, newCapacity, newSections);
        if (isUpdated) {
            return "Venue updated successfully: " + newName + " at " + newLocation;
        } else {
            return "Venue with ID " + id + " not found. Update failed.";
        }
    }

    // Deletes a venue by ID
    public String deleteVenue(int id) {
        boolean isDeleted = venueService.deleteVenue(id);
        if (isDeleted) {
            return "Venue with ID " + id + " has been successfully deleted.";
        } else {
            return "Venue with ID " + id + " not found. Deletion failed.";
        }
    }

    // Retrieves a list of all venues
    public String getAllVenues() {
        List<Venue> venues = venueService.getAllVenues();
        if (venues.isEmpty()) {
            return "No venues found in the repository.";
        }
        StringBuilder sb = new StringBuilder("Venues in the repository:\n");
        for (Venue venue : venues) {
            sb.append("- ").append(venue.getVenueName()).append(" at ").append(venue.getLocation()).append("\n");
        }
        return sb.toString();
    }

    // Finds a venue by name
    public String findVenueByName(String name) {
        Venue venue = venueService.findVenueByName(name);
        if (venue != null) {
            return "Venue found: " + venue.getVenueName() + " at " + venue.getLocation();
        } else {
            return "Venue with name '" + name + "' not found.";
        }
    }

    // Finds venues by location or name
    public String getVenuesByLocationOrName(String locationOrVenueName) {
        List<Venue> venues = venueService.getVenuesByLocationOrName(locationOrVenueName);
        if (venues.isEmpty()) {
            return "No venues found for location or name: " + locationOrVenueName;
        }
        StringBuilder sb = new StringBuilder("Matching venues:\n");
        for (Venue venue : venues) {
            sb.append("- ").append(venue.getVenueName()).append(" at ").append(venue.getLocation()).append("\n");
        }
        return sb.toString();
    }

    // Checks the number of available seats for a specific event in a venue
    public String getAvailableSeats(Venue venue, Event event) {
        int availableSeats = venueService.getAvailableSeats(venue, event);
        if (availableSeats > 0) {
            return "Available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "': " + availableSeats;
        } else {
            return "No available seats for event '" + event.getEventName() + "' in venue '" + venue.getVenueName() + "'.";
        }
    }

    // Recommends a seat in a venue based on customer preferences
    public String recommendSeat(Customer customer, Venue venue, Event event) {
        Seat recommendedSeat = venueService.recommendSeat(customer, venue, event);
        if (recommendedSeat != null) {
            return "Recommended seat for customer '" + customer.getUsername() + "' in venue '" + venue.getVenueName() + "' for event '" + event.getEventName() + "': Row " + recommendedSeat.getRowNumber() + ", Seat " + recommendedSeat.getSitNumber();
        } else {
            return "No preferred seat available for customer '" + customer.getUsername() + "' in venue '" + venue.getVenueName() + "' for event '" + event.getEventName() + "'.";
        }
    }
}
