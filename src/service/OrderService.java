package service;


import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;


/**
 * Service class responsible for managing orders, including creating orders, processing payments,
 * and managing order history. This service interacts with the shopping cart, payment processor,
 * ticket repository, and seat service to fulfill order-related tasks.
 */

public class OrderService {
    private final IRepository<Order> orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final PaymentProcessor paymentProcessor;
    private final SeatService seatService;
    private final IRepository<Ticket> ticketRepository;
    private final FileRepository<Order> orderFileRepository;

    public OrderService(IRepository<Order> orderRepository, ShoppingCartService shoppingCartService, PaymentProcessor paymentProcessor, SeatService seatService, IRepository<Ticket> ticketRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
        this.paymentProcessor = paymentProcessor;
        this.seatService = seatService;
        this.ticketRepository = ticketRepository;
        this.orderFileRepository = new FileRepository<>("src/repository/data/orders.csv", Order::fromCsvFormat);
        List<Order> ordersfromFile = orderFileRepository.getAll();
        for (Order order : ordersfromFile) {
            orderRepository.create(order);
        }
    }

    /**
     * Creates a new order from a customer's shopping cart. Clears the shopping cart after the order is created.
     *
     * @param customer The customer for whom the order is being created.
     * @return The created order, containing the tickets from the shopping cart.
     */

    // Creates a new order from a customer's shopping cart
    public Order createOrder(Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        List<Ticket> tickets = new ArrayList<>(cart.getItems());
        Order order = new Order(customer, tickets);
        orderRepository.create(order);
        shoppingCartService.clearCart(cart); // Clear the cart after creating the order
        return order;
    }

    /**
     * Processes payment for an order using the specified payment details.
     * Sets the order status to COMPLETED if the payment is successful.
     *
     * @param order         The order being paid for.
     * @param cardNumber    The card number for payment.
     * @param cvv           The CVV for payment verification.
     * @param cardOwner     The name of the card owner.
     * @param expirationDate The expiration date of the card.
     * @return True if the payment is successful, false otherwise.
     */

