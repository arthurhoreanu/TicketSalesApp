package model;

import controller.Controller;

import javax.persistence.*;

/**
 * Represents a seat in a venue section, with details such as its ID, parent row, reservation status,
 * and an optional association with a ticket if reserved.
 */
@Entity
@Table(name = "seat")
public class Seat implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatID;

    @Column(name = "seat_number", nullable = false)
    private int number; // The seat number within the row

    @Column(name = "is_reserved", nullable = false)
    private boolean isReserved = false; // Indicates if the seat is reserved (default is false)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "row_id", nullable = false)
    private Row row; // Many-to-One relationship with Row

    @OneToOne(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ticket ticket; // 1:1 relationship with Ticket

    static Controller controller = ControllerProvider.getController();

    /**
     * Default constructor for JPA, CSV, and InMemory compatibility.
     */
    public Seat() {}

    /**
     * Constructs a Seat with the specified attributes.
     *
     * @param seatID   the unique ID of the seat
     * @param number   the seat number
     * @param isReserved whether the seat is reserved
     * @param row      the Row object associated with the seat
     */
    public Seat(int seatID, int number, boolean isReserved, Row row) {
        this.seatID = seatID;
        this.number = number;
        this.isReserved = isReserved;
        this.row = row;
    }

    @Override
    public Integer getID() {
        return seatID;
    }

    @Override
    public void setID(int seatID) {
        this.seatID = seatID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        if (this.ticket != null) {
            this.ticket.setSeat(null); // Break the old association
        }
        this.ticket = ticket;
        if (ticket != null) {
            ticket.setSeat(this); // Establish the new association
        }
    }
    /**
     * Gets the Section to which this seat belongs via its Row.
     *
     * @return The Section object.
     */
    public Section getSection() {
        return row != null ? row.getSection() : null;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatID=" + seatID +
                ", number=" + number +
                ", isReserved=" + isReserved +
                ", rowID=" + (row != null && row.getID() != null ? row.getID() : "not loaded") +
                ", ticketID=" + (ticket != null ? ticket.getID() : "null") +
                '}';
    }

    // CSV Methods

    /**
     * Converts the Seat object into a CSV representation.
     *
     * @return A comma-separated string representing the seat.
     */
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(seatID),
                String.valueOf(number),
                String.valueOf(isReserved),
                row != null ? String.valueOf(row.getID()) : "null",
                ticket != null ? String.valueOf(ticket.getID()) : "null"
        );
    }

    /**
     * Creates a Seat object from a CSV string.
     *
     * @param csvLine The CSV string.
     * @return A Seat object.
     */
    public static Seat fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int seatID = Integer.parseInt(fields[0].trim());
        int number = Integer.parseInt(fields[1].trim());
        boolean isReserved = Boolean.parseBoolean(fields[2].trim());
        int rowID = Integer.parseInt(fields[3].trim());

        Row row = controller.findRowByID(rowID);
        return new Seat(seatID, number, isReserved, row);    }
}
