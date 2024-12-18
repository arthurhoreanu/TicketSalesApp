package com.ticketsalescompany.model;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a shopping cart that contains multiple tickets, associated with a specific user.
 */
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shoppingCartID;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCartTicket> tickets;

    /**
     * Default constructor for JPA and serialization.
     */
    public ShoppingCart() {}

    /**
     * Constructs a ShoppingCart with the specified attributes.
     *
     * @param shoppingCartID the unique ID of the shopping cart
     * @param totalPrice     the total price of tickets in the cart
     */
    public ShoppingCart(int shoppingCartID, double totalPrice) {
        this.shoppingCartID = shoppingCartID;
        this.totalPrice = totalPrice;
    }

    @Override
    public Integer getID() {
        return shoppingCartID;
    }

    public void setShoppingCartID(int shoppingCartID) {
        this.shoppingCartID = shoppingCartID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ShoppingCartTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<ShoppingCartTicket> tickets) {
        this.tickets = tickets;
    }

    public List<ShoppingCartTicket> getItems() {
        return tickets;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "shoppingCartID=" + shoppingCartID +
                ", totalPrice=" + totalPrice +
                ", tickets=" + tickets +
                '}';
    }
    @Override
    public String toCsv() {
        return shoppingCartID + "," + totalPrice;
    }

    public static ShoppingCart fromCSV(String csvLine) {
        String[] fields = csvLine.split(",");
        int shoppingCartID = Integer.parseInt(fields[0].trim());
        double totalPrice = Double.parseDouble(fields[1].trim());
        return new ShoppingCart(shoppingCartID, totalPrice);
    }

}
