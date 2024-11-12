package controller;

import model.*;
import service.OrderService;

import java.util.List;

public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Creates a new order from the customer's shopping cart
    public void createOrder(Customer customer) {
        Order order = orderService.createOrder(customer);
        if (order != null) {
            System.out.println("Order created successfully for customer " + customer.getUsername() + " with order ID: " + order.getID());
        } else {
            System.out.println("Failed to create order. Please ensure the shopping cart is not empty.");
        }
    }

    // Processes payment for an order
    public void processOrderPayment(Order order, String cardNumber, int cvv, String cardOwner, String expirationDate) {
        boolean paymentSuccessful = orderService.processOrderPayment(order, cardNumber, cvv, cardOwner, expirationDate);
        if (paymentSuccessful) {
            System.out.println("Payment processed successfully for order ID: " + order.getID());
        } else {
            System.out.println("Payment failed. Please check the payment details and try again.");
        }
    }

    // Cancels an order by its ID
    public void cancelOrder(int orderId) {
        boolean canceled = orderService.cancelOrder(orderId);
        if (canceled) {
            System.out.println("Order with ID " + orderId + " has been canceled successfully.");
        } else {
            System.out.println("Order cancellation failed. The order may already be completed or not found.");
        }
    }

    // Retrieves order history for a customer
    public void getOrderHistory(Customer customer) {
        List<Order> history = orderService.getOrderHistory(customer);
        if (history.isEmpty()) {
            System.out.println("No orders found for customer " + customer.getUsername());
        } else {
            System.out.println("Order history for customer " + customer.getUsername() + ":");
            history.forEach(order -> System.out.println(order.toString()));
        }
    }

    // Retrieves an order by its ID
    public void getOrderById(int orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            System.out.println("Order found: " + order.toString());
        } else {
            System.out.println("Order with ID " + orderId + " does not exist.");
        }
    }

    // Orders all tickets from the customer's shopping cart
    public Order orderAllTicketsFromCart(Customer customer) {
        Order order = orderService.orderAllTicketsFromCart(customer);
        if (order != null) {
            System.out.println("Order created successfully for all items in the cart. Order ID: " + order.getID());
        } else {
            System.out.println("Failed to create order. The shopping cart may be empty.");
        }
        return order;
    }

    // Orders tickets for a specific event from the customer's cart
    public void orderTicketsForEvent(Customer customer, Event event) {
        Order order = orderService.orderTicketsForEvent(customer, event);
        if (order != null) {
            System.out.println("Order created for tickets associated with event " + event.getEventName() + ". Order ID: " + order.getID());
        } else {
            System.out.println("No tickets found in the cart for event " + event.getEventName() + ".");
        }
    }
}
