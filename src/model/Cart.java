package model;

import controller.Controller;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart implements Identifiable {

    @Id
    @Column(name = "cart_id", nullable = false)
    private int cartID;

    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "is_payment_processed", nullable = false)
    private boolean isPaymentProcessed = false;

    @Transient
    private List<Ticket> tickets = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private double totalPrice = 0.0;

    static Controller controller = ControllerProvider.getController();

    public Cart() {}

    public Cart(Customer customer, Event event) {
        if (customer == null || event == null) {
            throw new IllegalArgumentException("Customer and Event cannot be null.");
        }
        this.customer = customer;
        this.event = event;
    }

    @Override
    public Integer getID() {
        return cartID;
    }

    @Override
    public void setID(int cartID){
        this.cartID = cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (this.customer != null) {
            this.customer.setCart(null);
        }
        this.customer = customer;
        if (customer != null) {
            customer.setCart(this);
        }
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isPaymentProcessed() {
        return isPaymentProcessed;
    }

    public void setPaymentProcessed(boolean paymentProcessed) {
        isPaymentProcessed = paymentProcessed;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Ticket> getTickets() {
        if (tickets.isEmpty() && cartID > 0) {
            tickets = controller.findTicketsByCartID(cartID);
        }
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets != null ? tickets : new ArrayList<>();
    }

    /**
     * Adds a ticket to the cart, ensuring it belongs to the same event.
     * Maintains bidirectional relationship.
     *
     * @param ticket The ticket to add.
     */
    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null.");
        }
        if (!ticket.getEvent().equals(this.event)) {
            throw new IllegalArgumentException("All tickets in the cart must belong to the same event.");
        }
        tickets.add(ticket);
        ticket.setCart(this); // Maintain bidirectional relationship.
    }

    /**
     * Removes a ticket from the cart and clears its cart reference.
     *
     * @param ticket The ticket to remove.
     */
    public void removeTicket(Ticket ticket) {
        if (ticket == null || !tickets.contains(ticket)) {
            throw new IllegalArgumentException("Ticket not found in the cart.");
        }
        tickets.remove(ticket);
        ticket.setCart(null); // Break bidirectional relationship.
    }

    /**
     * Clears all tickets from the cart.
     */
    public void clearCart() {
        for (Ticket ticket : tickets) {
            ticket.setCart(null); // Break bidirectional relationship.
        }
        tickets.clear();
    }

    /**
     * Calculates the total price of the cart based on its tickets.
     *
     * @return The total price of all tickets in the cart.
     */
    public double calculateTotalPrice() {
        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
    }



    public String toCsv() {
        return cartID + "," +
                (customer != null ? customer.getID() : "null") + "," +
                (event != null ? event.getID() : "null") + "," +
                isPaymentProcessed + "," +
                totalPrice;
    }

    public static Cart fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int cartID = Integer.parseInt(fields[0].trim());
        Customer customer = controller.findCustomerByID(Integer.parseInt(fields[1].trim()));
        Event event = controller.findEventByID(Integer.parseInt(fields[2].trim()));
        boolean isPaymentProcessed = Boolean.parseBoolean(fields[3].trim());
        double totalPrice = Double.parseDouble(fields[4].trim());

        Cart cart = new Cart(customer, event);
        cart.setCartID(cartID);
        cart.setPaymentProcessed(isPaymentProcessed);
        cart.setTotalPrice(totalPrice);
        return cart;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartID=" + cartID +
                ", customerID=" + (customer != null ? customer.getID() : "null") +
                ", eventID=" + (event != null ? event.getID() : "not loaded") +
                ", isPaymentProcessed=" + isPaymentProcessed +
                '}';
    }

}