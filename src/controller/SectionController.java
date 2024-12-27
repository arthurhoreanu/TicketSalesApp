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
     */
    public void createSection(String sectionName, int sectionCapacity, Venue venue) {
        Section section = new Section(0, sectionName, sectionCapacity, venue);
        Section createdSection = sectionService.createSection(section);

        if (createdSection != null) {
            System.out.println("Section created successfully: " + createdSection);
        } else {
            System.out.println("Failed to create section.");
        }
    }

    /**
     * Updates an existing Section.
     */
    public void updateSection(int sectionId, String sectionName, int sectionCapacity) {
        Section updatedSection = sectionService.updateSection(sectionId, sectionName, sectionCapacity);

        if (updatedSection != null) {
            System.out.println("Section updated successfully: " + updatedSection);
        } else {
            System.out.println("Failed to update section. Section with ID " + sectionId + " not found.");
        }
    }

    /**
     * Deletes a Section by its ID.
     */
    public void deleteSection(int sectionId) {
        boolean deleted = sectionService.deleteSection(sectionId);

        if (deleted) {
            System.out.println("Section with ID " + sectionId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete section. Section with ID " + sectionId + " not found.");
        }
    }

    /**
     * Retrieves a Section by its ID.
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
     */
    public void getAllSections() {
        List<Section> sections = sectionService.getAllSections();

        if (!sections.isEmpty()) {
            System.out.println("All sections:");
            sections.forEach(System.out::println);
        } else {
            System.out.println("No sections available.");
        }
    }

    /**
     * Finds Sections by their name.
     */
    public void findSectionsByName(String name) {
        List<Section> sections = sectionService.findSectionsByName(name);

        if (!sections.isEmpty()) {
            System.out.println("Sections found:");
            sections.forEach(System.out::println);
        } else {
            System.out.println("No sections found with name: " + name);
        }
    }

    /**
     * Retrieves all available Seats in a Section for a specific Event.
     */
    public void getAvailableSeats(int sectionId, int eventId) {
        List<Seat> availableSeats = sectionService.getAvailableSeats(sectionId, eventId);

        if (!availableSeats.isEmpty()) {
            System.out.println("Available seats in Section ID " + sectionId + " for Event ID " + eventId + ":");
            availableSeats.forEach(System.out::println);
        } else {
            System.out.println("No available seats in Section ID " + sectionId + " for Event ID " + eventId + ".");
        }
    }
}