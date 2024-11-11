package service;

import model.Order;
import model.ShoppingCart;
import model.Ticket;
import model.Customer;
import model.PaymentProcessor;
import model.OrderStatus;
import repository.IRepository;
import model.Event;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final IRepository<Order> orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final PaymentProcessor paymentProcessor;
    private final SeatService seatService;
    private final IRepository<Ticket> ticketRepository;

    public OrderService(IRepository<Order> orderRepository, ShoppingCartService shoppingCartService, PaymentProcessor paymentProcessor, SeatService seatService, IRepository<Ticket> ticketRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
        this.paymentProcessor = paymentProcessor;
        this.seatService = seatService;
        this.ticketRepository = ticketRepository;
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
    //TODO check this
    // Method to order all tickets from the customer's cart
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
