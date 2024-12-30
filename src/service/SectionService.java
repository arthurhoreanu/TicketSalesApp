package service;

import controller.Controller;
import model.*;
import repository.IRepository;
import repository.factory.RepositoryFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing section-related operations, including creating sections with seats,
 * retrieving available seats, and recommending seats based on customer preferences.
 */
public class SectionService {

    private final IRepository<Section> sectionRepository;
    private final RowService rowService;
    static Controller controller = ControllerProvider.getController();

    public SectionService(RepositoryFactory repositoryFactory, RowService rowService) {
        this.sectionRepository = repositoryFactory.createSectionRepository();
        this.rowService = rowService;
    }

    public Section createSection(Venue venue, int sectionCapacity, String sectionName) {
        if (venue == null) {
            throw new IllegalArgumentException("Venue cannot be null");
        }
        Section section = new Section(0, sectionName, sectionCapacity, venue);
        sectionRepository.create(section);
        venue.addSection(section);
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
        sectionRepository.update(section);
        return section;
    }

    public void deleteSectionByVenue(int venueID) {
        List<Section> sections = sectionRepository.getAll().stream().
                filter(section -> section.getVenue().getID() == venueID)
                .toList();
        for (Section section : sections) {
            rowService.deleteRowsBySection(section.getID());
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
        Section section = sectionRepository.read(sectionID);
        if (section == null) {
            throw new IllegalArgumentException("Section not found");
        }
        rowService.deleteRowsBySection(sectionID);
        sectionRepository.delete(sectionID);
    }

    /**
     * Retrieves a Section by its ID.
     *
     * @param sectionId the ID of the Section to retrieve.
     * @return the Section object if found, null otherwise.
     */
    public Section findSectionByID(int sectionId) {
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

    public void addRowsToSection(int sectionID, int numberOfRows, int rowCapacity) {
        Section section = sectionRepository.read(sectionID);
        if (section == null) {
            throw new IllegalArgumentException("Section not found");
        }
        for (int i = 0; i < numberOfRows; i++) {
            rowService.createRow(section, rowCapacity);
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
                            .toList()
            );
        }
        return availableSeats;
    }

    public List<Row> findRowsBySection(int sectionId) {
        Section section = sectionRepository.read(sectionId);
        return (section != null) ? section.getRows() : new ArrayList<>();
    }

}