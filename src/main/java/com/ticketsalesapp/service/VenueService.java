package main.java.com.ticketsalesapp.service;

import lombok.RequiredArgsConstructor;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.EntityNotFoundException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.event.Event;
import main.java.com.ticketsalesapp.model.ticket.Ticket;
import main.java.com.ticketsalesapp.model.ticket.TicketType;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.venue.Row;
import main.java.com.ticketsalesapp.model.venue.Seat;
import main.java.com.ticketsalesapp.model.venue.Section;
import main.java.com.ticketsalesapp.model.venue.Venue;
import main.java.com.ticketsalesapp.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final Repository<Venue> venueRepository;
    private final Repository<Section> sectionRepository;
    private final Repository<Row> rowRepository;
    private final Repository<Seat> seatRepository;

    // Seat

    public void updateSeat(Seat seat) {
        if (seat == null) {
            throw new ValidationException("Seat cannot be null.");
        }
        if (findRowByID(seat.getRow().getID()) == null) {
            throw new BusinessLogicException("Row associated with the seat does not exist.");
        }
        seatRepository.update(seat);
    }

    /**
     * Creates a new Seat and saves it to all repositories.
     *
     * @param rowId      the ID of the Row to which the Seat belongs.
     * @param seatNumber the number of the Seat.
     * @return the created Seat object.
     */
    public Seat createSeat(int rowId, int seatNumber) {
        Optional<Row> row = findRowByID(rowId);
        if (row == null) {
            throw new EntityNotFoundException("Row not found");
        }
        Seat seat = new Seat();
        seat.setNumber(seatNumber);
        seat.setRow(row.get());
        row.get().addSeat(seat);
        seatRepository.create(seat);
        return seat;
    }

    public void deleteSeatsByRow(int rowId) {
        List<Seat> seats = seatRepository.getAll().stream()
                .filter(seat -> seat.getRow().getID() == rowId)
                .toList();
        for (Seat seat : seats) {
            seatRepository.delete(seat.getID());
        }
    }

    /**
     * Deletes a Seat by its ID.
     *
     * @param seatID the ID of the Seat to delete.
     * @return true if the Seat was successfully deleted, false otherwise.
     */
    public boolean deleteSeat(int seatID) {
        Optional<Seat> seat = findSeatByID(seatID);
        if (seat.isEmpty()) {
            throw new EntityNotFoundException("Seat not found");
        }
        Row row = seat.get().getRow();
        if (row != null) {
            row.removeSeat(seat);
        }
        seatRepository.delete(seatID);
        return true;
    }

    /**
     * Retrieves a Seat by its ID.
     *
     * @param seatId the ID of the Seat to retrieve.
     * @return the Seat object, or null if not found.
     */
    public Optional<Seat> findSeatByID(int seatId) {
        return seatRepository.read(seatId);
    }

    /**
     * Retrieves all Seats from the repository.
     *
     * @return a list of all Seats.
     */
    public List<Seat> getAllSeats() {
        return seatRepository.getAll();
    }

    /**
     * Reserves a Seat by associating it with a Ticket.
     *
     * @param seatId the ID of the Seat to reserve.
     * @return true if the Seat was successfully reserved, false otherwise.
     */
    public boolean reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        Optional<Seat> seatOptional = findSeatByID(seatId);
        if (seatOptional.isEmpty()) {
            throw new EntityNotFoundException("Seat not found");
        }
        Seat seat = seatOptional.get();
        if (seat.isReserved()) {
            throw new ValidationException("Seat with ID " + seatId + " is already reserved.");
        }
        if (event == null) {
            throw new ValidationException("Event cannot be null.");
        }
        if (customer == null) {
            throw new ValidationException("Customer cannot be null.");
        }
        Ticket ticket = new Ticket();
        ticket.setSeat(seat);
        ticket.setEvent(event);
        ticket.setCustomer(customer);
        ticket.setPrice(price);
        ticket.setTicketType(ticketType);
        seat.setReserved(true);
        seat.setTicket(ticket);
        seatRepository.update(seat);
        return true;
    }

    /**
     * Unreserves a Seat, removing its association with any Ticket.
     *
     * @param seatId the ID of the Seat to unreserve.
     */
    public void unreserveSeat(int seatId) {
        Optional<Seat> seatOptional = findSeatByID(seatId);
        if (seatOptional.isEmpty()) {
            throw new EntityNotFoundException("Seat not found");
        }
        Seat seat = seatOptional.get();
        if (!seat.isReserved()) {
            throw new ValidationException("Seat is not reserved.");
        }
        seat.setReserved(false);
        seat.setTicket(null);
        seatRepository.update(seat);
    }

    /**
     * Checks if a Seat is reserved for a specific Event.
     *
     * @param seatId  the ID of the Seat.
     * @param eventId the ID of the Event.
     * @return true if the Seat is reserved for the Event, false otherwise.
     */
    public boolean isSeatReservedForEvent(int seatId, int eventId) {
        Optional<Seat> seatOptional = findSeatByID(seatId);
        if (seatOptional.isEmpty()) {
            return false;
        }
        Seat seat = seatOptional.get();
        return seat.isReserved()
                && seat.getTicket() != null
                && seat.getTicket().getEvent().getID() == eventId;
    }

    public boolean isSeatForEvent(Seat seat, int eventId) {
        return seat.getTicket() != null && seat.getTicket().getEvent().getID() == eventId;
    }

    // Row

    public Row createRow(Section section, int rowCapacity) {
        if (section == null) {
            throw new BusinessLogicException("Section cannot be null.");
        }
        Row row = new Row(0, rowCapacity, section);
        rowRepository.create(row);
        section.addRow(row);
        return row;
    }

    public Optional<Row> updateRow(int rowId, int rowCapacity) {
        Optional<Row> rowOptional = findRowByID(rowId);
        if (rowOptional.isEmpty()) {
            throw new EntityNotFoundException("Row not found with ID: " + rowId);
        }
        Row row = rowOptional.get();
        row.setRowCapacity(rowCapacity);
        rowRepository.update(row);
        return Optional.of(row);
    }

    public void deleteRowsBySection(int sectionID) {
        List<Row> rows = rowRepository.getAll().stream().
                filter(row -> row.getSection().getID() == sectionID)
                .toList();
        for (Row row : rows) {
            deleteSeatsByRow(row.getID());
            rowRepository.delete(row.getID());
        }
    }

    public void deleteRow(int rowID) {
        Optional<Row> row = findRowByID(rowID);
        if (row == null) {
            throw new EntityNotFoundException("Row not found with ID: " + rowID);
        }
        deleteSeatsByRow(rowID);
        rowRepository.delete(rowID);
    }

    public Optional<Row> findRowByID(int rowId) {
        return rowRepository.read(rowId);
    }

    public List<Row> getAllRows() {
        return rowRepository.getAll();
    }

    /**
     * Adds a specified number of seats to a row.
     *
     * @param rowId         the ID of the row to which seats will be added
     * @param numberOfSeats the number of seats to add
     * @throws EntityNotFoundException if the row with the specified ID is not found
     */
    public void addSeatsToRow(int rowId, int numberOfSeats) {
        Optional<Row> rowOptional = findRowByID(rowId);
        if (rowOptional.isEmpty()) {
            throw new EntityNotFoundException("Row not found");
        }
        Row row = rowOptional.get();
        for (int i = 1; i <= numberOfSeats; i++) {
            Seat seat = new Seat(0, i, false, row); // Create the Seat object
            row.addSeat(seat); // Add the Seat to the Row's seats list
            seatRepository.create(seat); // Persist the Seat in the repository
        }
        rowRepository.update(row); // Persist the updated Row with its seats
    }

    /**
     * Finds rows within a specified section.
     *
     * @param sectionId the ID of the section
     * @return a list of rows in the specified section, or an empty list if the section is not found
     */
    public List<Row> findRowsBySection(int sectionId) {
        Optional<Section> section = findSectionByID(sectionId);
        return section.isPresent() ? section.get().getRows() : new ArrayList<>();
    }

    /**
     * Retrieves the available (non-reserved) seats in a specific row for a given event.
     *
     * @param rowId   the ID of the row
     * @param eventId the ID of the event
     * @return a list of available seats in the specified row and event, or an empty list if the row is not found
     */
    public List<Seat> getAvailableSeatsInRow(int rowId, int eventId) {
        Optional<Row> row = findRowByID(rowId);
        if (row == null) {
            return new ArrayList<>();
        }
        return row.get().getSeats().stream()
                .filter(seat -> !seat.isReserved() && isSeatForEvent(seat, eventId))
                .collect(Collectors.toList());
    }

    /**
     * Recommends the closest available seat to the selected seats in a specific row and section.
     *
     * @param sectionId           the ID of the section
     * @param rowId               the ID of the row
     * @param selectedSeatNumbers a list of seat numbers that have already been selected
     * @return the closest available seat, or null if no suitable seat is found
     */
    public Seat recommendClosestSeat(int sectionId, int rowId, List<Integer> selectedSeatNumbers) {
        Optional<Section> section = findSectionByID(sectionId);
        if (section.isEmpty()) {
            return null;
        }

        Row row = section.get().getRows().stream()
                .filter(r -> r.getID() == rowId)
                .findFirst()
                .orElse(null);
        if (row == null) {
            return null;
        }

        List<Seat> availableSeats = row.getSeats().stream()
                .filter(seat -> !seat.isReserved())
                .filter(seat -> !selectedSeatNumbers.contains(seat.getNumber()))
                .toList();

        if (availableSeats.isEmpty()) {
            return null;
        }

        List<Seat> sortedSeats = availableSeats.stream()
                .sorted((seat1, seat2) -> {
                    int minDistanceToSeat1 = selectedSeatNumbers.stream()
                            .mapToInt(selectedSeat -> Math.abs(seat1.getNumber() - selectedSeat))
                            .min().orElse(Integer.MAX_VALUE);

                    int minDistanceToSeat2 = selectedSeatNumbers.stream()
                            .mapToInt(selectedSeat -> Math.abs(seat2.getNumber() - selectedSeat))
                            .min().orElse(Integer.MAX_VALUE);

                    return Integer.compare(minDistanceToSeat1, minDistanceToSeat2);
                })
                .toList();

        return sortedSeats.get(0);
    }

    public List<Seat> getSeatsByRow(int rowId) {
        Optional<Row> row = findRowByID(rowId);
        return (row != null) ? row.get().getSeats() : new ArrayList<>();
    }

    // Section

    public Section createSection(Venue venue, int sectionCapacity, String sectionName) {
        if (venue == null) {
            throw new BusinessLogicException("Venue cannot be null.");
        }
        if (venue.getSections().stream().anyMatch(s -> s.getSectionName().equalsIgnoreCase(sectionName))) {
            throw new BusinessLogicException("Section with name '" + sectionName + "' already exists in the venue.");
        }
        Section section = new Section();// ID will be auto-assigned
        section.setSectionName(sectionName);
        section.setVenue(venue);
        section.setSectionCapacity(sectionCapacity);
        sectionRepository.create(section); // Persist the new section
        return section;
    }

    /**
     * Updates an existing Section.
     *
     * @param sectionId       the ID of the Section to update.
     * @param sectionName     the new name for the Section.
     * @param sectionCapacity the new capacity for the Section.
     * @return the updated Section object, or null if the Section does not exist.
     */
    public Object updateSection(int sectionId, String sectionName, int sectionCapacity) {
        Optional<Section> sectionOptional = sectionRepository.read(sectionId);
        if (sectionOptional.isEmpty()) {
            return null;
        }
        Section section = sectionOptional.get();
        section.setSectionName(sectionName);
        section.setSectionCapacity(sectionCapacity);
        return sectionRepository.update(section);
    }

    public void deleteSectionByVenue(int venueID) {
        List<Section> sections = sectionRepository.getAll().stream().
                filter(section -> section.getVenue().getID() == venueID)
                .toList();
        for (Section section : sections) {
            deleteRowsBySection(section.getID());
            sectionRepository.delete(section.getID());
        }
    }

    /**
     * Deletes a Section by its ID.
     *
     * @param sectionID the ID of the Section to delete.
     * @return true if the Section was successfully deleted, false otherwise.
     */
    public void deleteSection(int sectionID) {
        Optional<Section> section = sectionRepository.read(sectionID);
        if (section.isEmpty()) {
            throw new EntityNotFoundException("Section not found with ID: " + sectionID);
        }
        deleteRowsBySection(sectionID);
        sectionRepository.delete(sectionID);
    }

    /**
     * Retrieves a Section by its ID.
     *
     * @param sectionId the ID of the Section to retrieve.
     * @return the Section object if found, null otherwise.
     */
    public Optional<Section> findSectionByID(int sectionId) {
        return sectionRepository.read(sectionId);
    }

    /**
     * Retrieves all Sections from the repository.
     *
     * @return a list of all Sections.
     */
    public List<Section> getAllSections() {
        return sectionRepository.getAll();
    }

    public void addRowsToSection(int sectionId, int numberOfRows, int rowCapacity) {
        Optional<Section> sectionOptional = sectionRepository.read(sectionId);
        if (sectionOptional.isEmpty()) {
            throw new EntityNotFoundException("Section not found");
        }
        Section section = sectionOptional.get();
        for (int i = 0; i < numberOfRows; i++) {
            createRow(section, rowCapacity);
        }
        sectionRepository.update(section);
    }

    /**
     * Finds Sections by their name.
     *
     * @param name the name of the Sections to search for.
     * @return a list of Sections matching the given name.
     */
    public List<Section> findSectionsByName(String name) {
        return sectionRepository.getAll().stream()
                .filter(section -> section.getSectionName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all available Seats in a Section for a specific Event.
     *
     * @param sectionId the ID of the Section.
     * @param eventId   the ID of the Event.
     * @return a list of available Seats in the Section.
     */
    public List<Seat> getAvailableSeatsInSection(int sectionId, int eventId) {
        Optional<Section> section = sectionRepository.read(sectionId);
        if (section.isEmpty()) {
            return new ArrayList<>(); // Section not found
        }
        List<Seat> availableSeats = new ArrayList<>();
        for (Row row : section.get().getRows()) {
            availableSeats.addAll(
                    row.getSeats().stream()
                            .filter(seat -> !seat.isReserved() && seat.getTicket() != null
                                    && seat.getTicket().getEvent().getID() == eventId)
                            .toList()
            );
        }
        return availableSeats;
    }

    // Venue

    /**
     * Creates a new Venue and saves it to both repositories.
     */
    public Venue createVenue(String name, String location, int capacity, boolean hasSeats) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessLogicException("Venue name cannot be null or empty.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new BusinessLogicException("Venue location cannot be null or empty.");
        }
        if (capacity <= 0) {
            throw new ValidationException("Venue capacity must be greater than zero.");
        }
        Venue venue = new Venue();
        venue.setVenueName(name);
        venue.setLocation(location);
        venue.setVenueCapacity(capacity);
        venue.setHasSeats(hasSeats);
        venueRepository.create(venue);
        if (!hasSeats) {
            addSectionToVenue(venue.getID(), 1, capacity, "Default Section");
        }
        return venue;
    }

    /**
     * Retrieves a Venue by its ID.
     */
    public Optional<Venue> findVenueByID(int venueId) {
        return venueRepository.read(venueId);
    }

    /**
     * Finds Venues by location or name.
     */
    public List<Venue> findVenuesByLocationOrName(String keyword) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getVenueName().equalsIgnoreCase(keyword) || venue.getLocation().equalsIgnoreCase(keyword))
                .collect(Collectors.toList());
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
     * Retrieves all Venues from the in-memory repository.
     */
    public List<Venue> getAllVenues() {
        return venueRepository.getAll();
    }

    /**
     * Updates an existing Venue.
     */
    public Object updateVenue(int venueId, String name, String location, int capacity, boolean hasSeats) {
        Optional<Venue> venueOptional = venueRepository.read(venueId);
        if (venueOptional.isEmpty()) {
            return null;
        }
        Venue venue = venueOptional.get();
        venue.setVenueName(name);
        venue.setLocation(location);
        venue.setVenueCapacity(capacity);
        venue.setHasSeats(hasSeats);
        return venueRepository.update(venue);
    }

    /**
     * Deletes a Venue and its associated Sections.
     */
    public boolean deleteVenue(int venueId) {
        Optional<Venue> venue = venueRepository.read(venueId);
        if (venue.isEmpty()) {
            return false;
        }
        deleteSectionByVenue(venueId);
        venueRepository.delete(venueId);
        return true;
    }

    /**
     * Adds a Section to a Venue.
     */
    public void addSectionToVenue(int venueId, int numberOfSections, int sectionCapacity, String defaultSectionName) {
        Optional<Venue> venueOptional = venueRepository.read(venueId);
        if (venueOptional.isEmpty()) {
            throw new EntityNotFoundException("Venue not found");
        }
        Venue venue = venueOptional.get();

        for (int i = 0; i < numberOfSections; i++) {
            String sectionName = defaultSectionName + " " + (i + 1);
            if (venue.getSections().stream().anyMatch(s -> s.getSectionName().equals(sectionName))) {
                throw new ValidationException("Duplicate section name: " + sectionName);
            }
            Section section = createSection(venue, sectionCapacity, sectionName);
            sectionRepository.create(section);
        }
        loadSectionsForVenue(venue);
        venueRepository.update(venue);
    }

    /**
     * Retrieves all Sections associated with a specific Venue.
     */
    public List<Section> getSectionsByVenueID(int venueId) {
        return sectionRepository.getAll().stream()
                .filter(section -> section.getVenue().getID() == venueId)
                .collect(Collectors.toList());
    }

    public void loadSectionsForVenue(Venue venue) {
        List<Section> sections = getSectionsByVenueID(venue.getID());
        venue.setSections(sections); // Populate the sections list
    }

    /**
     * Retrieves all available Seats in a Venue for a specific Event.
     */
    public List<Seat> getAvailableSeatsInVenue(int venueId, int eventId) {
        Optional<Venue> venueOptional = findVenueByID(venueId);
        if (venueOptional.isEmpty() || !venueOptional.get().isHasSeats()) {
            return new ArrayList<>();
        }
        Venue venue = venueOptional.get();

        return venue.getSections().stream()
                .flatMap(section -> section.getRows().stream())
                .flatMap(row -> row.getSeats().stream())
                .filter(seat -> !seat.isReserved() && seat.getTicket() == null)
                .collect(Collectors.toList());
    }

}