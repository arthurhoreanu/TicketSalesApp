package model;

import controller.Controller;

import javax.persistence.*;

/**
 * Represents a seat in a venue section, with details such as its ID, parent row, reservation status,
 * and an optional association with an event if reserved.
 */
@Entity
@Table(name = "seat")
public class Seat implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "row_id", nullable = false)
    private Row row;

    @Column(name = "is_reserved", nullable = false)
    private boolean isReserved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserved_event_id")
    private Event reservedForEvent;

    /**
     * Default constructor for JPA and serialization.
     */
    public Seat() {}

    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Seat with the specified attributes.
     *
     * @param seatID           the unique ID of the seat
     * @param row              the Row object where the seat is located
     * @param isReserved       whether the seat is reserved
     * @param reservedForEvent the Event object if the seat is reserved, null otherwise
     */
    public Seat(int seatID, Row row, boolean isReserved, Event reservedForEvent) {
        this.seatID = seatID;
        this.row = row;
        this.isReserved = isReserved;
        this.reservedForEvent = reservedForEvent;
    }

    @Override
    public Integer getID() {
        return seatID;
    }

    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public Section getSection() {
        return row.getSection(); // Retrieve the section via the row
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public Event getReservedForEvent() {
        return reservedForEvent;
    }

    public void setReservedForEvent(Event reservedForEvent) {
        this.reservedForEvent = reservedForEvent;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatID=" + seatID +
                ", rowID=" + (row != null ? row.getID() : "null") +
                ", isReserved=" + isReserved +
                ", reservedForEventID=" + (reservedForEvent != null ? reservedForEvent.getID() : "null") +
                '}';
    }

    // CSV Methods
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(seatID),
                row != null ? String.valueOf(row.getID()) : "null",
                String.valueOf(isReserved),
                reservedForEvent != null ? String.valueOf(reservedForEvent.getID()) : "null"
        );
    }

    public static Seat fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int seatID = Integer.parseInt(fields[0].trim());
        int rowID = Integer.parseInt(fields[1].trim());
        boolean isReserved = Boolean.parseBoolean(fields[2].trim());
        Integer reservedForEventID = fields[3].trim().equals("null") ? null : Integer.parseInt(fields[3].trim());
        Row row = controller.findRowByID(rowID);
        Event event = reservedForEventID != null ? ControllerProvider.getController().findEventByID(reservedForEventID) : null;
        return new Seat(seatID, row, isReserved, event);
    }
}
