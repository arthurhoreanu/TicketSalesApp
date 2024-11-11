package service;

import model.Event;
import model.Seat;
import model.Section;
import model.Customer;

import java.util.ArrayList;
import java.util.List;

public class SectionService {
    private final SeatService seatService;

    public SectionService(SeatService seatService) {
        this.seatService = seatService;
    }

    // Retrieves the list of available seats in a section for a specific event using for loop
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

    // Recommends a seat within a section based on customer preferences using for loop
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
}
