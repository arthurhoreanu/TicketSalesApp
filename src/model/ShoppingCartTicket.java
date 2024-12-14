package model;

public class ShoppingCartTicket {
    private int shoppingCartID; // ID of the shopping cart
    private int eventID; // ID of the event
    private int ticketID; // ID of the ticket

    /**
     * Constructs a ShoppingCartTicket with the specified attributes.
     *
     * @param shoppingCartID the ID of the associated shopping cart
     * @param eventID        the ID of the associated event
     * @param ticketID       the ID of the associated ticket
     */
    public ShoppingCartTicket(int shoppingCartID, int eventID, int ticketID) {
        this.shoppingCartID = shoppingCartID;
        this.eventID = eventID;
        this.ticketID = ticketID;
    }

    public int getShoppingCartID() {
        return shoppingCartID;
    }

    public int getEventID() {
        return eventID;
    }

    public int getTicketID() {
        return ticketID;
    }

    @Override
    public String toString() {
        return "ShoppingCartTicket{" +
                "shoppingCartID=" + shoppingCartID +
                ", eventID=" + eventID +
                ", ticketID=" + ticketID +
                '}';
    }

    /**
     * Converts the ShoppingCartTicket to a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the ShoppingCartTicket
     */
    public String toCSV() {
        return shoppingCartID + "," + eventID + "," + ticketID;
    }

    /**
     * Creates a ShoppingCartTicket object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string
     * @return the created ShoppingCartTicket object
     */
    public static ShoppingCartTicket fromCSV(String csvLine) {
        String[] fields = csvLine.split(",");
        int shoppingCartID = Integer.parseInt(fields[0].trim());
        int eventID = Integer.parseInt(fields[1].trim());
        int ticketID = Integer.parseInt(fields[2].trim());
        return new ShoppingCartTicket(shoppingCartID, eventID, ticketID);
    }
}
