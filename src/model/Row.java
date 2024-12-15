package model;

import controller.Controller;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a row within a section, containing details such as its ID, capacity, and associated section.
 * A row can dynamically fetch its associated seats and parent section using provided methods.
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
    private List<Seat> seats;

    /**
     * Default constructor for JPA and serialization.
     */
    public Row() {}

    static Controller controller = ControllerProvider.getController();


    /**
     * Constructs a Row object with a specified ID, capacity, and parent Section.
     *
     * @param rowID       The unique ID of the row.
     * @param rowCapacity The capacity of the row.
     * @param section     The Section object associated with this row.
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
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Row{" +
                "rowID=" + rowID +
                ", rowCapacity=" + rowCapacity +
                ", sectionID=" + (section != null ? section.getID() : "null") +
                '}';
    }

    // CSV Methods
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(rowID),
                String.valueOf(rowCapacity),
                section != null ? String.valueOf(section.getID()) : "null"
        );
    }

    public static Row fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int rowID = Integer.parseInt(fields[0].trim());
        int rowCapacity = Integer.parseInt(fields[1].trim());
        int sectionID = Integer.parseInt(fields[2].trim());
        Section section = ControllerProvider.getController().findSectionByID(sectionID);
        return new Row(rowID, rowCapacity, section);
    }
}
