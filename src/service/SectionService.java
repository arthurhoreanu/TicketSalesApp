package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

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
}
