package model;

import controller.Controller;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a shopping cart for storing tickets, with an ID, list of tickets, and total price.
 */
public class ShoppingCart implements Identifiable {
    private int shoppingCartID;
    private List<Ticket> items;
    private double totalPrice;

    static Controller controller = ControllerProvider.getController();

    public static ShoppingCart fromCsvFormat(String csvLine) {
        String[] fields = csvLine.split(",");
        int shoppingCartID = Integer.parseInt(fields[0]);
        List<Ticket> items = new ArrayList<>();

        if (!fields[1].equals("null")) {
            String[] ticketIds = fields[1].split(";");
            for (String ticketId : ticketIds) {
                Ticket ticket = controller.getTicketByID(Integer.parseInt(ticketId));
                if (ticket != null) {
                    items.add(ticket);
                }
            }
        }

        double totalPrice = Double.parseDouble(fields[2]);

        return new ShoppingCart(shoppingCartID, items, totalPrice);
    }

    @Override
    public String toCsvFormat() {
        String ticketIds = items.isEmpty() ? "null" : items.stream()
                .map(ticket -> String.valueOf(ticket.getID()))
                .reduce((t1, t2) -> t1 + ";" + t2)
                .orElse("null");

        return String.join(",",
                String.valueOf(shoppingCartID),
                ticketIds,
                String.valueOf(totalPrice)
        );
    }


    /**
     * Constructs a ShoppingCart with the specified ID, list of items, and total price.
     * @param shoppingCartID the unique ID of the shopping cart
     * @param items          the list of tickets in the shopping cart
     * @param totalPrice     the total price of the tickets in the cart
     */
    public ShoppingCart(int shoppingCartID, List<Ticket> items, double totalPrice) {
        this.shoppingCartID = shoppingCartID;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the unique ID of the shopping cart.
     * @return the ID of the shopping cart
     */
    @Override
    public Integer getID() {
        return shoppingCartID;
    }

    /**
     * Gets the list of tickets in the shopping cart.
     * @return the list of tickets in the cart
     */
    public List<Ticket> getItems() {
        return items;
    }

    /**
     * Gets the total price of all items in the shopping cart.
     * @return the total price of the cart
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the shopping cart.
     * @param totalPrice the total price to set for the cart
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Returns a string representation of the shopping cart, including its ID, items, and total price.
     * @return a string representing the shopping cart's details
     */
    @Override
    public String toString() {
        return "Shopping Cart [shoppingCartID=" + shoppingCartID + ", items=" + items + ", totalPrice=" + totalPrice + "]";
    }
}
