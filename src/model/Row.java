package model;

import javax.persistence.*;
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

    @Column(name = "section_id", nullable = false)
    private int sectionId;

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

    /**
     * Default constructor for JPA and serialization.
     */
    public Row() {}

    /**
     * Constructs a Row object with a specified ID, capacity, and associated Section ID.
     *
     * @param rowID       The unique ID of the row.
     * @param rowCapacity The capacity of the row.
     * @param sectionId   The ID of the section associated with this row.
     */
    public Row(int rowID, int rowCapacity, int sectionId) {
        this.rowID = rowID;
        this.rowCapacity = rowCapacity;
        this.sectionId = sectionId;
    }

    /**
     * Constructs a Row object with no specified ID (used for persistence where ID is auto-generated).
     *
     * @param rowCapacity The capacity of the row.
     * @param sectionId   The ID of the section associated with this row.
     */
    public Row(int rowCapacity, int sectionId) {
        this.rowCapacity = rowCapacity;
        this.sectionId = sectionId;
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

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Row{" +
                "rowID=" + rowID +
                ", rowCapacity=" + rowCapacity +
                ", sectionId=" + sectionId +
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
                String.valueOf(sectionId)
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
        int sectionId = Integer.parseInt(fields[2].trim());
        return new Row(rowID, rowCapacity, sectionId);
    }
}