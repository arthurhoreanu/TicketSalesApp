package model;

import controller.Controller;

/**
 * Represents a seat in a venue section, with details such as row, seat number, and reservation status.
 */
public class Seat implements Identifiable {
    private static int seatCounter = 1;
    private int seatID;
    private Row row;
    private Event reservedForEvent;
    //todo rethink about this attribute and method related to this bool
    private boolean isReserved;

    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a Seat with the specified attributes.
     * @param seatID          the unique ID of the seat
     * @param rowNumber       the row number where the seat is located
     * @param reservedForEvent the event for which the seat is reserved, initially null
     */
    public Seat(int seatID, int rowNumber, Row row, Event reservedForEvent) {
        this.seatID = seatCounter++;
        this.row = row;
        this.reservedForEvent = null; // initially, the seat is not reserved
    }

    /**
     * Creates a Seat object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string.
     * @return the deserialized Seat object.
     */
    public static Seat fromCsv(String csvLine) {
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
    public String toCsv() {
        return String.join(",",
                String.valueOf(seatID),
                String.valueOf(row.getID()),
                String.valueOf(seatNumber),
                reservedForEvent == null ? "null" : reservedForEvent.toCsv()
        );
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
                ", reservedForEvent=" + reservedForEvent +
                '}';
    }
}
