package controller;

import model.*;
import service.SectionService;

import java.util.List;

/**
 * Controller for managing section-related operations.
 */
public class SectionController {
    private final SectionService sectionService;

    /**
     * Constructs a SectionController with the specified SectionService.
     *
     * @param sectionService The service for managing sections.
     */
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

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
            Section section = sectionService.createSection(venue, sectionCapacity, sectionName);
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
        Section updatedSection = sectionService.updateSection(sectionId, sectionName, sectionCapacity);
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
            sectionService.deleteSection(sectionId);
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
        sectionService.deleteSectionByVenue(venueId);
        System.out.println("All sections for Venue ID " + venueId + " deleted successfully.");
    }

    /**
     * Retrieves a Section by its ID.
     *
     * @param sectionId The ID of the section to retrieve.
     * @return The Section object if found, null otherwise.
     */
    public Section findSectionByID(int sectionId) {
        Section section = sectionService.findSectionByID(sectionId);
        if (section != null) {
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
        List<Section> sections = sectionService.getAllSections();
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
            sectionService.addRowsToSection(sectionId, numberOfRows, rowCapacity);
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
        List<Section> sections = sectionService.findSectionsByName(name);
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
        List<Seat> availableSeats = sectionService.getAvailableSeatsInSection(sectionId, eventId);
        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats in Section ID " + sectionId + " for Event ID " + eventId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats in Section ID " + sectionId + " for Event ID " + eventId + ".");
        }
        return availableSeats;
    }

    /**
     * Retrieves all Rows for a specific Section.
     *
     * @param sectionId The ID of the section.
     * @return A list of Rows in the Section.
     */
    public List<Row> findRowsBySection(int sectionId) {
        List<Row> rows = sectionService.findRowsBySection(sectionId);
        if (!rows.isEmpty()) {
            System.out.println("Rows in Section ID " + sectionId + ":");
            rows.forEach(System.out::println);
        } else {
            System.out.println("No rows found for Section ID " + sectionId + ".");
        }
        return rows;
    }
}
