package service;

import model.Event;
import model.EventStatus;
import model.Venue;
import repository.IRepository;

import java.util.List;

public class EventService {
    private final IRepository<Event> eventIRepository;
    private final VenueService venueService;

    public EventService(IRepository<Event> eventIRepository, VenueService venueService) {
        this.eventIRepository = eventIRepository;
        this.venueService = venueService;
    }
}
