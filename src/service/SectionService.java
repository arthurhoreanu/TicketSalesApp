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

        // Sync data from file and database repositories to the primary repository
        syncFromCsv();
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
     * Creates a new section and saves it to all repositories.
     *
     * @param sectionName     the name of the section
     * @param sectionCapacity the capacity of the section
     * @param venue           the venue to which the section belongs
     * @return the created Section object
     */
    /**
     * Creates a new section and saves it to all repositories.
     *
     * @param section the Section object to be created.
     * @return the created Section object.
     */
    public Section createSection(Section section) {
        // Save to repositories
        sectionRepository.create(section);
        sectionFileRepository.create(section);

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
    public Section updateSection(int sectionId, String sectionName, int sectionCapacity) {
        Section section = sectionRepository.read(sectionId);
        if (section == null) {
            return null; // Section not found
        }

        section.setSectionName(sectionName);
        section.setSectionCapacity(sectionCapacity);

        sectionRepository.update(section);             // Update in-memory repository
        sectionFileRepository.update(section);         // Update CSV
        return section;
    }



    /**
     * Deletes a Section by its ID.
     *
     * @param sectionId the ID of the Section to delete.
     * @return true if the Section was successfully deleted, false otherwise.
     */
    public boolean deleteSection(int sectionId) {
        Section section = sectionRepository.read(sectionId);
        if (section == null) {
            return false; // Section not found
        }

        // Delete all seats associated with the Section
        for (Row row : section.getRows()) {
            for (Seat seat : row.getSeats()) {
                seatRepository.delete(seat.getID());
                seatFileRepository.delete(seat.getID());
            }
        }

        sectionRepository.delete(sectionId);           // Remove from in-memory repository
        sectionFileRepository.delete(sectionId);       // Remove from CSV
        return true;
    }



    /**
     * Retrieves a Section by its ID.
     *
     * @param sectionId the ID of the Section to retrieve.
     * @return the Section object if found, null otherwise.
     */
    public Section getSectionById(int sectionId) {
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
    public List<Seat> getAvailableSeats(int sectionId, int eventId) {
        Section section = sectionRepository.read(sectionId);
        if (section == null) {
            return new ArrayList<>(); // Section not found
        }

        List<Seat> availableSeats = new ArrayList<>();
        for (Row row : section.getRows()) {
            availableSeats.addAll(
                    row.getSeats().stream()
                            .filter(seat -> !seat.isReserved() && seat.getTicket() != null
                                    && seat.getTicket().getEvent().getID() == eventId)
                            .collect(Collectors.toList())
            );
        }

        return availableSeats;
    }
}


