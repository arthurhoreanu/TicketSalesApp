package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.io.*;
import java.util.*;

/**
 * Service class for managing section-related operations, including creating sections with seats,
 * retrieving available seats, and recommending seats based on customer preferences.
 */
public class SectionService {

    private final SeatService seatService;
    private final IRepository<Section> sectionRepository;
    private final IRepository<Seat> seatRepository;
    private final FileRepository<Section> sectionFileRepository;
    private final FileRepository<Seat> seatFileRepository;

    /**
     * Constructs a SectionService with dependencies for managing sections and seats.
     *
     * @param seatService        the service for managing seat-related operations
     * @param sectionRepository  the repository for storing and managing section data
     * @param seatRepository     the repository for storing and managing seat data
     */
    public SectionService(SeatService seatService, IRepository<Section> sectionRepository, IRepository<Seat> seatRepository) {
        this.seatService = seatService;
        this.sectionRepository = sectionRepository;
        this.seatRepository = seatRepository;
        this.sectionFileRepository = new FileRepository<>("src/repository/data/sections.csv", Section::fromCsv);
        List<Section> sectionsFromFile = sectionFileRepository.getAll();
        for (Section section : sectionsFromFile) {
            sectionRepository.create(section);
        }
        this.seatFileRepository = new FileRepository<>("src/repository/data/seats.csv", Seat::fromCsv);
        List<Seat> seatsFromFile = seatFileRepository.getAll();
        for (Seat seat : seatsFromFile) {
            seatRepository.create(seat);
        }
    }

    /**
     * Updates a venue CSV file by appending a section ID to the appropriate venue entry.
     *
     * @param venueId   the ID of the venue to update
     * @param sectionID the ID of the section to append
     */
    private void appendSectionToVenueCsv(int venueId, int sectionID) {
        File tempFile = new File("tempfile.csv");
        File originalFile = new File("src/repository/data/venues.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                int currentVenueId = Integer.parseInt(fields[0].trim());
                if (currentVenueId == venueId) {
                    if (fields.length < 5 || fields[4].trim().equals("null") || fields[4].trim().isEmpty()) {
                        fields[4] = String.valueOf(sectionID);
                    } else {
                        fields[4] += ";" + sectionID;
                    }
                    line = String.join(",", fields);
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating venue CSV: " + e.getMessage());
        }

        if (!originalFile.delete()) {
            System.err.println("Error deleting original file.");
        } else if (!tempFile.renameTo(originalFile)) {
            System.err.println("Error renaming temp file to original.");
        }
    }

    /**
     * Initializes rows with seats for a section and saves them to the repositories.
     *
     * @param section      the section for which rows are created
     * @param rowCount     the number of rows in the section
     * @param seatsPerRow  the number of seats per row
     * @param seatRepository the repository for saving seats
     * @return a list of rows represented as maps of seat numbers to Seat objects
     */
    private List<Map<Integer, Seat>> initializeRowsWithSeats(Section section, int rowCount, int seatsPerRow, IRepository<Seat> seatRepository) {
        List<Map<Integer, Seat>> rows = new ArrayList<>();
        for (int row = 1; row <= rowCount; row++) {
            Map<Integer, Seat> rowMap = new HashMap<>();
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                Seat seat = new Seat(row * 100 + seatNumber, row, section, seatNumber, null);

                // Save seat to repositories
                seatRepository.create(seat);
                seatFileRepository.create(seat);

                rowMap.put(seatNumber, seat);
            }
            rows.add(rowMap);
        }
        return rows;
    }

