package service;

import model.Order;
import model.ShoppingCart;
import model.Ticket;
import model.Customer;
import model.PaymentProcessor;
import model.OrderStatus;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final IRepository<Order> orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final PaymentProcessor paymentProcessor;

    public OrderService(IRepository<Order> orderRepository, ShoppingCartService shoppingCartService, PaymentProcessor paymentProcessor) {
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
        this.paymentProcessor = paymentProcessor;
    }

    // Creates a new order from a customer's shopping cart
    public Order createOrder(Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        List<Ticket> tickets = new ArrayList<>(cart.getItems());
        Order order = new Order(customer, tickets);
        orderRepository.create(order);
        shoppingCartService.clearCart(cart); // Clear the cart after creating the order
        return order;
    }
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

    // Cancels an order and releases tickets if not completed
    public boolean cancelOrder(int orderId) {
        Order order = getOrderById(orderId);
        if (order != null && !order.isCompleted()) {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.update(order);
            releaseTickets(order.getTickets());
            return true;
        }
        return false;
    }

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

    // Helper method to get an order by its ID
    public Order getOrderById(int orderId) {
        List<Order> allOrders = orderRepository.getAll();

        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            if (order.getID() == orderId) {
                return order;
            }
        }
        return null;
    }

    // Helper method to calculate the total price of an order
    private double calculateOrderTotal(Order order) {
        double total = 0;
        List<Ticket> tickets = order.getTickets();

        for (int i = 0; i < tickets.size(); i++) {
            total += tickets.get(i).getPrice();
        }
        return total;
    }

    // Releases tickets back to the available state if an order is canceled
    private void releaseTickets(List<Ticket> tickets) {
        for (int i = 0; i < tickets.size(); i++) {
            tickets.get(i).setSold(false);
        }
    }
}
