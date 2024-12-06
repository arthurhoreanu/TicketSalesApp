package model;

import controller.Controller;

/**
 * Represents a seat in a venue section, with details such as row, seat number, and reservation status.
 */
public class Seat implements Identifiable {
    private int seatID;
    private Row row;
    private int seatNumber; //TODO should we use the satID as the seatNumber???? I'd say YES
    private Event reservedForEvent;
    //todo rethink about this attribute and method related to this bool
    private boolean isReserved;

    static Controller controller = ControllerProvider.getController();

    /**
     * Creates a Seat object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string.
     * @return the deserialized Seat object.
     */
    public static Seat fromCsvFormat(String csvLine) {
        String[] fields = csvLine.split(",");
        int seatID = Integer.parseInt(fields[0].trim());
        int rowNumber = Integer.parseInt(fields[1].trim());
        Section section = controller.findSectionByID(Integer.parseInt(fields[2].trim()));
        int seatNumber = Integer.parseInt(fields[3].trim());
        Event reservedForEvent = controller.findEventByID(Integer.parseInt(fields[4].trim()));
        return new Seat(seatID, rowNumber, section, seatNumber, reservedForEvent);
    }

    /**
     * Converts the Seat object into a CSV-formatted string.
     * @return the CSV-formatted string representing the Seat.
     */

    //TODO change CSV s to accommodate the Row class
    @Override
    public String toCsvFormat() {
        return String.join(",",
                String.valueOf(seatID),
                String.valueOf(row.getID()),
                String.valueOf(seatNumber),
                reservedForEvent == null ? "null" : reservedForEvent.toCsvFormat()
        );
    }

    /**
     * Constructs a Seat with the specified attributes.
     * @param seatID          the unique ID of the seat
     * @param rowNumber       the row number where the seat is located
     * @param section         the section where the seat is located
     * @param seatNumber      the seat number within the row
     * @param reservedForEvent the event for which the seat is reserved, initially null
     */
    public Seat(int seatID, int rowNumber, Section section, int seatNumber, Event reservedForEvent) {
        this.seatID = seatID;
        this.row = row;
        this.seatNumber = seatNumber;
        this.reservedForEvent = null; // initially, the seat is not reserved
    }

    /**
     * Gets the unique ID of the seat.
     * @return the ID of the seat
     */
    @Override
    public Integer getID() {
        return this.seatID;
    }

    /**
     * Gets the section where the seat is located.
     * @return the section of the seat
     */
    public Row getRow() {
        return this.row;
    }

    /**
     * Sets the section where the seat is located.
     * @param row the section to set for the seat
     */
    public void setRow(Row row) {
        this.row = row;
    }


    /**
     * Gets the seat number within the row.
     * @return the seat number of the seat
     */
    public int getSeatNumber() {
        return this.seatNumber;
    }

    /**
     * Gets the event for which the seat is reserved.
     * @return the event for which the seat is reserved, or null if not reserved
     */
    public Event getReservedForEvent() {
        return this.reservedForEvent;
    }

    /**
     * Sets the event for which the seat is reserved.
     * @param reservedForEvent the event for which the seat is being reserved
     */
    public void setReservedForEvent(Event reservedForEvent) {
        this.reservedForEvent = reservedForEvent;
    }

    /**
     * Returns a string representation of the seat, including its ID, section, row number, seat number, and reserved event.
     * @return a string representing the seat's details
     */
    @Override
    public String toString() {
        return "Seat{" +
                "seatID=" + seatID +
                ", row=" + row +
                ", seatNumber=" + seatNumber +
                ", reservedForEvent=" + reservedForEvent +
                '}';
    }
}
