package controller;

import model.*;
import service.OrderService;

import java.util.List;

/**
 * The OrderController class provides methods to handle operations related to orders, including
 * order creation, payment processing, cancellation, and retrieval of order history.
 */
public class OrderController {
    private final OrderService orderService;

    /**
     * Constructs an OrderController with the specified OrderService.
     *
     * @param orderService the service handling order-related operations
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new order for the specified customer based on the items in their shopping cart.
     *
     * @param customer the customer for whom the order is created
     */
    public void createOrder(Customer customer) {
        Order order = orderService.createOrder(customer);
        if (order != null) {
            System.out.println("Order created successfully for customer " + customer.getUsername() + " with order ID: " + order.getID());
        } else {
            System.out.println("Failed to create order. Please ensure the shopping cart is not empty.");
        }
    }

    /**
     * Processes the payment for a specified order using provided payment details.
     *
     * @param order       the order for which payment is to be processed
     * @param cardNumber  the card number for the payment
     * @param cvv         the CVV code for the card
     * @param cardOwner   the name of the card owner
     * @param expirationDate the expiration date of the card in MM/YY format
     */
    public void processOrderPayment(Order order, String cardNumber, int cvv, String cardOwner, String expirationDate) {
        boolean paymentSuccessful = orderService.processOrderPayment(order, cardNumber, cvv, cardOwner, expirationDate);
        if (paymentSuccessful) {
            System.out.println("Payment processed successfully for order ID: " + order.getID());
        } else {
            System.out.println("Payment failed. Please check the payment details and try again.");
        }
    }

    /**
     * Cancels an order with the specified order ID.
     *
     * @param orderId the ID of the order to be canceled
     */
    public void cancelOrder(int orderId) {
        boolean canceled = orderService.cancelOrder(orderId);
        if (canceled) {
            System.out.println("Order with ID " + orderId + " has been canceled successfully.");
        } else {
            System.out.println("Order cancellation failed. The order may already be completed or not found.");
        }
    }

    /**
     * Retrieves and prints the order history for a specified customer.
     *
     * @param customer the customer whose order history is to be retrieved
     */
    public void getOrderHistory(Customer customer) {
        List<Order> history = orderService.getOrderHistory(customer);
        if (history.isEmpty()) {
            System.out.println("No orders found for customer " + customer.getUsername());
        } else {
            System.out.println("Order history for customer " + customer.getUsername() + ":");
            history.forEach(order -> System.out.println(order.toString()));
        }
    }

    /**
     * Retrieves and prints the details of an order with the specified ID.
     *
     * @param orderID the ID of the order to be retrieved
     */
    public void getOrderByID(int orderID) {
        Order order = orderService.getOrderByID(orderID);
        if (order != null) {
            System.out.println("Order found: " + order.toString());
        } else {
            System.out.println("Order with ID " + orderID + " does not exist.");
        }
    }

    /**
     * Creates an order for all tickets in the customer's shopping cart and prints the order ID.
     *
     * @param customer the customer for whom the order is created
     * @return the created order, or null if the shopping cart is empty
     */
    public Order orderAllTicketsFromCart(Customer customer) {
        Order order = orderService.orderAllTicketsFromCart(customer);
        if (order != null) {
            System.out.println("Order created successfully for all items in the cart. Order ID: " + order.getID());
        } else {
            System.out.println("Failed to create order. The shopping cart may be empty.");
        }
        return order;
    }

    /**
     * Creates an order for tickets associated with a specific event in the customer's shopping cart.
     *
     * @param customer the customer for whom the order is created
     * @param event    the event for which tickets are ordered
     */
    public void orderTicketsForEvent(Customer customer, Event event) {
        Order order = orderService.orderTicketsForEvent(customer, event);
        if (order != null) {
            System.out.println("Order created for tickets associated with event " + event.getEventName() + ". Order ID: " + order.getID());
        } else {
            System.out.println("No tickets found in the cart for event " + event.getEventName() + ".");
        }
    }
}