    /**
     * Creates a section with the specified parameters, including seats, and saves it to the repositories.
     *
     * @param sectionName    the name of the section
     * @param sectionId      the unique ID of the section
     * @param sectionCapacity the capacity of the section
     * @param rowCount       the number of rows in the section
     * @param seatsPerRow    the number of seats per row
     * @param venue          the venue to which the section belongs
     * @return the created Section object
     */
    public Section createSectionWithSeats(
            String sectionName,
            int sectionId,
            int sectionCapacity,
            int rowCount,
            int seatsPerRow,
            Venue venue) {

        if (sectionName == null || venue == null || rowCount <= 0 || seatsPerRow <= 0) {
            throw new IllegalArgumentException("Invalid input for section creation.");
        }

        // Create section
        Section section = new Section(sectionId, sectionName, sectionCapacity, venue, new ArrayList<>());

        // Initialize rows and save seats to repository
        List<Map<Integer, Seat>> rows = initializeRowsWithSeats(section, rowCount, seatsPerRow, seatRepository);
        section.setRows(rows);

        // Save section to repositories
        sectionRepository.create(section);
        sectionFileRepository.create(section);

        // Append section ID to the venue's CSV entry
        appendSectionToVenueCsv(venue.getID(), sectionId);

        return section;
    }

    /**
     * Updates an existing section in the repositories.
     *
     * @param section the section to update
     */
    public void updateSection(Section section) {
        sectionRepository.update(section);
        sectionFileRepository.update(section);
    }

    /**
     * Deletes a section by its ID from the repositories.
     *
     * @param sectionId the ID of the section to delete
     */
    public void deleteSection(Integer sectionId) {
        sectionRepository.delete(sectionId);
        sectionFileRepository.delete(sectionId);
    }

    /**
     * Retrieves all sections from the repository.
     *
     * @return a list of all sections
     */
    public List<Section> getAllSections() {
        return sectionRepository.getAll();
    }

    /**
     * Retrieves all available seats in a section for a specific event.
     *
     * @param section the section to check for available seats
     * @param event   the event for which to check availability
     * @return a list of available Seat objects
     */
    public List<Seat> getAvailableSeats(Section section, Event event) {
        List<Seat> availableSeats = new ArrayList<>();
        for (Map<Integer, Seat> row : section.getRows()) {
            for (Seat seat : row.values()) {
                if (!seatService.isSeatReservedForEvent(seat, event)) {
                    availableSeats.add(seat);
                }
            }
        }
        return availableSeats;
    }

    /**
     * Recommends a seat in a section for a customer based on preferences and availability for an event.
     *
     * @param customer the customer for whom the seat is recommended
     * @param section  the section to search for a seat
     * @param event    the event for which the seat is recommended
     * @return the recommended Seat object, or null if no suitable seat is found
     */
    public Seat recommendSeat(Customer customer, Section section, Event event) {
        List<Seat> availableSeats = getAvailableSeats(section, event);

        // Check if customer prefers this section
        Integer preferredCount = customer.getPreferredSections().get(section.getID());
        if (preferredCount != null && preferredCount > 0) {
            return availableSeats.isEmpty() ? null : availableSeats.get(0);
        }

        // Fallback: Recommend a front-row seat
        return seatService.recommendFrontRowSeat(availableSeats);
    }

    /**
     * Finds a seat in a section by its row and seat number.
     *
     * @param section    the section containing the seat
     * @param rowNumber  the row number of the seat
     * @param seatNumber the seat number within the row
     * @return the Seat object if found; otherwise, null
     */
    public Seat findSeatByRowAndNumber(Section section, int rowNumber, int seatNumber) {
        if (rowNumber > 0 && rowNumber <= section.getRows().size()) {
            Map<Integer, Seat> row = section.getRows().get(rowNumber - 1);
            return row != null ? row.get(seatNumber) : null;
        }
        return null;
    }

    /**
     * Finds a section by its ID.
     *
     * @param sectionID the ID of the section to find
     * @return the Section object if found; otherwise, null
     */
    public Section findSectionByID(int sectionID) {
        return sectionRepository.getAll().stream()
                .filter(section -> section.getID() == sectionID)
                .findFirst()
                .orElse(null);
    }
}
