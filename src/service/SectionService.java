package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;
import repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing section-related operations, including creating sections with seats,
 * retrieving available seats, and recommending seats based on customer preferences.
 */
public class SectionService {

    private final IRepository<Section> sectionRepository;
    private final IRepository<Seat> seatRepository;
    private final FileRepository<Section> sectionFileRepository;
    private final FileRepository<Seat> seatFileRepository;
    private final DBRepository<Section> sectionDatabaseRepository;
    private final DBRepository<Seat> seatDatabaseRepository;
    private final SeatService seatService;

    /**
     * Constructs a SectionService with dependencies for managing sections and seats.
     *
     * @param seatService        the service for managing seat-related operations
     * @param sectionRepository  the repository for managing section data
     * @param seatRepository     the repository for managing seat data
     */
    public SectionService(SeatService seatService, IRepository<Section> sectionRepository, IRepository<Seat> seatRepository) {
        this.seatService = seatService;
        this.sectionRepository = sectionRepository;
        this.seatRepository = seatRepository;

        // File repositories
        this.sectionFileRepository = new FileRepository<>("src/repository/data/sections.csv", Section::fromCsv);
        this.seatFileRepository = new FileRepository<>("src/repository/data/seats.csv", Seat::fromCsv);

        // Database repositories
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.sectionDatabaseRepository = new DBRepository<>(entityManagerFactory, Section.class);
        this.seatDatabaseRepository = new DBRepository<>(entityManagerFactory, Seat.class);

        // Sync data from file and database repositories to the primary repository
        syncFromCsv();
        syncFromDatabase();
    }

    /**
     * Synchronizes sections from the CSV file into the main repository.
     */
    private void syncFromCsv() {
        List<Section> sections = sectionFileRepository.getAll();
        for (Section section : sections) {
            sectionRepository.create(section);
        }

        List<Seat> seats = seatFileRepository.getAll();
        for (Seat seat : seats) {
            seatRepository.create(seat);
        }
    }

    /**
     * Synchronizes sections from the database into the main repository.
     */
    private void syncFromDatabase() {
        List<Section> sections = sectionDatabaseRepository.getAll();
        for (Section section : sections) {
            sectionRepository.create(section);
        }

        List<Seat> seats = seatDatabaseRepository.getAll();
        for (Seat seat : seats) {
            seatRepository.create(seat);
        }
    }

    /**
     * Creates a new section and saves it to all repositories.
     *
     * @param sectionName     the name of the section
     * @param sectionCapacity the capacity of the section
     * @param venue           the venue to which the section belongs
     * @return the created Section object
     */
    public Section createSection(String sectionName, int sectionCapacity, Venue venue) {
        int newID = sectionRepository.getAll().size() + 1;
        Section section = new Section(newID, sectionName, sectionCapacity, venue);

        // Save to repositories
        sectionRepository.create(section);
        sectionFileRepository.create(section);
        sectionDatabaseRepository.create(section);

        return section;
    }

    /**
     * Updates an existing section.
     *
     * @param sectionID        the ID of the section to update
     * @param newSectionName   the new name of the section
     * @param newSectionCapacity the new capacity of the section
     * @return true if the section was updated, false otherwise
     */
    public boolean updateSection(int sectionID, String newSectionName, int newSectionCapacity) {
        Section section = findSectionByID(sectionID);
        if (section != null) {
            section.setSectionName(newSectionName);
            section.setSectionCapacity(newSectionCapacity);

            // Update repositories
            sectionRepository.update(section);
            sectionFileRepository.update(section);
            sectionDatabaseRepository.update(section);
            return true;
        }
        return false;
    }

    /**
     * Deletes a section by its ID.
     *
     * @param sectionID the ID of the section to delete
     * @return true if the section was deleted, false otherwise
     */
    public boolean deleteSection(int sectionID) {
        Section section = findSectionByID(sectionID);
        if (section != null) {
            sectionRepository.delete(sectionID);
            sectionFileRepository.delete(sectionID);
            sectionDatabaseRepository.delete(sectionID);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all sections.
     *
     * @return a list of all sections
     */
    public List<Section> getAllSections() {
        return sectionRepository.getAll();
    }

    /**
     * Finds a section by its ID.
     *
     * @param sectionID the ID of the section to find
     * @return the Section object if found, null otherwise
     */
    public Section findSectionByID(int sectionID) {
        return sectionRepository.read(sectionID);
    }

    /**
     * Finds sections by name.
     *
     * @param sectionName the name of the section to search for
     * @return a list of matching sections
     */
    public List<Section> findSectionsByName(String sectionName) {
        return sectionRepository.getAll().stream()
                .filter(section -> section.getSectionName().equalsIgnoreCase(sectionName))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of available seats in a section for a specific event.
     *
     * @param section the section to check
     * @param event   the event for which to check availability
     * @return a list of available seats
     */
    public List<Seat> getAvailableSeats(Section section, Event event) {
        return section.getRows().stream()
                .flatMap(row -> row.getSeats().stream())
                .filter(seat -> !seatService.isSeatReservedForEvent(seat, event))
                .collect(Collectors.toList());
    }

    /**
     * Recommends a seat in a section for a customer based on preferences.
     *
     * @param customer the customer for whom the recommendation is made
     * @param section  the section to check
     * @param event    the event for which to recommend a seat
     * @return the recommended Seat, or null if no suitable seat is found
     */
    public Seat recommendSeat(Customer customer, Section section, Event event) {
        List<Seat> availableSeats = getAvailableSeats(section, event);

        // Recommend based on preferences
        return seatService.recommendSeatFromList(customer, availableSeats);
    }
}
