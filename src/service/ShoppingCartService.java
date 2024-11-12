package service;

import model.Order;
import model.ShoppingCart;
import model.Ticket;
import model.Customer;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartService {
    private final IRepository<ShoppingCart> shoppingCartRepository;
    private final IRepository<Order> orderRepository;
    private final CustomerService customerService; // Inject CustomerService to get the current customer

    public ShoppingCartService(IRepository<ShoppingCart> shoppingCartRepository, IRepository<Order> orderRepository, CustomerService customerService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

    // Adds a ticket to the shopping cart
    public boolean addTicketToCart(ShoppingCart cart, Ticket ticket) {
        List<Ticket> items = cart.getItems();
        if (items.contains(ticket)) {
            return false; // Ticket is already in the cart
        }
        items.add(ticket);
        updateTotalPrice(cart);
        shoppingCartRepository.update(cart);
        return true;
    }

    // Removes a ticket from the shopping cart
    public boolean removeTicketFromCart(ShoppingCart cart, Ticket ticket) {
        List<Ticket> items = cart.getItems();
        if (!items.remove(ticket)) {
            return false; // Ticket not found in the cart
        }
        updateTotalPrice(cart);
        shoppingCartRepository.update(cart);
        return true;
    }

    // Clears all tickets from the shopping cart
    public void clearCart(ShoppingCart cart) {
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        shoppingCartRepository.update(cart);
    }

    // Checks out the shopping cart, marking tickets as sold and creating an order
    public Order checkout(ShoppingCart cart) {
        List<Ticket> items = new ArrayList<>(cart.getItems());
        if (items.isEmpty()) {
            return null; // Return null if cart is empty to indicate checkout failure
        }

        Customer currentCustomer = customerService.getCurrentCustomer();

        for (Ticket ticket : items) {
            ticket.markAsSold(currentCustomer.getUsername());
        }

        clearCart(cart); // Clear the cart after checkout

        // Create and save a new Order for the customer
        Order order = new Order(currentCustomer, items);
        orderRepository.create(order);
        return order;  // Return the created Order
    }

    // Calculates the total price of items in the shopping cart
    public void updateTotalPrice(ShoppingCart cart) {
        double totalPrice = cart.getItems().stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
        cart.setTotalPrice(totalPrice);
    }

    // Retrieves the total price of the shopping cart
    public double getTotalPrice(ShoppingCart cart) {
        return cart.getTotalPrice();
    }
}
