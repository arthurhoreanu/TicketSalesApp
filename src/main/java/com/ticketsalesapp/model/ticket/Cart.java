package main.java.com.ticketsalesapp.model.ticket;

import lombok.Getter;
import lombok.Setter;
import main.java.com.ticketsalesapp.model.Identifiable;
import main.java.com.ticketsalesapp.model.event.Event;
import main.java.com.ticketsalesapp.model.user.Customer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart implements Identifiable {

    @Setter
    @Id
    @Column(name = "cart_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartID;

    @Getter
    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Customer customer;

    @Getter
    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "is_payment_processed", nullable = false)
    private boolean isPaymentProcessed = false;

    @Transient
    private List<Ticket> tickets = new ArrayList<>();

    @Getter
    @Setter
    @Column(name = "total_price", nullable = false)
    private double totalPrice = 0.0;

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

    public void setCustomer(Customer customer) {
        if (this.customer != null) {
            this.customer.setCart(null);
        }
        this.customer = customer;
        if (customer != null) {
            customer.setCart(this);
        }
    }

    public boolean isPaymentProcessed() {
        return isPaymentProcessed;
    }

    public void setPaymentProcessed(boolean paymentProcessed) {
        isPaymentProcessed = paymentProcessed;
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

}