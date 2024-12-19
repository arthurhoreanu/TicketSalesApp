package controller;

import model.*;
import service.OrderService;

import java.util.List;

/**
 * The OrderController class handles operations related to orders,
 * such as creating, processing, and managing order history.
 */
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructs an OrderController with the specified OrderService.
     *
     * @param orderService the service managing order-related operations
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new order for all tickets in the customer's shopping cart.
     *
     * @param customer the customer creating the order
     */
    public void createOrder(Customer customer) {
        Order order = orderService.createOrder(customer);
        if (order != null) {
            System.out.println("Order created successfully with ID: " + order.getID());
        } else {
            System.out.println("Shopping cart is empty. Order creation failed.");
        }
    }

    /**
     * Processes payment for a given order.
     *
     * @param order         the order to process payment for
     * @param cardNumber    the card number for payment
     * @param cvv           the CVV of the card
     * @param cardOwner     the name of the card owner
     * @param expirationDate the expiration date of the card
     */
    public void processOrderPayment(Order order, String cardNumber, int cvv, String cardOwner, String expirationDate) {
        boolean success = orderService.processOrderPayment(order, cardNumber, cvv, cardOwner, expirationDate);
        if (success) {
            System.out.println("Payment processed successfully for Order ID: " + order.getID());
        } else {
            System.out.println("Payment failed. Please check payment details and try again.");
        }
    }

    /**
     * Cancels an order by its ID.
     *
     * @param orderID the ID of the order to cancel
     */
    public void cancelOrder(int orderID) {
        boolean success = orderService.cancelOrder(orderID);
        if (success) {
            System.out.println("Order with ID " + orderID + " has been canceled successfully.");
        } else {
            System.out.println("Order with ID " + orderID + " could not be canceled.");
        }
    }

    /**
     * Retrieves the order history for a specific customer.
     *
     * @param customer the customer whose order history is requested
     * @return a list of orders belonging to the customer
     */
    public List<Order> getOrderHistory(Customer customer) {
        List<Order> orders = orderService.getOrderHistory(customer);
        if (orders.isEmpty()) {
            System.out.println("No orders found for customer: " + customer.getUsername());
        } else {
            System.out.println("Order history for customer: " + customer.getUsername());
            orders.forEach(order -> System.out.println(order));
        }
        return orders;
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderID the ID of the order to retrieve
     * @return the order with the specified ID, or null if not found
     */
    public Order getOrderByID(int orderID) {
        Order order = orderService.getOrderByID(orderID);
        if (order != null) {
            System.out.println("Order found: " + order);
        } else {
            System.out.println("No order found with ID: " + orderID);
        }
        return order;
    }

    /**
     * Calculates the total price for a specific order.
     *
     * @param order the order to calculate the total price for
     */
    public void calculateOrderTotal(Order order) {
        double total = orderService.calculateOrderTotal(order);
        System.out.println("Total price for Order ID " + order.getID() + ": $" + total);
    }

    /**
     * Places an order for all tickets in the customer's shopping cart.
     *
     * @param customer the customer placing the order
     */
    public void orderAllTicketsFromCart(Customer customer) {
        Order order = orderService.orderAllTicketsFromCart(customer);
        if (order != null) {
            System.out.println("Order for all tickets from cart created successfully. Order ID: " + order.getID());
        } else {
            System.out.println("Failed to create order. Cart may be empty.");
        }
    }

    /**
     * Places an order for tickets for a specific event from the customer's shopping cart.
     *
     * @param customer the customer placing the order
     * @param event    the event for which tickets are ordered
     */
    public void orderTicketsForEvent(Customer customer, Event event) {
        Order order = orderService.orderTicketsForEvent(customer, event);
        if (order != null) {
            System.out.println("Order created successfully for event: " + event.getEventName() + ". Order ID: " + order.getID());
        } else {
            System.out.println("Failed to create order for event: " + event.getEventName() + ". No tickets found in the cart.");
        }
    }

    /**
     * Places an order for tickets directly for a specific event.
     *
     * @param customer   the customer placing the order
     * @param event      the event for which tickets are ordered
     * @param ticketIDs  the list of ticket IDs to be included in the order
     */
    public void orderTicketsDirectlyForEvent(Customer customer, Event event, List<Integer> ticketIDs) {
        try {
            Order order = orderService.orderTicketsDirectlyForEvent(customer, event, ticketIDs);
            System.out.println("Order created successfully for event: " + event.getEventName() + ". Order ID: " + order.getID());
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }
}
