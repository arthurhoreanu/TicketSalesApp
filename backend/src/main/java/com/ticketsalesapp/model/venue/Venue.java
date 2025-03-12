//package com.ticketsalesapp.model.venue;
//
//import lombok.Getter;
//import lombok.Setter;
//import com.ticketsalesapp.model.Identifiable;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Represents a venue for events, containing details such as its unique ID, name, location, and capacity.
// * Includes relationships with Sections (1:N).
// */
//@Getter
//@Entity
//@Table(name = "venue")
//public class Venue implements Identifiable {
//
//    @Setter
//    @Id
//    @Column(name = "venue_id", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int venueID;
//
//    @Setter
//    @Column(name = "venue_name", nullable = false)
//    private String venueName;
//
//    @Setter
//    @Column(name = "location", nullable = false)
//    private String location;
//
//    @Setter
//    @Column(name = "venue_capacity", nullable = false)
//    private int venueCapacity;
//
//    @Setter
//    @Column(name = "has_seats", nullable = false)
//    private boolean hasSeats;
//
//    @Transient
//    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Section> sections = new ArrayList<>();
//
//    // Default constructor for JPA
//    public Venue() {}
//
//    /**
//     * Constructs a Venue with the specified attributes.
//     *
//     * @param venueID       the unique ID of the venue
//     * @param venueName     the name of the venue
//     * @param location      the location of the venue
//     * @param venueCapacity the total capacity of the venue
//     * @param hasSeats      true if the venue has seats, false otherwise
//     */
//    public Venue(int venueID, String venueName, String location, int venueCapacity, boolean hasSeats) {
//        this.venueID = venueID;
//        this.venueName = venueName;
//        this.location = location;
//        this.venueCapacity = venueCapacity;
//        this.hasSeats = hasSeats;
//    }
//
//    @Override
//    public Integer getId() {
//        return venueID;
//    }
//
//    @Override
//    public void setId(int venueID) {
//        this.venueID = venueID;
//    }
//
//    public void setSections(List<Section> sections) {
//        this.sections.clear(); // Clear existing list to avoid duplicates
//        for (Section section : sections) {
//            addSection(section); // Use addSection to maintain bidirectional relationship
//        }
//    }
//
//    /**
//     * Adds a Section to the Venue and maintains bidirectional relationship.
//     *
//     * @param section The section to add.
//     */
//    public void addSection(Section section) {
//        sections.add(section);
//        section.setVenue(this); // Maintain bidirectional relationship
//    }
//
//    /**
//     * Removes a Section from the Venue and maintains bidirectional relationship.
//     *
//     * @param section The section to remove.
//     */
//    public void removeSection(Section section) {
//        sections.remove(section);
//        section.setVenue(null); // Break bidirectional relationship
//    }
//
//    @Override
//    public String toString() {
//        return "Venue{" +
//                "venueID=" + venueID +
//                ", venueName='" + venueName + '\'' +
//                ", location='" + location + '\'' +
//                ", venueCapacity=" + venueCapacity +
//                ", hasSeats=" + hasSeats +
//                ", sectionsCount=" + sections.size() +
//                '}';
//    }
//
//}
