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
// * Represents a section within a venue, containing details such as name, capacity,
// * and its associated venue. Each section can dynamically retrieve its rows
// * and venue using IDs and a controller.
// */
//@Entity
//@Table(name = "section")
//public class Section implements Identifiable {
//
//    private static int idCounter = 1; // Static counter for unique IDs
//
//    @Setter
//    @Id
//    @Column(name = "section_id", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int sectionID;
//
//    @Getter
//    @Setter
//    @Column(name = "section_name", nullable = false)
//    private String sectionName;
//
//    @Getter
//    @Setter
//    @Column(name = "section_capacity", nullable = false)
//    private int sectionCapacity;
//
//    @Getter
//    @Setter
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "venue_id", nullable = false)
//    private Venue venue;
//
//    @Getter
//    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Row> rows = new ArrayList<>();
//
//    // Default constructor for JPA
//    public Section() {}
//
//    /**
//     * Constructs a Section with the specified attributes.
//     *
//     * @param sectionID       the unique ID of the section
//     * @param sectionName     the name of the section
//     * @param sectionCapacity the seating capacity of the section
//     * @param venue           the Venue object associated with this section
//     */
//    public Section(int sectionID, String sectionName, int sectionCapacity, Venue venue) {
//        this.sectionID = (sectionID > 0) ? sectionID : idCounter++;
//        this.sectionName = sectionName;
//        this.sectionCapacity = sectionCapacity;
//        this.venue = venue;
//    }
//
//    @Override
//    public Integer getId() {
//        return sectionID;
//    }
//
//    @Override
//    public void setId(int sectionID) {
//        this.sectionID = sectionID;
//    }
//
//    public void setRows(List<Row> rows) {
//        this.rows.clear(); // Clear existing rows to avoid duplicates
//        for (Row row : rows) {
//            addRow(row); // Use addRow to maintain bidirectional relationship
//        }
//    }
//
//    /**
//     * Adds a Row to the Section and maintains bidirectional relationship.
//     *
//     * @param row The Row to add.
//     */
//    public void addRow(Row row) {
//        rows.add(row);
//        row.setSection(this); // Maintain bidirectional relationship
//    }
//
//    /**
//     * Removes a Row from the Section and maintains bidirectional relationship.
//     *
//     * @param row The Row to remove.
//     */
//    public void removeRow(Row row) {
//        rows.remove(row);
//        row.setSection(null); // Break bidirectional relationship
//    }
//
//    @Override
//    public String toString() {
//        return "Section{" +
//                "sectionID=" + sectionID +
//                ", sectionName='" + sectionName + '\'' +
//                ", sectionCapacity=" + sectionCapacity +
//                ", venue=" + (venue != null ? venue.getId() : "null") +
//                ", rowsCount=" + rows.size() +
//                '}';
//    }
//}
