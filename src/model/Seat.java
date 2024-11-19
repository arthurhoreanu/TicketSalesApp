package model;

/**
 * Represents a seat in a venue section, with details such as row, seat number, and reservation status.
 */
public class Seat implements Identifiable {
    private int seatID;
    private Section section;
    private int rowNumber;
    private int seatNumber;
    private Event reservedForEvent;

    /**
     * Converts a CSV line into a Seat object.
     *
     * @param csvLine the CSV line containing the serialized Seat data. Expected format:
     *                {seatID, rowNumber, section, seatNumber, reservedForEvent}.
     *                - `seatID` is the unique identifier for the seat (integer).
     *                - `rowNumber` is the row where the seat is located (integer).
     *                - `section` is the serialized representation of the Section object.
     *                - `seatNumber` is the seat number within the row (integer).
     *                - `reservedForEvent` is the serialized representation of the Event object or "null" if not reserved.
     * @return the deserialized Seat object.
     * @throws NumberFormatException if numeric fields (e.g., seatID, rowNumber, seatNumber) cannot be parsed.
     * @throws IllegalArgumentException if the CSV format is invalid or the referenced section/event cannot be deserialized.
     */
    public static Seat fromCsvFormat(String csvLine) {
        String[] fields = csvLine.split(",");
        int seatID = Integer.parseInt(fields[0]);
        int rowNumber = Integer.parseInt(fields[1]);
        Section section = Section.fromCsvFormat(fields[2]);
        int seatNumber = Integer.parseInt(fields[3]);
        Event reservedForEvent = fields[4].equals("null") ? null : Event.fromCsvFormat(fields[4]);

        return new Seat(seatID, rowNumber, section, seatNumber, reservedForEvent);
    }

    /**
     * Converts the Seat object into a CSV format string.
     *
     * @return the CSV representation of the Seat object. The format is:
     *         {seatID, rowNumber, section, seatNumber, reservedForEvent}.
     *         - `seatID` is the unique identifier for the seat (integer).
     *         - `rowNumber` is the row where the seat is located (integer).
     *         - `section` is the serialized representation of the Section object.
     *         - `seatNumber` is the seat number within the row (integer).
     *         - `reservedForEvent` is the serialized representation of the Event object or "null" if not reserved.
     */
    @Override
    public String toCsvFormat() {
        return String.join(",",
                String.valueOf(seatID),
                String.valueOf(rowNumber),
                section.toCsvFormat(),
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
        this.rowNumber = rowNumber;
        this.section = section;
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
    public Section getSection() {
        return this.section;
    }

    /**
     * Sets the section where the seat is located.
     * @param section the section to set for the seat
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * Gets the row number of the seat.
     * @return the row number of the seat
     */
    public int getRowNumber() {
        return this.rowNumber;
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
                ", section=" + section +
                ", rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                ", reservedForEvent=" + reservedForEvent +
                '}';
    }
}
