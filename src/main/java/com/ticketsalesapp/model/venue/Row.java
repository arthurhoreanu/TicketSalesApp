package main.java.com.ticketsalesapp.model.venue;

import lombok.Getter;
import lombok.Setter;
import main.java.com.ticketsalesapp.model.Identifiable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a row within a section, containing details such as its ID, capacity, and associated section ID.
 */
@Entity
@Table(name = "row")
public class Row implements Identifiable {

    @Id
    @Column(name = "row_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rowID;

    @Getter
    @Setter
    @Column(name = "row_capacity", nullable = false)
    private int rowCapacity;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Getter
    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();


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
    public Integer getId() {
        return rowID;
    }

    @Override
    public void setId(int rowID) {
        this.rowID = rowID;
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
        if (!seats.contains(seat)) {
            seats.add(seat); // Add the Seat to the Row's list
            seat.setRow(this); // Maintain the bidirectional relationship
        }
    }

    /**
     * Removes a Seat from the Row and maintains bidirectional relationship.
     *
     * @param seat The Seat to remove.
     */
    public void removeSeat(Optional<Seat> seat) {
        seat.ifPresent(s -> {
            seats.remove(s);
            s.setRow(null); // Break bidirectional relationship
        });
    }

    @Override
    public String toString() {
        return "Row{" +
                "rowID=" + rowID +
                ", rowCapacity=" + rowCapacity +
                ", section=" + (section != null ? section.getId() : "null") +
                ", seatsCount=" + seats.size() +
                '}';
    }
}