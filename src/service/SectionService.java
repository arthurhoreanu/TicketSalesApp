//TODO DONT KNOW THAT TO DO WITH THE FUCKING RECOMMENDsEAT METHOD!!!!!!!!!!!!!!!!

package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.io.*;
import java.util.*;

public class SectionService {
    private final SeatService seatService;
    private final IRepository<Section> sectionRepository;
    private final FileRepository<Section> sectionFileRepository;

    public SectionService(SeatService seatService, IRepository<Section> sectionRepository) {
        this.seatService = seatService;
        this.sectionRepository = sectionRepository;
        this.sectionFileRepository = new FileRepository<>("src/repository/data/sections.csv", Section::fromCsvFormat);
        List<Section> sectionsFromFile = sectionFileRepository.getAll();
        for (Section section : sectionsFromFile) {
            sectionRepository.create(section);
        }
    }

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
     * Helper method to initialize rows with seats for a section.
     *
     * @param section the section for which to create rows and seats.
     * @param rowCount the number of rows in the section.
     * @param seatsPerRow the number of seats per row.
     * @return a list of rows represented as maps of seat numbers to Seat objects.
     */
    private List<Map<Integer, Seat>> initializeRowsWithSeats(Section section, int rowCount, int seatsPerRow) {
        List<Map<Integer, Seat>> rows = new ArrayList<>();
        for (int row = 1; row <= rowCount; row++) {
            Map<Integer, Seat> rowMap = new HashMap<>();
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                Seat seat = new Seat(row * 100 + seatNumber, row, section, seatNumber, null);
                rowMap.put(seatNumber, seat);
            }
            rows.add(rowMap);
        }
        return rows;
    }

    public Section createSectionWithSeats(String sectionName, int sectionId, int sectionCapacity, int rowCount, int seatsPerRow, Venue venue) {

        if (sectionName == null || venue == null || rowCount <= 0 || seatsPerRow <= 0) {
            throw new IllegalArgumentException("Invalid input for section creation.");
        }
        Section section = new Section(sectionId, sectionName, sectionCapacity, venue, new ArrayList<>());
        List<Map<Integer, Seat>> rows = initializeRowsWithSeats(section, rowCount, seatsPerRow);
        section.setRows(rows);

        sectionRepository.create(section);
        sectionFileRepository.create(section);
        appendSectionToVenueCsv(venue.getID(), sectionId);

        return section;
    }

    public void updateSection(Section section) {
        sectionRepository.update(section);
        sectionFileRepository.update(section);
    }

    public void deleteSection(Integer sectionId) {
        sectionRepository.delete(sectionId);
        sectionFileRepository.delete(sectionId);
    }

    public List<Section> getAllSections() {
        return sectionRepository.getAll();
    }

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

    public Seat findSeatByRowAndNumber(Section section, int rowNumber, int seatNumber) {
        if (rowNumber > 0 && rowNumber <= section.getRows().size()) {
            Map<Integer, Seat> row = section.getRows().get(rowNumber - 1);
            return row != null ? row.get(seatNumber) : null;
        }
        return null;
    }
}
