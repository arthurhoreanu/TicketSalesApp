package controller;

import model.Customer;
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
        List<Seat> availableSeats = sectionService.getAvailableSeats(section, event);
        System.out.println("Retrieved " + availableSeats.size() + " available seats for event '"
                + event.getEventName() + "' in section '" + section.getSectionName() + "'.");
        return availableSeats;
    }

    // Recommends a seat in a section based on customer preferences for a specific event
    public Seat recommendSeat(Customer customer, Section section, Event event) {
        Seat recommendedSeat = sectionService.recommendSeat(customer, section, event);
        if (recommendedSeat != null) {
            System.out.println("Recommended seat " + recommendedSeat.getRowNumber() + "-" + recommendedSeat.getSitNumber() +
                    " for customer '" + customer.getUsername() + "' in section '" + section.getSectionName() + "' for event '"
                    + event.getEventName() + "'.");
        } else {
            System.out.println("No recommended seat found for customer '" + customer.getUsername() +
                    "' in section '" + section.getSectionName() + "' for event '" + event.getEventName() + "'.");
        }
        return recommendedSeat;
    }

    // Adds a new seat to a section
    public void addSeatToSection(Section section, Seat seat) {
        section.getSeats().add(seat);
        System.out.println("Added seat " + seat.getRowNumber() + "-" + seat.getSitNumber() + " to section '" + section.getSectionName() + "'.");
    }

    // Removes a seat from a section
    public boolean removeSeatFromSection(Section section, Seat seat) {
        boolean removed = section.getSeats().remove(seat);
        if (removed) {
            System.out.println("Removed seat " + seat.getRowNumber() + "-" + seat.getSitNumber() + " from section '" + section.getSectionName() + "'.");
        } else {
            System.out.println("Seat " + seat.getRowNumber() + "-" + seat.getSitNumber() + " not found in section '" + section.getSectionName() + "'.");
        }
        return removed;
    }

    // Checks if a section is at full capacity for a specific event
    public boolean isSectionFull(Section section, Event event) {
        boolean isFull = sectionService.getAvailableSeats(section, event).isEmpty();
        if (isFull) {
            System.out.println("Section '" + section.getSectionName() + "' is at full capacity for event '" + event.getEventName() + "'.");
        } else {
            System.out.println("Section '" + section.getSectionName() + "' has available seats for event '" + event.getEventName() + "'.");
        }
        return isFull;
    }
}
