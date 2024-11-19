package controller;

import model.Customer;
import model.Event;
import model.Seat;
import model.Section;
import model.Venue;
import repository.IRepository;
import service.SectionService;

import java.util.List;

/**
 * The SectionController class provides methods to handle operations related to sections within a venue,
 * including seat availability, seat recommendations, section creation, and section information retrieval.
 */
public class SectionController {
    private final SectionService sectionService;
    private final IRepository<Section> sectionRepository;

    /**
     * Constructs a SectionController with the specified SectionService.
     *
     * @param sectionService the service handling section-related operations
     */
    public SectionController(SectionService sectionService, IRepository<Section> sectionRepository ) {
        this.sectionService = sectionService;
        this.sectionRepository = sectionRepository;
    }

    /**
     * Retrieves and displays available seats in the specified section for a given event.
     *
     * @param section the section in which to check for available seats
     * @param event   the event for which seat availability is checked
     */
    public void getAvailableSeats(Section section, Event event) {
        List<Seat> availableSeats = sectionService.getAvailableSeats(section, event);
        if (availableSeats.isEmpty()) {
            System.out.println("No available seats in section '" + section.getSectionName() + "' for event '" + event.getEventName() + "'.");
        } else {
            System.out.println("Available seats in section '" + section.getSectionName() + "' for event '" + event.getEventName() + "':");
            availableSeats.forEach(seat -> System.out.println("- Row " + seat.getRowNumber() + ", Seat " + seat.getSeatNumber()));
        }
    }

    /**
     * Recommends a seat in the specified section based on customer preferences for a given event.
     *
     * @param customer the customer for whom the seat is recommended
     * @param section  the section in which to recommend a seat
     * @param event    the event for which the seat recommendation is made
     */
    public void recommendSeat(Customer customer, Section section, Event event) {
        Seat recommendedSeat = sectionService.recommendSeat(customer, section, event);
        if (recommendedSeat != null) {
            System.out.println("Recommended seat for customer '" + customer.getUsername() + "' in section '" + section.getSectionName() + "' for event '" + event.getEventName() + "': Row " + recommendedSeat.getRowNumber() + ", Seat " + recommendedSeat.getSeatNumber());
        } else {
            System.out.println("No preferred seat available for customer '" + customer.getUsername() + "' in section '" + section.getSectionName() + "' for event '" + event.getEventName() + "'.");
        }
    }

    /**
     * Creates a new section within the specified venue, with a specified number of rows and seats per row.
     *
     * @param sectionName     the name of the section to be created
     * @param sectionId       the unique ID of the section
     * @param sectionCapacity the total capacity of the section
     * @param rowCount        the number of rows in the section
     * @param seatsPerRow     the number of seats per row in the section
     * @param venue           the venue where the section is created
     * @return the created Section object
     */
    public Section createSectionWithSeats(String sectionName, int sectionId, int sectionCapacity, int rowCount, int seatsPerRow, Venue venue) {
        Section section = sectionService.createSectionWithSeats(sectionName, sectionId, sectionCapacity, rowCount, seatsPerRow, venue);
        System.out.println("Section '" + sectionName + "' created in venue '" + venue.getVenueName() + "' with " + rowCount + " rows and " + seatsPerRow + " seats per row.");
        return section;
    }

    /**
     * Retrieves and displays information about the specified section, including its total capacity
     * and the number of available seats for a given event.
     *
     * @param section the section for which information is retrieved
     * @param event   the event for which available seats are counted
     */
    public void getSectionInfo(Section section, Event event) {
        int availableSeatsCount = sectionService.getAvailableSeats(section, event).size();
        System.out.println("Section '" + section.getSectionName() + "' has a capacity of " + section.getSectionCapacity() + " seats. Currently available seats for event '" + event.getEventName() + "': " + availableSeatsCount);
    }

    /**
     * Finds a section by its unique ID.
     *
     * @param sectionId the ID of the section to find.
     * @return the Section object if found, or null if no such section exists.
     */
    public Section findSectionById(int sectionId) {
        List<Section> sections = sectionRepository.getAll();
        for (Section section : sections) {
            if (section.getID() == sectionId) {
                return section;
            }
        }
        return null;
    }
}
