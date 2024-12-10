package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing shopping cart-related operations, including adding, removing,
 * clearing items, and checking out.
 */
public class ShoppingCartService {
    private final IRepository<ShoppingCart> shoppingCartRepository;
    private final IRepository<Order> orderRepository;
    private final CustomerService customerService; // Inject CustomerService to get the current customer
    private final FileRepository<ShoppingCart> shoppingCartFileRepository;

    /**
     * Constructs a ShoppingCartService with the specified dependencies.
     *
     * @param shoppingCartRepository the repository for managing ShoppingCart persistence.
     * @param orderRepository        the repository for managing Order persistence.
     * @param customerService        the service used to retrieve the current customer.
     */
    public ShoppingCartService(IRepository<ShoppingCart> shoppingCartRepository, IRepository<Order> orderRepository, CustomerService customerService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.shoppingCartFileRepository = new FileRepository<>("src/repository/data/shoppingCarts.csv", ShoppingCart::fromCsv);
        List<ShoppingCart> shoppingCartsFromFile = shoppingCartFileRepository.getAll();
        for (ShoppingCart shoppingCart : shoppingCartsFromFile) {
            shoppingCartRepository.create(shoppingCart);
        }
    }

    /**
     * Adds a ticket to the specified shopping cart.
     * Updates the total price of the cart and saves the cart to the repository.
     *
     * @param cart   the shopping cart to which the ticket will be added.
     * @param ticket the ticket to be added to the cart.
     * @return true if the ticket was added successfully, false if it was already in the cart.
     */
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

    /**
     * Removes a ticket from the specified shopping cart.
     * Updates the total price of the cart and saves the cart to the repository.
     *
     * @param cart   the shopping cart from which the ticket will be removed.
     * @param ticket the ticket to be removed from the cart.
     * @return true if the ticket was removed successfully, false if it was not found in the cart.
     */
    public boolean removeTicketFromCart(ShoppingCart cart, Ticket ticket) {
        List<Ticket> items = cart.getItems();
        if (!items.remove(ticket)) {
            return false; // Ticket not found in the cart
        }
        updateTotalPrice(cart);
        shoppingCartRepository.update(cart);
        return true;
    }

    /**
     * Clears all tickets from the specified shopping cart.
     * Resets the total price of the cart and saves the cart to the repository.
     *
     * @param cart the shopping cart to be cleared.
     */
    public void clearCart(ShoppingCart cart) {
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        shoppingCartRepository.update(cart);
    }

    /**
     * Checks out the specified shopping cart, marking all tickets as sold, clearing the cart,
     * and creating an order for the current customer.
     *
     * @param cart the shopping cart to be checked out.
     * @return the created Order if checkout was successful, or null if the cart is empty.
     */
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

    /**
     * Calculates and updates the total price of items in the specified shopping cart.
     *
     * @param cart the shopping cart for which the total price will be calculated and updated.
     */
    public void updateTotalPrice(ShoppingCart cart) {
        double totalPrice = cart.getItems().stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
        cart.setTotalPrice(totalPrice);
    }

    /**
     * Retrieves the total price of items in the specified shopping cart.
     *
     * @param cart the shopping cart from which the total price will be retrieved.
     * @return the total price of the shopping cart.
     */
    public double getTotalPrice(ShoppingCart cart) {
        return cart.getTotalPrice();
    }
}
