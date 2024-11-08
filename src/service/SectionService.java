package service;

import model.Event;
import model.Seat;
import model.Section;

import java.util.ArrayList;
import java.util.List;


public class SectionService {
    public List<Seat> getAvailableSeats(Section section, Event event) {
        List<Seat> availableSeats = new ArrayList<>();
        List<Seat> seats = section.getSeats();

        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            //TODO should we have the isReservedForEvent method and the other methods in the model??
            if (!seat.isReservedForEvent(event)) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }
}
