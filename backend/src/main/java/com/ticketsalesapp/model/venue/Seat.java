//package com.ticketsalesapp.model.venue;
//
//import lombok.Getter;
//import lombok.Setter;
//import com.ticketsalesapp.model.Identifiable;
//import com.ticketsalesapp.model.ticket.Ticket;
//
//import javax.persistence.*;
//
///**
// * Represents a seat in a venue section, with details such as its ID, parent row, reservation status,
// * and an optional association with a ticket if reserved.
// */
//@Entity
//@Table(name = "seat")
//public class Seat implements Identifiable {
//
//    @Id
//    @Column(name = "seat_id", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int seatID;
//
//    @Getter
//    @Setter
//    @Column(name = "seat_number", nullable = false)
//    private int number; // The seat number within the row
//
//    @Column(name = "is_reserved", nullable = false)
//    @Getter
//    @Setter
//    private boolean isReserved = false; // Indicates if the seat is reserved (default is false)
//
//    @Setter
//    @Getter
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "row_id", nullable = false)
//    private Row row; // Many-to-One relationship with Row
//
//    @Getter
//    @OneToOne(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Ticket ticket; // 1:1 relationship with Ticket
//
//    /**
//     * Default constructor for JPA, CSV, and InMemory compatibility.
//     */
//    public Seat() {}
//
//    /**
//     * Constructs a Seat with the specified attributes.
//     *
//     * @param seatID   the unique ID of the seat
//     * @param number   the seat number
//     * @param isReserved whether the seat is reserved
//     * @param row      the Row object associated with the seat
//     */
//    public Seat(int seatID, int number, boolean isReserved, Row row) {
//        this.seatID = seatID;
//        this.number = number;
//        this.isReserved = isReserved;
//        this.row = row;
//    }
//
//    @Override
//    public Integer getId() {
//        return seatID;
//    }
//
//    @Override
//    public void setId(int seatID) {
//        this.seatID = seatID;
//    }
//
//    public void setTicket(Ticket ticket) {
//        if (this.ticket != null) {
//            this.ticket.setSeat(null); // Break the old association
//        }
//        this.ticket = ticket;
//        if (ticket != null) {
//            ticket.setSeat(this); // Establish the new association
//        }
//    }
//
//    /**
//     * Gets the Section to which this seat belongs via its Row.
//     *
//     * @return The Section object.
//     */
//    public Section getSection() {
//        return row != null ? row.getSection() : null;
//    }
//
//    @Override
//    public String toString() {
//        return "Seat{" +
//                "seatID=" + seatID +
//                ", number=" + number +
//                ", isReserved=" + isReserved +
//                ", rowID=" + (row != null && row.getId() != null ? row.getId() : "not loaded") +
//                ", ticketID=" + (ticket != null ? ticket.getId() : "null") +
//                '}';
//    }
//}
