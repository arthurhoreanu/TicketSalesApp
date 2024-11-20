package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SectionService {
    private final SeatService seatService;
    private final IRepository<Section> sectionRepository;
    private final FileRepository<Section> sectionFileRepository;

    /**
     * Constructs a SectionService with the specified SeatService dependency and repositories.
     *
     * @param seatService              the SeatService used for managing seat-related operations.
     * @param sectionRepository the in-memory repository for storing sections.
     */
    public SectionService(SeatService seatService, IRepository<Section> sectionRepository) {
        this.seatService = seatService;
        this.sectionRepository = sectionRepository;
        this.sectionFileRepository = new FileRepository<>("src/repository/data/sections.csv", Section::fromCsvFormat);
        List<Section> sectionsfromFile = sectionFileRepository.getAll();
        for (Section section : sectionsfromFile) {
            sectionRepository.create(section);
        }
    }

    private void appendSectionToVenueCsv(int venueId, int sectionID) {
        File tempFile = new File("tempfile.csv");
        File originalFile = new File("src/repository/data/venues.csv");

        boolean venueFound = false; // Flag to check if the venue ID is found

        try (
                BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                int currentVenueId = Integer.parseInt(fields[0].trim());

                if (currentVenueId == venueId) {
                    venueFound = true;

                    // Handle the sections field (assume it's the 5th field)
                    if (fields.length < 5 || fields[4].trim().equals("null") || fields[4].trim().isEmpty()) {
                        fields[4] = String.valueOf(sectionID); // Initialize with the first section
                    } else {
                        fields[4] += ";" + sectionID; // Append the new section name
                    }
                    line = String.join(",", fields);
                }

                writer.write(line);
                writer.newLine();
            }

            if (!venueFound) {
                System.out.println("Warning: Venue with ID " + venueId + " not found.");
            }

        } catch (IOException e) {
            System.err.println("Error updating venue CSV: " + e.getMessage());
            e.printStackTrace();
        }

        // Replace the original file with the updated file
        if (!originalFile.delete()) {
            System.err.println("Error deleting original file.");
        } else if (!tempFile.renameTo(originalFile)) {
            System.err.println("Error renaming temp file to original.");
        }
    }

    /**
     * Creates a new section with a specified number of rows and seats per row.
     * The section is saved to both in-memory and file repositories.
     *
     * @param sectionName    the name of the section.
     * @param sectionId      the unique ID for the section.
     * @param sectionCapacity the total capacity of the section.
     * @param rowCount       the number of rows in the section.
     * @param seatsPerRow    the number of seats per row.
     * @param venue          the venue in which the section is located.
     * @return the created Section object with seats initialized.
     */
    public Section createSectionWithSeats(
            String sectionName,
            int sectionId,
            int sectionCapacity,
            int rowCount,
            int seatsPerRow,
            Venue venue) {
        // Create seats for the section
        List<Seat> seats = new ArrayList<>();
        Section section = new Section(sectionId, sectionName, sectionCapacity, venue, seats);
        for (int row = 1; row <= rowCount; row++) {
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                Seat seat = new Seat(row * 100 + seatNumber, row, section, seatNumber, null);
                seats.add(seat);
            }
        }
        // Save to both in-memory and file repositories
        sectionRepository.create(section);
        sectionFileRepository.create(section);
        appendSectionToVenueCsv(venue.getID(), sectionId);

        return section;
    }



    /**
     * Updates a section in both repositories.
     *
     * @param section the updated section object.
     */
    public void updateSection(Section section) {
        sectionRepository.update(section);
        sectionFileRepository.update(section);
    }

    /**
     * Deletes a section by ID from both repositories.
     *
     * @param sectionId the ID of the section to delete.
     */
    public void deleteSection(Integer sectionId) {
        sectionRepository.delete(sectionId);
        sectionFileRepository.delete(sectionId);
    }

    /**
     * Retrieves all sections from the in-memory repository.
     *
     * @return a list of all sections.
     */
    public List<Section> getAllSections() {
        return sectionRepository.getAll();
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

}
