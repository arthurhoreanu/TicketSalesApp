package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shopping cart for a customer during an active session.
 * Stores selected tickets until the payment is processed.
 */
@Entity
@Table(name = "cart")
public class Cart implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartID;

    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "is_payment_processed", nullable = false)
    private boolean isPaymentProcessed = false;

    /**
     * Default constructor for JPA and serialization.
     */
    public Cart() {
        this.tickets = new ArrayList<>();
    }

    /**
     * Constructs a Cart associated with a specific customer and event.
     *
     * @param customer The customer owning this cart.
     * @param event    The event associated with the tickets in this cart.
     */
    public Cart(Customer customer, Event event) {
        this.customer = customer;
        this.event = event;
    }

    @Override
    public Integer getID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
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

    /**
     * Adds a ticket to the cart, validating that all tickets belong to the same event.
     *
     * @param ticket The ticket to add.
     * @throws IllegalArgumentException if the ticket belongs to a different event.
     */
    public void addTicket(Ticket ticket) {
        if (!ticket.getEvent().equals(this.event)) {
            throw new IllegalArgumentException("All tickets in the cart must belong to the same event.");
        }
        tickets.add(ticket);
        ticket.setCart(this);
    }

    /**
     * Removes a ticket from the cart.
     *
     * @param ticket The ticket to remove.
     */
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setCart(null);
    }

    /**
     * Clears all tickets from the cart.
     */
    public void clearCart() {
        for (Ticket ticket : tickets) {
            ticket.setCart(null);
        }
        tickets.clear();
    }

    /**
     * Processes the payment for the tickets in the cart and marks them as sold.
     *
     * @param paymentDetails The payment details to process.
     * @return PurchaseHistory containing the tickets from the processed cart.
     * @throws IllegalStateException if the payment has already been processed.
     */
    // TODO vezi cum se leagă de PurchaseHistory și Payment
    public PurchaseHistory processPayment(Payment paymentDetails) {
        if (isPaymentProcessed) {
            throw new IllegalStateException("Payment has already been processed for this cart.");
        }
        // Payment processing logic here (e.g., PaymentProcessor).

        for (Ticket ticket : tickets) {
            ticket.markAsSold();
        }
        isPaymentProcessed = true;

        // Create a PurchaseHistory record
        PurchaseHistory purchaseHistory = new PurchaseHistory(this.customer, new ArrayList<>(tickets));
        clearCart(); // Reset the cart after payment
        return purchaseHistory;
    }

    /**
     * Converts the cart to a CSV representation.
     *
     * @return A CSV string representing the cart.
     */
    public String toCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append(cartID).append(",").append(isPaymentProcessed).append(",")
                .append(event != null ? event.getID() : "null").append("\n");
        for (Ticket ticket : tickets) {
            csv.append(ticket.toCsv()).append("\n");
        }
        return csv.toString();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartID=" + cartID +
                ", customerID=" + (customer != null ? customer.getID() : "null") +
                ", eventID=" + (event != null ? event.getID() : "null") +
                ", isPaymentProcessed=" + isPaymentProcessed +
                ", tickets=" + tickets +
                '}';
    }
}