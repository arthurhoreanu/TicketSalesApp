package controller;

import model.ShoppingCart;
import model.Ticket;
import service.ShoppingCartService;

public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    // Adds a ticket to the shopping cart
    public void addTicketToCart(ShoppingCart cart, Ticket ticket) {
        boolean isAdded = shoppingCartService.addTicketToCart(cart, ticket);
        if (isAdded) {
            System.out.println("Ticket added to cart successfully.");
        } else {
            System.out.println("Ticket is already in the cart.");
        }
    }

    // Removes a ticket from the shopping cart
    public void removeTicketFromCart(ShoppingCart cart, Ticket ticket) {
        boolean isRemoved = shoppingCartService.removeTicketFromCart(cart, ticket);
        if (isRemoved) {
            System.out.println("Ticket removed from cart successfully.");
        } else {
            System.out.println("Ticket not found in the cart.");
        }
    }

    // Clears all tickets from the shopping cart
    public void clearCart(ShoppingCart cart) {
        shoppingCartService.clearCart(cart);
        System.out.println("Shopping cart has been cleared.");
    }

    // Checks out the shopping cart, marking tickets as sold
    public void checkout(ShoppingCart cart, String purchaserName) {
        shoppingCartService.checkout(cart, purchaserName);
        System.out.println("Checkout completed. Tickets have been marked as sold.");
    }

    // Updates the total price of items in the shopping cart
    public void updateTotalPrice(ShoppingCart cart) {
        shoppingCartService.updateTotalPrice(cart);
        System.out.println("Total price has been updated. Current total: $" + cart.getTotalPrice());
    }

    // Retrieves and displays the total price of the shopping cart
    public void getTotalPrice(ShoppingCart cart) {
        double totalPrice = shoppingCartService.getTotalPrice(cart);
        System.out.println("Total price of the shopping cart: $" + totalPrice);
    }
}
