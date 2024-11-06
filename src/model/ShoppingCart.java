package model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Ticket> items = new ArrayList<>();
    private double totalPrice;

    public ShoppingCart(List<Ticket> items, double totalPrice) {
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public List<Ticket> getItems() {
        return items;
    }

    public void setItems(List<Ticket> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addTicket(Ticket ticket) {
        items.add(ticket);
        recalculateTotalPrice();
    }

    public void removeTicket(Ticket ticket) {
        items.remove(ticket);
        recalculateTotalPrice();
    }

    public void clear() {
        items.clear();
        totalPrice = 0;
    }

    public Order checkout(User user) {
        if (items.isEmpty()) {
            throw new IllegalStateException("Shopping cart is empty.");
        }

        Order order = new Order(user, new ArrayList<>(items));
        clear();
        return order;
    }

    private void recalculateTotalPrice() {
        totalPrice = items.stream().mapToDouble(Ticket::getPrice).sum();
    }
}


