package model;

public class OrderTicket {
    private int orderID; // ID of the associated order
    private int ticketID; // ID of the associated ticket

    /**
     * Constructs an OrderTicket with specified attributes.
     *
     * @param orderID  the ID of the associated order
     * @param ticketID the ID of the associated ticket
     */
    public OrderTicket(int orderID, int ticketID) {
        this.orderID = orderID;
        this.ticketID = ticketID;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getTicketID() {
        return ticketID;
    }

    @Override
    public String toString() {
        return "OrderTicket{" +
                "orderID=" + orderID +
                ", ticketID=" + ticketID +
                '}';
    }

    /**
     * Converts the OrderTicket to a CSV-formatted string.
     *
     * @return the CSV string
     */
    public String toCsv() {
        return orderID + "," + ticketID;
    }

    /**
     * Creates an OrderTicket object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string
     * @return the deserialized OrderTicket object
     */
    public static OrderTicket fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int orderID = Integer.parseInt(fields[0].trim());
        int ticketID = Integer.parseInt(fields[1].trim());
        return new OrderTicket(orderID, ticketID);
    }
}
