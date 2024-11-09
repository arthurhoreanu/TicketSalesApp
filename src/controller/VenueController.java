package controller;


import model.Event;
import model.Section;
import model.Venue;
import service.VenueService;

import java.util.List;

public class VenueController {
    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    public void addVenue(String name, String location, int capacity, List<Section> sections) {
        if(name == null || name.isEmpty() || location == null || capacity < 0 || sections == null || sections.isEmpty()) {
            System.out.println("All fields are required for account creation.");
        }
        boolean success = venueService.addVenue(name, location, capacity, sections);
        if(success) {
            System.out.println("Venue added successfully.");
        }
        else {
            System.out.println("Venue could not be added.");
        }
    }

    public void updateVenue(int id, String name, String location, int capacity, List<Section> sections) {
        boolean success = venueService.updateVenue(id, name, location, capacity, sections);
        if(success) {
            System.out.println("Venue updated successfully.");
        }
        else {
            System.out.println("Venue could not be updated.");
        }
    }

    public void deleteVenue(int id) {
        boolean success = venueService.deleteVenue(id);
        if(success) {
            System.out.println("Venue deleted successfully.");
        }
        else {
            System.out.println("Venue could not be deleted.");
        }
    }

    public List<Venue> getAllVenues() {
        return venueService.getAllVenues();
    }

    public Venue findVenueByName(String name) {
        return venueService.findVenueByName(name);
    }

    public int getAvailableSeats(Venue venue, Event event) {
        return venueService.getAvailableSeats(venue, event);
    }
}
