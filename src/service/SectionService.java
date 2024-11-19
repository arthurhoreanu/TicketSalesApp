package service;

import model.*;
import repository.IRepository;


import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing section-related operations, including seat availability and recommendations.
 */
public class SectionService {
    private final SeatService seatService;
    private final IRepository<Section> sectionRepository;


    /**
     * Constructs a SectionService with the specified SeatService dependency.
     *
     * @param seatService the SeatService used for managing seat-related operations.
     */
    public SectionService(SeatService seatService, IRepository<Section> sectionRepository) {
        this.seatService = seatService;
        this.sectionRepository = sectionRepository;
    }



    /**
     * Retrieves a list of available seats in a specified section for a specific event.
     * A seat is considered available if it is not reserved for the given event.
     *
     * @param section the section to check for available seats.
     * @param event   the event for which seat availability is being checked.
     * @return a list of available seats for the specified event in the section.
     */
    public List<Seat> getAvailableSeats(Section section, Event event) {
        List<Seat> availableSeats = new ArrayList<>();
        List<Seat> seats = section.getSeats();
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if (!seatService.isSeatReservedForEvent(seat, event)) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    /**
     * Recommends a seat within a section for a customer based on their preferences.
     * If the customer has a preference for this section, the first available seat is recommended.
     * Otherwise, a front-row seat is recommended as a fallback.
     *
     * @param customer the customer for whom the seat is being recommended.
     * @param section  the section in which the seat recommendation is made.
     * @param event    the event for which the seat is being recommended.
     * @return the recommended seat, or null if no seats are available.
     */
    public Seat recommendSeat(Customer customer, Section section, Event event) {
        List<Seat> availableSeats = getAvailableSeats(section, event);

        // Check if the customer has a preference for this section
        Integer preferredCount = customer.getPreferredSections().get(section.getID());
        if (preferredCount != null && preferredCount > 0) {
            return availableSeats.isEmpty() ? null : availableSeats.get(0);
        }

        // Fallback: Recommend the first front-row seat
        return seatService.recommendFrontRowSeat(availableSeats);
    }

    /**
     * Creates a new section with a specified number of rows and seats per row.
     * Each seat is assigned a unique ID based on its row and seat number.
     *
     * @param sectionName   the name of the section.
     * @param sectionId     the unique ID for the section.
     * @param sectionCapacity the total capacity of the section.
     * @param rowCount      the number of rows in the section.
     * @param seatsPerRow   the number of seats per row.
     * @param venue         the venue in which the section is located.
     * @return the created Section object with seats initialized.
     */
    public Section createSectionWithSeats(String sectionName, int sectionId, int sectionCapacity, int rowCount, int seatsPerRow, Venue venue) {
        List<Seat> seats = new ArrayList<>();
        Section section = new Section(sectionId, sectionName, sectionCapacity, venue, seats);

        for (int row = 1; row <= rowCount; row++) {
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                Seat seat = new Seat(row * 100 + seatNumber, row, section, seatNumber, null);
                seats.add(seat);
            }
        }
        return section;
    }



}
