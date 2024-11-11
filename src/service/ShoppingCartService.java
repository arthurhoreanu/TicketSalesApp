package service;

import model.ShoppingCart;
import model.Ticket;
import repository.IRepository;

import java.util.List;

public class ShoppingCartService {
    private final IRepository<ShoppingCart> shoppingCartRepository;

    public ShoppingCartService(IRepository<ShoppingCart> shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    // Adds a ticket to the shopping cart
    public boolean addTicketToCart(ShoppingCart cart, Ticket ticket) {
        List<Ticket> items = cart.getItems();
        boolean ticketExists = false;

        // Check if the ticket is already in the cart
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(ticket)) {
                ticketExists = true;
                break;
            }
        }

        if (!ticketExists) {
            items.add(ticket);
            updateTotalPrice(cart);
            shoppingCartRepository.update(cart);
            return true;
        }
        return false;
    }

    // Removes a ticket from the shopping cart
    public boolean removeTicketFromCart(ShoppingCart cart, Ticket ticket) {
        List<Ticket> items = cart.getItems();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(ticket)) {
                items.remove(i);
                updateTotalPrice(cart);
                shoppingCartRepository.update(cart);
                return true;
            }
        }
        return false;
    }

    // Clears all tickets from the shopping cart
    public void clearCart(ShoppingCart cart) {
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        shoppingCartRepository.update(cart);
    }

    // Checks out the shopping cart, marking tickets as sold
    public void checkout(ShoppingCart cart, String purchaserName) {
        List<Ticket> items = cart.getItems();

        for (int i = 0; i < items.size(); i++) {
            items.get(i).markAsSold(purchaserName);
        }

        clearCart(cart);
    }

    // Calculates the total price of items in the shopping cart
    public void updateTotalPrice(ShoppingCart cart) { // Changed from private to public
        double totalPrice = 0.0;
        List<Ticket> items = cart.getItems();

        for (int i = 0; i < items.size(); i++) {
            totalPrice += items.get(i).getPrice();
        }

        cart.setTotalPrice(totalPrice);
    }

    // Retrieves the total price of the shopping cart
    public double getTotalPrice(ShoppingCart cart) {
        return cart.getTotalPrice();
    }
}
