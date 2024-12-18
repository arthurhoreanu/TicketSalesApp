package com.ticketsalescompany.service;

import com.ticketsalescompany.model.*;
import com.ticketsalescompany.repository.FileRepository;
import com.ticketsalescompany.repository.IRepository;
import com.ticketsalescompany.repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VenueService {
    private final IRepository<Venue> venueRepository;
    private final FileRepository<Venue> venueFileRepository;
    private final DBRepository<Venue> venueDatabaseRepository;
    private final SectionService sectionService;

    /**
     * Constructs a VenueService with the specified dependencies.
     *
     * @param venueRepository the unified repository for managing venues.
     * @param sectionService  the service for managing sections within venues.
     */
    public VenueService(IRepository<Venue> venueRepository, SectionService sectionService) {
        this.venueRepository = venueRepository;
        this.sectionService = sectionService;

        // Initialize file repository for CSV operations
        this.venueFileRepository = new FileRepository<>("src/repository/data/venues.csv", Venue::fromCsv);
        syncFromCsv();

        // Initialize database repository
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.venueDatabaseRepository = new DBRepository<>(entityManagerFactory, Venue.class);
        syncFromDatabase();
    }

    /**
     * Syncs venues from the CSV file into the unified repository.
     */
    private void syncFromCsv() {
        List<Venue> venues = venueFileRepository.getAll();
        for (Venue venue : venues) {
            venueRepository.create(venue);
        }
    }

    /**
     * Syncs venues from the database into the unified repository.
     */
    private void syncFromDatabase() {
        List<Venue> venues = venueDatabaseRepository.getAll();
        for (Venue venue : venues) {
            venueRepository.create(venue);
        }
    }

    /**
     * Creates a new venue and adds it to all repositories.
     *
     * @param name     the name of the venue.
     * @param location the location of the venue.
     * @param capacity the capacity of the venue.
     * @return true if the venue was successfully created, false if a duplicate exists.
     */
    public boolean createVenue(String name, String location, int capacity) {
        // Check for duplicates
        for (Venue existingVenue : venueRepository.getAll()) {
            if (existingVenue.getVenueName().equalsIgnoreCase(name) &&
                    existingVenue.getLocation().equalsIgnoreCase(location)) {
                return false; // Duplicate venue
            }
        }

        Venue newVenue = new Venue();
        newVenue.setVenueName(name);
        newVenue.setLocation(location);
        newVenue.setVenueCapacity(capacity);

        // Add to all repositories
        venueRepository.create(newVenue);
        venueFileRepository.create(newVenue);
        venueDatabaseRepository.create(newVenue);
        return true;
    }

    /**
     * Updates an existing venue's details.
     *
     * @param venueId    the ID of the venue to update.
     * @param newName    the new name of the venue.
     * @param newLocation the new location of the venue.
     * @param newCapacity the new capacity of the venue.
     * @return true if the venue was updated, false otherwise.
     */
    public boolean updateVenue(int venueId, String newName, String newLocation, int newCapacity) {
        Venue venue = findVenueByID(venueId);
        if (venue != null) {
            venue.setVenueName(newName);
            venue.setLocation(newLocation);
            venue.setVenueCapacity(newCapacity);

            // Update in all repositories
            venueRepository.update(venue);
            venueFileRepository.update(venue);
            venueDatabaseRepository.update(venue);
            return true;
        }
        return false;
    }

    /**
     * Deletes a venue by its ID.
     *
     * @param venueId the ID of the venue to delete.
     * @return true if the venue was deleted, false otherwise.
     */
    public boolean deleteVenue(int venueId) {
        Venue venue = findVenueByID(venueId);
        if (venue != null) {
            // Remove from all repositories
            venueRepository.delete(venueId);
            venueFileRepository.delete(venueId);
            venueDatabaseRepository.delete(venueId);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all venues.
     *
     * @return a list of all venues in the repository.
     */
    public List<Venue> getAllVenues() {
        return venueRepository.getAll();
    }

    /**
     * Finds a venue by its ID.
     *
     * @param venueId the ID of the venue to find.
     * @return the Venue object if found, null otherwise.
     */
    public Venue findVenueByID(int venueId) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getID() == venueId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a venue by its name.
     *
     * @param name the name of the venue to find.
     * @return the Venue object if found, null otherwise.
     */
    public Venue findVenueByName(String name) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getVenueName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all venues that match the specified location or name.
     *
     * @param locationOrName the location or name to search for.
     * @return a list of matching venues.
     */
    public List<Venue> findVenuesByLocationOrName(String locationOrName) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getVenueName().equalsIgnoreCase(locationOrName) ||
                        venue.getLocation().equalsIgnoreCase(locationOrName))
                .collect(Collectors.toList());
    } //Collectors.toList() --> collects elements into a list,

    /**
     * Calculates the total number of available seats for a specific event in a venue.
     *
     * @param venue the venue to check.
     * @param event the event to calculate availability for.
     * @return the total number of available seats.
     */
    public List<Seat> getAvailableSeats(Venue venue, Event event) {
        return venue.getSections().stream()
                .flatMap(section -> sectionService.getAvailableSeats(section, event).stream())
                .collect(Collectors.toList());
    }


   /* *//**
     * Recommends a seat for a customer in a specific venue for an event.
     *
     * @param customer the customer for whom to recommend a seat.
     * @param venue    the venue in which to recommend a seat.
     * @param event    the event to recommend a seat for.
     * @return the recommended seat, or null if none are available.
     *//*
    public Seat recommendSeat(Customer customer, Venue venue, Event event) {
        return venue.getSections().stream()
                .map(section -> sectionService.recommendSeat(customer, section, event))
                .filter(seat -> seat != null)
                .findFirst()
                .orElse(null);
    }*/

}
