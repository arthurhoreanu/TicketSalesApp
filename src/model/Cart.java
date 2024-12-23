package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartID;

    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
                                                                        //todo modified this shit from lazy to eager
    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Payment payment;

    @Column(name = "is_payment_processed", nullable = false)
    private boolean isPaymentProcessed = false;

    @Transient
    private List<Ticket> tickets = new ArrayList<>();

    static Controller controller = new Controller();

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

    public List<Ticket> getTickets() {
        if (tickets.isEmpty()) {
            tickets = ControllerProvider.getController().findTicketsByCartID(cartID);
        }
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets != null ? tickets : new ArrayList<>();
    }


    public String toCsv() {
        return cartID + "," +
                (customer != null ? customer.getID() : "null") + "," +
                (event != null ? event.getID() : "null") + "," +
                isPaymentProcessed;
    }

    public static Cart fromCsv(String csvLine, Controller controller) {
        String[] fields = csvLine.split(",");
        int cartID = Integer.parseInt(fields[0].trim());
        Customer customer = controller.findCustomerByID(Integer.parseInt(fields[1].trim()));
        Event event = controller.findEventByID(Integer.parseInt(fields[2].trim()));
        boolean isPaymentProcessed = Boolean.parseBoolean(fields[3].trim());

        if (customer == null || event == null) {
            throw new IllegalArgumentException("Customer or Event not found for the given IDs.");
        }

        Cart cart = new Cart(customer, event);
        cart.setCartID(cartID);
        cart.setPaymentProcessed(isPaymentProcessed);
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
