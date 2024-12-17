package controller;

import model.Customer;
import model.Event;
import model.Seat;
import model.Section;
import model.Venue;
import repository.IRepository;
import service.SectionService;

import java.util.List;

/**
 * The SectionController class provides methods to handle operations related to sections within a venue,
 * including seat availability, seat recommendations, section creation, and section information retrieval.
 */
public class SectionController {
    private final SectionService sectionService;
    private final IRepository<Section> sectionRepository;

    /**
     * Constructs a SectionController with the specified SectionService.
     *
     * @param sectionService the service handling section-related operations
     */
    public SectionController(SectionService sectionService, IRepository<Section> sectionRepository ) {
        this.sectionService = sectionService;
        this.sectionRepository = sectionRepository;
    }

    public void createSection(String sectionName, int sectionCapacity, Venue venue) {
        Section section = sectionService.createSection(sectionName, sectionCapacity, venue);
        System.out.println("Section '" + sectionName + "' created in venue '" + venue.getVenueName() + "' with a capacity of " + sectionCapacity + " seats.");
    }

    public void updateSection(int sectionID, String newSectionName, int newSectionCapacity) {
        boolean isUpdated = sectionService.updateSection(sectionID, newSectionName, newSectionCapacity);
        if (isUpdated) {
            System.out.println("Section with ID " + sectionID + " updated to name '" + newSectionName + "' with a capacity of " + newSectionCapacity + " seats.");
        } else {
            System.out.println("Section with ID " + sectionID + " not found. Update failed.");
        }
    }

    public void deleteSection(int sectionID) {
        boolean isDeleted = sectionService.deleteSection(sectionID);
        if (isDeleted) {
            System.out.println("Section with ID " + sectionID + " has been successfully deleted.");
        } else {
            System.out.println("Section with ID " + sectionID + " not found. Deletion failed.");
        }
    }

    public void getAllSections() {
        List<Section> sections = sectionService.getAllSections();
        if (sections.isEmpty()) {
            System.out.println("No sections available.");
        } else {
            System.out.println("All sections:");
            sections.forEach(section -> System.out.println("- ID: " + section.getID() + ", Name: " + section.getSectionName() + ", Capacity: " + section.getSectionCapacity()));
        }
    }

    public Section findSectionByID(int sectionID) {
        Section section = sectionService.findSectionByID(sectionID);
        if (section != null) {
            System.out.println("Section found: ID " + section.getID() + ", Name: " + section.getSectionName() + ", Capacity: " + section.getSectionCapacity());
        } else {
            System.out.println("Section with ID " + sectionID + " not found.");
        }
        return section;
    }

    public void getSectionInfo(String sectionName) {
        List<Section> sections = sectionService.findSectionsByName(sectionName);
        if (sections.isEmpty()) {
            System.out.println("No sections found with name: " + sectionName);
        } else {
            System.out.println("Matching sections with name '" + sectionName + "':");
            sections.forEach(section -> System.out.println("- ID: " + section.getID() + ", Capacity: " + section.getSectionCapacity()));
        }
    }
}
