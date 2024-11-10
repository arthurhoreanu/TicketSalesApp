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
        if (!cart.getItems().contains(ticket)) {
            cart.getItems().add(ticket);
            updateTotalPrice(cart);
            shoppingCartRepository.update(cart);
            return true;
        }
        return false;
    }

    // Removes a ticket from the shopping cart
    public boolean removeTicketFromCart(ShoppingCart cart, Ticket ticket) {
        if (cart.getItems().remove(ticket)) {
            updateTotalPrice(cart);
            shoppingCartRepository.update(cart);
            return true;
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
        for (Ticket ticket : cart.getItems()) {
            ticket.markAsSold(purchaserName);
        }
        clearCart(cart);
    }

    // Calculates the total price of items in the shopping cart
    private void updateTotalPrice(ShoppingCart cart) {
        double totalPrice = 0.0;
        for (Ticket ticket : cart.getItems()) {
            totalPrice += ticket.getPrice();
        }
        cart.setTotalPrice(totalPrice);
    }

    // Retrieves the total price of the shopping cart
    public double getTotalPrice(ShoppingCart cart) {
        return cart.getTotalPrice();
    }
}
