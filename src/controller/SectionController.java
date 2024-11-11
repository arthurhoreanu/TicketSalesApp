package controller;

import model.Event;
import model.Seat;
import model.Section;
import service.SectionService;

import java.util.List;

public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // Retrieves available seats in a section for a specific event
    public List<Seat> getAvailableSeats(Section section, Event event) {
        return sectionService.getAvailableSeats(section, event);
    }

    // TODO: fix these errors
//    // Counts the number of available seats in a section for a specific event
//    public int countAvailableSeats(Section section, Event event) {
//        return sectionService.countAvailableSeats(section, event);
//    }
//
//    // Adds a new seat to the section
//    public void addSeat(Section section, Seat seat) {
//        sectionService.addSeat(section, seat);
//    }
//
//    // Removes a seat from the section
//    public boolean removeSeat(Section section, Seat seat) {
//        return sectionService.removeSeat(section, seat);
//    }
//
//    // Checks if a section is at full capacity for a specific event
//    public boolean isSectionFull(Section section, Event event) {
//        return sectionService.isSectionFull(section, event);
//    }
//
//    // Reserves a list of seats in the section for a specific event
//    public void reserveSeats(Section section, List<Seat> seatsToReserve, Event event) {
//        sectionService.reserveSeats(section, seatsToReserve, event);
//    }
//
//    // Releases all reserved seats in the section for a specific event
//    public void releaseAllSeats(Section section, Event event) {
//        sectionService.releaseAllSeats(section, event);
//    }
//
//    // Retrieves a list of seats that are reserved for a specific event
//    public List<Seat> getReservedSeats(Section section, Event event) {
//        return sectionService.getReservedSeats(section, event);
//    }
//
//    // Retrieves a list of recommended seats in a section for a specific event
//    // This could be implemented to recommend seats based on proximity to the stage, aisle, or based on seat grouping
//    public List<Seat> getRecommendedSeats(Section section, Event event) {
//        // Placeholder for a more sophisticated recommendation logic based on seat preferences
//        // Could recommend seats based on criteria like proximity, best view, or seat groupings
//        return sectionService.getAvailableSeats(section, event); // Basic implementation returns all available seats
//    }
}
