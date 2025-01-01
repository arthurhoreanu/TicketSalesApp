package model;

import controller.Controller;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_history")
public class PurchaseHistory implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "purchaseHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "total_amount", nullable = true)
    private Double totalAmount;

    static Controller controller = ControllerProvider.getController();

    public PurchaseHistory() {}

    public PurchaseHistory(Customer customer, LocalDate purchaseDate) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (purchaseDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Purchase date cannot be in the future.");
        }
        this.customer = customer;
        this.purchaseDate = purchaseDate;
    }

    @Override
    public Integer getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
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
        if (tickets == null) {
            throw new IllegalArgumentException("Tickets list cannot be null.");
        }
        this.tickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket == null) {
                throw new IllegalArgumentException("Ticket in list cannot be null.");
            }
            addTicket(ticket);
        }
    }

    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null.");
        }
        tickets.add(ticket);
        ticket.setPurchaseHistory(this);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setPurchaseHistory(null);
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void calculateTotalAmount() {
        this.totalAmount = tickets.stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
    }

    public String toCsv() {
        return id + "," +
                (customer != null ? customer.getID() : "null") + "," +
                purchaseDate + "," +
                (totalAmount != null ? totalAmount : "null");
    }

    public static PurchaseHistory fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0].trim());
        Customer customer = controller.findCustomerByID(Integer.parseInt(fields[1].trim()));
        LocalDate purchaseDate = LocalDate.parse(fields[2].trim());
        Double totalAmount = fields[3].trim().equals("null") ? null : Double.parseDouble(fields[3].trim());

        PurchaseHistory purchaseHistory = new PurchaseHistory(customer, purchaseDate);
        purchaseHistory.setID(id);
        purchaseHistory.calculateTotalAmount();

        return purchaseHistory;
    }

    public String toString() {
        return "PurchaseHistory{" +
                "id=" + id +
                ", customerID=" + (customer != null ? customer.getID() : "null") +
                ", purchaseDate=" + purchaseDate +
                ", totalAmount=" + totalAmount +
                ", ticketsCount=" + tickets.size() +
                '}';
    }
}