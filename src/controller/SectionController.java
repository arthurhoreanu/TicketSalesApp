package controller;

import model.Customer;
import model.Event;
import model.Seat;
import model.Section;
import model.Venue;
import service.SectionService;

import java.util.List;

public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // Retrieves available seats in a section for a specific event
    public void getAvailableSeats(Section section, Event event) {
        List<Seat> availableSeats = sectionService.getAvailableSeats(section, event);
        if (availableSeats.isEmpty()) {
            System.out.println("No available seats in section '" + section.getSectionName() + "' for event '" + event.getEventName() + "'.");
        } else {
            System.out.println("Available seats in section '" + section.getSectionName() + "' for event '" + event.getEventName() + "':");
            availableSeats.forEach(seat -> System.out.println("- Row " + seat.getRowNumber() + ", Seat " + seat.getSeatNumber()));
        }
    }

    // Recommends a seat in a section based on customer preferences for a specific event
    public void recommendSeat(Customer customer, Section section, Event event) {
        Seat recommendedSeat = sectionService.recommendSeat(customer, section, event);
        if (recommendedSeat != null) {
            System.out.println("Recommended seat for customer '" + customer.getUsername() + "' in section '" + section.getSectionName() + "' for event '" + event.getEventName() + "': Row " + recommendedSeat.getRowNumber() + ", Seat " + recommendedSeat.getSeatNumber());
        } else {
            System.out.println("No preferred seat available for customer '" + customer.getUsername() + "' in section '" + section.getSectionName() + "' for event '" + event.getEventName() + "'.");
        }
    }

    // Creates a new section with a specified number of rows and seats per row within a venue
    public Section createSectionWithSeats(String sectionName, int sectionId, int sectionCapacity, int rowCount, int seatsPerRow, Venue venue) {
        Section section = sectionService.createSectionWithSeats(sectionName, sectionId, sectionCapacity, rowCount, seatsPerRow, venue);
        System.out.println("Section '" + sectionName + "' created in venue '" + venue.getVenueName() + "' with " + rowCount + " rows and " + seatsPerRow + " seats per row.");
        return section;
    }


    // Retrieves information about the section, including total capacity and available seats
    public void getSectionInfo(Section section, Event event) {
        int availableSeatsCount = sectionService.getAvailableSeats(section, event).size();
        System.out.println("Section '" + section.getSectionName() + "' has a capacity of " + section.getSectionCapacity() + " seats. Currently available seats for event '" + event.getEventName() + "': " + availableSeatsCount);
    }
}
