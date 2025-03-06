package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.model.event.Event;
import main.java.com.ticketsalesapp.model.ticket.TicketType;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.venue.Row;
import main.java.com.ticketsalesapp.model.venue.Seat;
import main.java.com.ticketsalesapp.model.venue.Section;
import main.java.com.ticketsalesapp.model.venue.Venue;
import main.java.com.ticketsalesapp.service.VenueService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing venue-related operations.
 */
@Component
public class VenueController {
    private final VenueService venueService;

    /**
     * Constructs a VenueController with the specified VenueService.
     *
     * @param venueService The service for managing venues.
     */
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    // Seat

    /**
     * Creates a new Seat in a specific Row.
     *
     * @param rowId      The ID of the Row.
     * @param seatNumber The number of the Seat.
     * @return The created Seat.
     */
    public Seat createSeat(int rowId, int seatNumber) {
        try {
            return venueService.createSeat(rowId, seatNumber);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create seat: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a Seat by its ID.
     *
     * @param seatId The ID of the Seat to delete.
     */
    public void deleteSeat(int seatId) {
        boolean deleted = venueService.deleteSeat(seatId);
        System.out.println(deleted ? "Seat deleted successfully." : "Failed to delete seat. Seat not found.");
    }

    /**
     * Deletes all Seats in a specific Row.
     *
     * @param rowId The ID of the Row.
     */
    public void deleteSeatsByRow(int rowId) {
        venueService.deleteSeatsByRow(rowId);
        System.out.println("All seats in Row ID " + rowId + " deleted successfully.");
    }

    /**
     * Retrieves a Seat by its ID.
     *
     * @param seatId The ID of the Seat.
     * @return The Seat object if found, null otherwise.
     */
    public Optional<Seat> findSeatByID(int seatId) {
        Optional<Seat> seat = venueService.findSeatByID(seatId);
        if (seat.isPresent()) {
            System.out.println("Seat found: " + seat);
        } else {
            System.out.println("Seat with ID " + seatId + " not found.");
        }
        return seat;
    }

    /**
     * Retrieves all Seats.
     *
     * @return A list of all Seats.
     */
    public List<Seat> getAllSeats() {
        List<Seat> seats = venueService.getAllSeats();
        if (!seats.isEmpty()) {
            seats.forEach(System.out::println);
        } else {
            System.out.println("No seats available.");
        }
        return seats;
    }

    /**
     * Reserves a Seat.
     *
     * @param seatId     The ID of the Seat to reserve.
     * @param event      The Event associated with the reservation.
     * @param customer   The Customer making the reservation.
     * @param price      The price of the reservation.
     * @param ticketType The type of Ticket.
     */
    public boolean reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        boolean reserved = venueService.reserveSeat(seatId, event, customer, price, ticketType);
        if (reserved) {
            System.out.println("Seat reserved successfully.");
        } else {
            System.out.println("Failed to reserve seat.");
        }
        return reserved;
    }


    /**
     * Unreserves a Seat.
     *
     * @param seatId The ID of the Seat to unreserve.
     */
    public void unreserveSeat(int seatId) {
        venueService.unreserveSeat(seatId);
        System.out.println("Seat unreserved successfully.");
    }

    /**
     * Checks if a Seat is reserved for a specific Event.
     *
     * @param seatId  The ID of the Seat.
     * @param eventId The ID of the Event.
     * @return True if the Seat is reserved for the Event, false otherwise.
     */
    public boolean isSeatReservedForEvent(int seatId, int eventId) {
        boolean isReserved = venueService.isSeatReservedForEvent(seatId, eventId);
        System.out.println(isReserved ? "The seat is reserved for the event." : "The seat is not reserved for the event.");
        return isReserved;
    }

    // Row

    /**
     * Creates a new Row in a specific Section.
     *
     * @param section   The Section to which the row belongs.
     * @param rowCapacity The capacity of the row.
     * @return The created Row.
     */
    public Row createRow(Section section, int rowCapacity) {
        try {
            Row row = venueService.createRow(section, rowCapacity);
            System.out.println("Row created successfully: " + row);
            return row;
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create row: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing Row.
     *
     * @param rowId       The ID of the row.
     * @param rowCapacity The new capacity of the row.
     * @return The updated Row.
     */
    public Optional<Row> updateRow(int rowId, int rowCapacity) {
        Optional<Row> updatedRow = venueService.updateRow(rowId, rowCapacity);
        if (updatedRow.isPresent()) {
            System.out.println("Row updated successfully: " + updatedRow);
        } else {
            System.out.println("Failed to update row. Row with ID " + rowId + " not found.");
        }
        return updatedRow;
    }

    /**
     * Deletes all Rows in a specific Section.
     *
     * @param sectionId The ID of the Section.
     */
    public void deleteRowsBySection(int sectionId) {
        try {
            venueService.deleteRowsBySection(sectionId);
            System.out.println("All rows in Section ID " + sectionId + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete rows: " + e.getMessage());
        }
    }

    /**
     * Deletes a Row by its ID.
     *
     * @param rowId The ID of the row.
     */
    public void deleteRow(int rowId) {
        try {
            venueService.deleteRow(rowId);
            System.out.println("Row with ID " + rowId + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete row: " + e.getMessage());
        }
    }

    /**
     * Retrieves a Row by its ID.
     *
     * @param rowId The ID of the row.
     * @return The Row object.
     */
    public Optional<Row> findRowByID(int rowId) {
        Optional<Row> row = venueService.findRowByID(rowId);
        if (row.isPresent()) {
            System.out.println("Row found: " + row);
        } else {
            System.out.println("Row with ID " + rowId + " not found.");
        }
        return row;
    }

    /**
     * Retrieves all Rows.
     *
     * @return A list of all Rows.
     */
    public List<Row> getAllRows() {
        List<Row> rows = venueService.getAllRows();
        if (!rows.isEmpty()) {
            System.out.println("All rows:");
            rows.forEach(System.out::println);
        } else {
            System.out.println("No rows available.");
        }
        return rows;
    }

    /**
     * Adds Seats to a specific Row.
     *
     * @param rowId         The ID of the row.
     * @param numberOfSeats The number of seats to add.
     */
    public void addSeatsToRow(int rowId, int numberOfSeats) {
        try {
            venueService.addSeatsToRow(rowId, numberOfSeats);
            System.out.println("Added " + numberOfSeats + " seats to Row with ID " + rowId + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add seats: " + e.getMessage());
        }
    }

    /**
     * Retrieves all Seats in a specific Row.
     *
     * @param rowId The ID of the row.
     * @return A list of Seats in the row.
     */
    public List<Seat> getSeatsByRow(int rowId) {
        List<Seat> seats = venueService.getSeatsByRow(rowId);
        if (!seats.isEmpty()) {
            System.out.println("Seats in Row ID " + rowId + ":");
            seats.forEach(System.out::println);
        } else {
            System.out.println("No seats found for Row ID " + rowId + ".");
        }
        return seats;
    }

    /**
     * Finds Rows by their associated Section.
     *
     * @param sectionId The ID of the section.
     * @return A list of Rows in the section.
     */
    public List<Row> findRowsBySection(int sectionId) {
        List<Row> rows = venueService.findRowsBySection(sectionId);
        if (!rows.isEmpty()) {
            System.out.println("Rows in Section ID " + sectionId + ":");
            rows.forEach(System.out::println);
        } else {
            System.out.println("No rows found for Section ID " + sectionId + ".");
        }
        return rows;
    }

    /**
     * Retrieves all available Seats in a Row for a specific Event.
     *
     * @param rowId   The ID of the row.
     * @param eventId The ID of the event.
     * @return A list of available Seats.
     */
    public List<Seat> getAvailableSeatsInRow(int rowId, int eventId) {
        List<Seat> availableSeats = venueService.getAvailableSeatsInRow(rowId, eventId);
        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats in Row ID " + rowId + " for Event ID " + eventId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats in Row ID " + rowId + " for Event ID " + eventId + ".");
        }
        return availableSeats;
    }

    /**
     * Recommends the closest available Seat in a Row.
     *
     * @param rowId      The ID of the row.
     * @return The recommended Seat.
     */
    public Seat recommendClosestSeat(int sectionId, int rowId, List<Integer> selectedSeatNumbers) {
        // Call the updated venueService method with sectionId, rowId, and the list of selected seat numbers
        Seat recommendedSeat = venueService.recommendClosestSeat(sectionId, rowId, selectedSeatNumbers);

        // Display appropriate message based on the recommendation result
        if (recommendedSeat != null) {
            System.out.println("Recommended seat: " + recommendedSeat);
        } else {
            System.out.println("No suitable seat found in Section ID " + sectionId + ", Row ID " + rowId + ".");
        }

        return recommendedSeat;
    }








    // Section

    /**
     * Creates a new Section.
     *
     * @param venue          The venue to which the section belongs.
     * @param sectionCapacity The capacity of the section.
     * @param sectionName    The name of the section.
     * @return The created Section.
     */
    public Section createSection(Venue venue, int sectionCapacity, String sectionName) {
        try {
            Section section = venueService.createSection(venue, sectionCapacity, sectionName);
            System.out.println("Section created successfully: " + section);
            return section;
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create section: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing Section.
     *
     * @param sectionId       The ID of the section to update.
     * @param sectionName     The new name for the section.
     * @param sectionCapacity The new capacity for the section.
     * @return The updated Section.
     */
    public Section updateSection(int sectionId, String sectionName, int sectionCapacity) {
        Section updatedSection = (Section) venueService.updateSection(sectionId, sectionName, sectionCapacity);
        if (updatedSection != null) {
            System.out.println("Section updated successfully: " + updatedSection);
        } else {
            System.out.println("Failed to update section. Section with ID " + sectionId + " not found.");
        }
        return updatedSection;
    }

    /**
     * Deletes a Section by its ID.
     *
     * @param sectionId The ID of the section to delete.
     */
    public void deleteSection(int sectionId) {
        try {
            venueService.deleteSection(sectionId);
            System.out.println("Section with ID " + sectionId + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete section: " + e.getMessage());
        }
    }

    /**
     * Deletes all Sections for a Venue.
     *
     * @param venueId The ID of the venue.
     */
    public void deleteSectionsByVenue(int venueId) {
        venueService.deleteSectionByVenue(venueId);
        System.out.println("All sections for Venue ID " + venueId + " deleted successfully.");
    }

    /**
     * Retrieves a Section by its ID.
     *
     * @param sectionId The ID of the section to retrieve.
     * @return The Section object if found, null otherwise.
     */
    public Optional<Section> findSectionByID(int sectionId) {
        Optional<Section> section = venueService.findSectionByID(sectionId);
        if (section.isPresent()) {
            System.out.println("Section found: " + section);
        } else {
            System.out.println("Section with ID " + sectionId + " not found.");
        }
        return section;
    }

    /**
     * Retrieves all Sections.
     *
     * @return A list of all Sections.
     */
    public List<Section> getAllSections() {
        List<Section> sections = venueService.getAllSections();
        if (!sections.isEmpty()) {
            System.out.println("All sections:");
            sections.forEach(System.out::println);
        } else {
            System.out.println("No sections available.");
        }
        return sections;
    }

    /**
     * Adds rows to an existing Section.
     *
     * @param sectionId    The ID of the section.
     * @param numberOfRows The number of rows to add.
     * @param rowCapacity  The capacity of each row.
     */
    public void addRowsToSection(int sectionId, int numberOfRows, int rowCapacity) {
        try {
            venueService.addRowsToSection(sectionId, numberOfRows, rowCapacity);
            System.out.println("Added " + numberOfRows + " rows to Section ID " + sectionId + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add rows: " + e.getMessage());
        }
    }

    /**
     * Finds Sections by their name.
     *
     * @param name The name of the sections to find.
     * @return A list of matching Sections.
     */
    public List<Section> findSectionsByName(String name) {
        List<Section> sections = venueService.findSectionsByName(name);
        if (!sections.isEmpty()) {
            System.out.println("Sections found:");
            sections.forEach(System.out::println);
        } else {
            System.out.println("No sections found with name: " + name);
        }
        return sections;
    }

    /**
     * Retrieves all available Seats in a Section for a specific Event.
     *
     * @param sectionId The ID of the section.
     * @param eventId   The ID of the event.
     * @return A list of available Seats.
     */
    public List<Seat> getAvailableSeatsInSection(int sectionId, int eventId) {
        List<Seat> availableSeats = venueService.getAvailableSeatsInSection(sectionId, eventId);
        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats in Section ID " + sectionId + " for Event ID " + eventId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats in Section ID " + sectionId + " for Event ID " + eventId + ".");
        }
        return availableSeats;
    }

    //

    /**
     * Creates a new venue.
     */
    public Venue createVenue(String name, String location, int capacity, boolean hasSeats) {
        try {
            Venue venue = venueService.createVenue(name, location, capacity, hasSeats);
            System.out.println("Venue created successfully: " + venue);
            return venue;
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating venue: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a venue by its ID.
     */
    public Optional<Venue> findVenueByID(int venueId) {
        Optional<Venue> venue = venueService.findVenueByID(venueId);
        if (venue.isPresent()) {
            System.out.println("Venue found: " + venue);
        } else {
            System.out.println("Venue with ID " + venueId + " not found.");
        }
        return venue;
    }

    /**
     * Finds venues by location or name.
     */
    public List<Venue> findVenuesByLocationOrName(String keyword) {
        List<Venue> venues = venueService.findVenuesByLocationOrName(keyword);
        if (!venues.isEmpty()) {
            System.out.println("Venues found:");
            venues.forEach(System.out::println);
        } else {
            System.out.println("No venues found for the keyword: " + keyword);
        }
        return venues;
    }

    /**
     * Finds a venue by its name.
     */
    public Venue findVenueByName(String name) {
        Venue venue = venueService.findVenueByName(name);
        if (venue != null) {
            System.out.println("Venue found: " + venue);
        } else {
            System.out.println("Venue with name '" + name + "' not found.");
        }
        return venue;
    }

    /**
     * Retrieves all venues.
     */
    public List<Venue> getAllVenues() {
        List<Venue> venues = venueService.getAllVenues();
        if (!venues.isEmpty()) {
            System.out.println("All venues:");
            venues.forEach(System.out::println);
        } else {
            System.out.println("No venues available.");
        }
        return venues;
    }

    /**
     * Updates an existing venue.
     */
    public Venue updateVenue(int venueId, String name, String location, int capacity, boolean hasSeats) {
        Venue updatedVenue = (Venue) venueService.updateVenue(venueId, name, location, capacity, hasSeats);
        if (updatedVenue != null) {
            System.out.println("Venue updated successfully: " + updatedVenue);
        } else {
            System.out.println("Failed to update venue. Venue with ID " + venueId + " not found.");
        }
        return updatedVenue;
    }

    /**
     * Deletes a venue by its ID.
     */
    public boolean deleteVenue(int venueId) {
        boolean deleted = venueService.deleteVenue(venueId);
        if (deleted) {
            System.out.println("Venue with ID " + venueId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete venue. Venue with ID " + venueId + " not found.");
        }
        return deleted;
    }

    /**
     * Adds multiple sections to a venue.
     */
    public void addSectionToVenue(int venueId, int numberOfSections, int sectionCapacity, String defaultSectionName) {
        try {
            venueService.addSectionToVenue(venueId, numberOfSections, sectionCapacity, defaultSectionName);
            System.out.println("Added " + numberOfSections + " sections to Venue ID " + venueId);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Retrieves all sections by venue ID.
     */
    public List<Section> getSectionsByVenueID(int venueId) {
        List<Section> sections = venueService.getSectionsByVenueID(venueId);
        if (!sections.isEmpty()) {
            System.out.println("Sections for venue ID " + venueId + ":");
            sections.forEach(System.out::println);
        } else {
            System.out.println("No sections found for venue ID " + venueId + ".");
        }
        return sections;
    }

    /**
     * Retrieves all available seats in a venue for a specific event.
     */
    public List<Seat> getAvailableSeatsInVenue(int venueId, int eventId) {
        List<Seat> availableSeats = venueService.getAvailableSeatsInVenue(venueId, eventId);
        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats for venue ID " + venueId + " and event ID " + eventId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats for venue ID " + venueId + " and event ID " + eventId + ".");
        }
        return availableSeats;
    }

    /**
     * Loads the sections for a specific venue and populates the venue object with its sections.
     *
     * @param venue The venue for which to load sections.
     */
    public void loadSectionsForVenue(Venue venue) {
        venueService.loadSectionsForVenue(venue);
        System.out.println("Sections loaded for Venue ID " + venue.getID() + ".");
    }
}