    // injections!!!
    // Processes payment for an order and finalizes it
    public boolean processOrderPayment(Order order, String cardNumber, int cvv, String cardOwner, String expirationDate) {
        double totalAmount = calculateOrderTotal(order);

        // Enter and validate payment details through PaymentProcessor
        boolean paymentDetailsValid = paymentProcessor.enterPaymentDetails(cardNumber, cvv, cardOwner, expirationDate);
        if (!paymentDetailsValid) {
            return false; // Return false if payment details are invalid
        }

        // Process payment and check the result
        boolean paymentSuccessful = paymentProcessor.processPayment(totalAmount);
        if (paymentSuccessful) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.update(order);
            return true;
        }
        return false;
    }

    /**
     * Cancels an order if it is not already completed. Releases the associated tickets and sets the order status to CANCELED.
     *
     * @param orderId The ID of the order to cancel.
     * @return True if the order is successfully canceled, false otherwise.
     */

    // Cancels an order and releases tickets if not completed
    public boolean cancelOrder(int orderId) {
        Order order = getOrderByID(orderId);
        if (order != null && !order.isCompleted()) {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.update(order);
            releaseTickets(order.getTickets());
            return true;
        }
        return false;
    }

    /**
     * Retrieves the order history for a specified customer.
     *
     * @param customer The customer whose order history is being retrieved.
     * @return A list of orders associated with the customer.
     */

    // Retrieves order history for a customer
    public List<Order> getOrderHistory(Customer customer) {
        List<Order> history = new ArrayList<>();
        List<Order> allOrders = orderRepository.getAll();

        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            if (order.getUser().equals(customer)) {
                history.add(order);
            }
        }
        return history;
    }

    /**
     * Retrieves an order by its unique ID.
     *
     * @param orderID The unique identifier of the order.
     * @return The order with the specified ID, or null if not found.
     */

    // Helper method to get an order by its ID
    public Order getOrderByID(int orderID) {
        List<Order> allOrders = orderRepository.getAll();

        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            if (order.getID() == orderID) {
                return order;
            }
        }
        return null;
    }

    /**
     * Calculates the total price of all tickets in the order.
     *
     * @param order The order for which the total price is being calculated.
     * @return The total price of the order.
     */

    // Helper method to calculate the total price of an order
    private double calculateOrderTotal(Order order) {
        double total = 0;
        List<Ticket> tickets = order.getTickets();

        for (int i = 0; i < tickets.size(); i++) {
            total += tickets.get(i).getPrice();
        }
        return total;
    }

    /**
     * Releases the tickets associated with a canceled order, making them available for other customers.
     *
     * @param tickets The list of tickets to be released.
     */

    // Releases tickets back to the available state if an order is canceled
    private void releaseTickets(List<Ticket> tickets) {
        for (int i = 0; i < tickets.size(); i++) {
            tickets.get(i).setSold(false);
        }
    }

    /**
     * Orders all tickets in the customer's shopping cart, marking them as sold and reserving their associated seats.
     * Clears the shopping cart after creating the order.
     *
     * @param customer The customer placing the order.
     * @return The created order, or null if the shopping cart is empty.
     */

    public Order orderAllTicketsFromCart(Customer customer) {
        // Retrieve the customer's shopping cart
        ShoppingCart cart = customer.getShoppingCart();
        List<Ticket> tickets = new ArrayList<>(cart.getItems());

        // If cart is empty, return null or handle as needed
        if (tickets.isEmpty()) {
            return null;
        }

        // Create an order for all tickets in the cart
        Order order = new Order(customer, tickets);
        orderRepository.create(order);

        // Mark tickets as sold and reserve the associated seats
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            ticket.markAsSold(customer.getUsername());
            seatService.reserveSeatForEvent(ticket.getSeat(), ticket.getEvent());
            ticketRepository.update(ticket); // Update ticket status in repository
        }

        // Clear the shopping cart after creating the order
        shoppingCartService.clearCart(cart);

        return order; // Return the created order
    }

    /**
     * Orders only the tickets for a specified event from the customer's shopping cart. The ordered tickets
     * are removed from the shopping cart after order creation.
     *
     * @param customer The customer placing the order.
     * @param event    The event for which tickets are being ordered.
     * @return The created order containing only the tickets for the specified event, or null if no such tickets are found.
     */

    // Method to order only tickets for a specific event from the customer's cart
    public Order orderTicketsForEvent(Customer customer, Event event) {
        // Retrieve the customer's shopping cart
        ShoppingCart cart = customer.getShoppingCart();
        List<Ticket> eventTickets = new ArrayList<>();

        // Find tickets in the cart that belong to the specified event
        for (int i = 0; i < cart.getItems().size(); i++) {
            Ticket ticket = cart.getItems().get(i);
            if (ticket.getEvent().equals(event)) {
                eventTickets.add(ticket);
            }
        }

        // If no tickets are found for the event, return null or handle as needed
        if (eventTickets.isEmpty()) {
            return null;
        }

        // Create an order for tickets associated with the event
        Order order = new Order(customer, eventTickets);
        orderRepository.create(order);

        // Mark tickets as sold and reserve the associated seats
        for (int i = 0; i < eventTickets.size(); i++) {
            Ticket ticket = eventTickets.get(i);
            ticket.markAsSold(customer.getUsername());
            seatService.reserveSeatForEvent(ticket.getSeat(), ticket.getEvent());
            ticketRepository.update(ticket); // Update ticket status in repository
        }

        // Remove event tickets from the shopping cart after creating the order
        for (int i = 0; i < eventTickets.size(); i++) {
            Ticket ticket = eventTickets.get(i);
            cart.getItems().remove(ticket);
        }
        shoppingCartService.updateTotalPrice(cart); // Recalculate the cart total

        return order; // Return the created order
    }

}
