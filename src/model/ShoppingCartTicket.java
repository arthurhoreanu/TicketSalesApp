package model;

import controller.Controller;

import javax.persistence.*;

/**
 * Represents a connection between a ShoppingCart and its associated tickets.
 */
@Entity
@Table(name = "shopping_cart_ticket")
public class ShoppingCartTicket implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shoppingCartTicketID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    /**
     * Default constructor for JPA and serialization.
     */
    public ShoppingCartTicket() {}

    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs a ShoppingCartTicket with the specified attributes.
     *
     * @param shoppingCart the ShoppingCart associated with this ticket
     * @param event        the Event associated with this ticket
     * @param ticket       the Ticket being added to the shopping cart
     */
    public ShoppingCartTicket(int shoppingCartTicketID,ShoppingCart shoppingCart, Event event, Ticket ticket) {
        this.shoppingCartTicketID = shoppingCartTicketID;
        this.shoppingCart = shoppingCart;
        this.event = event;
        this.ticket = ticket;
    }

    /**
     * Constructs a ShoppingCartTicket without specifying an ID (used when the ID is auto-generated).
     *
     * @param shoppingCart the ShoppingCart associated with this ticket
     * @param event        the Event associated with this ticket
     * @param ticket       the Ticket being added to the shopping cart
     */
    public ShoppingCartTicket(ShoppingCart shoppingCart, Event event, Ticket ticket) {
        this.shoppingCart = shoppingCart;
        this.event = event;
        this.ticket = ticket;
    }


    @Override
    public Integer getID() {
        return shoppingCartTicketID;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "ShoppingCartTicket{" +
                "shoppingCartTicketID=" + shoppingCartTicketID +
                ", shoppingCartID=" + (shoppingCart != null ? shoppingCart.getID() : "null") +
                ", ticketID=" + (ticket != null ? ticket.getID() : "null") +
                ", eventID=" + (event != null ? event.getID() : "null") +
                '}';
    }

    // CSV Methods
    public String toCsv() {
        return String.join(",",
                String.valueOf(shoppingCartTicketID),
                String.valueOf(shoppingCart != null ? shoppingCart.getID() : "null"),
                String.valueOf(event != null ? event.getID() : "null"),
                String.valueOf(ticket != null ? ticket.getID() : "null")
        );
    }

    public static ShoppingCartTicket fromCSV(String csvLine) {
        String[] fields = csvLine.split(",");
        int shoppingCartTicketID = Integer.parseInt(fields[0].trim());
        int shoppingCartID = Integer.parseInt(fields[1].trim());
        int eventID = Integer.parseInt(fields[2].trim());
        int ticketID = Integer.parseInt(fields[3].trim());

        ShoppingCart shoppingCart = controller.findShoppingCartByID(shoppingCartID);
        Event event = controller.findEventByID(eventID);
        Ticket ticket = controller.findTicketByID(ticketID);

        return new ShoppingCartTicket(shoppingCartTicketID, shoppingCart, event, ticket);
    }
}
