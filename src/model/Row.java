package model;

import controller.Controller;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a row within a section, containing details such as its ID, capacity, and associated section ID.
 */
@Entity
@Table(name = "row")
public class Row implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rowID;

    @Column(name = "row_capacity", nullable = false)
    private int rowCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    static Controller controller = ControllerProvider.getController();

    // Default constructor for JPA
    public Row() {}

    /**
     * Constructs a Row object with a specified ID, capacity, and associated Section.
     *
     * @param rowID       The unique ID of the row.
     * @param rowCapacity The capacity of the row.
     * @param section     The Section associated with this row.
     */
    public Row(int rowID, int rowCapacity, Section section) {
        this.rowID = rowID;
        this.rowCapacity = rowCapacity;
        this.section = section;
    }

    @Override
    public Integer getID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public int getRowCapacity() {
        return rowCapacity;
    }

    public void setRowCapacity(int rowCapacity) {
        this.rowCapacity = rowCapacity;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats.clear(); // Clear existing seats to avoid duplicates
        for (Seat seat : seats) {
            addSeat(seat); // Use addSeat to maintain bidirectional relationship
        }
    }

    /**
     * Adds a Seat to the Row and maintains bidirectional relationship.
     *
     * @param seat The Seat to add.
     */
    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setRow(this); // Maintain bidirectional relationship
    }

    /**
     * Removes a Seat from the Row and maintains bidirectional relationship.
     *
     * @param seat The Seat to remove.
     */
    public void removeSeat(Seat seat) {
        seats.remove(seat);
        seat.setRow(null); // Break bidirectional relationship
    }

    @Override
    public String toString() {
        return "Row{" +
                "rowID=" + rowID +
                ", rowCapacity=" + rowCapacity +
                ", section=" + (section != null ? section.getID() : "null") +
                ", seatsCount=" + seats.size() +
                '}';
    }

    // CSV Methods

    /**
     * Converts the Row object into a CSV representation.
     *
     * @return A comma-separated string representing the row.
     */
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(rowID),
                String.valueOf(rowCapacity),
                String.valueOf(section != null ? section.getID() : "null")
        );
    }

    /**
     * Creates a Row object from a CSV string.
     *
     * @param csvLine The CSV string.
     * @return A Row object.
     */
    public static Row fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int rowID = Integer.parseInt(fields[0].trim());
        int rowCapacity = Integer.parseInt(fields[1].trim());
        int sectionID = Integer.parseInt(fields[2].trim());

        // Create the Row object with the Section retrieved from the controller
        return new Row(rowID, rowCapacity, controller.findSectionByID(sectionID));
    }
}
