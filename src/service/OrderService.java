package service;

import model.*;
import repository.IRepository;
import repository.FileRepository;
import repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing orders and their related operations.
 */
public class OrderService {

    private final IRepository<Order> orderRepository;
    private final FileRepository<Order> orderFileRepository;
    private final DBRepository<Order> orderDatabaseRepository;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartTicketService shoppingCartTicketService;
    private final PaymentProcessor paymentProcessor;
    private final OrderTicketService orderTicketService;
    private final TicketService ticketService;
    private final SeatService seatService;

    /**
     * Constructs an OrderService with dependencies for managing orders.
     */
    public OrderService(IRepository<Order> orderRepository, ShoppingCartService shoppingCartService, ShoppingCartTicketService shoppingCartTicketService,
                        PaymentProcessor paymentProcessor, OrderTicketService orderTicketService, TicketService ticketService, SeatService seatService) {
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
        this.shoppingCartTicketService = shoppingCartTicketService;
        this.paymentProcessor = paymentProcessor;
        this.orderTicketService = orderTicketService;
        this.ticketService = ticketService;
        this.seatService = seatService;

        this.orderFileRepository = new FileRepository<>("src/repository/data/orders.csv", Order::fromCsv);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.orderDatabaseRepository = new DBRepository<>(entityManagerFactory, Order.class);

        syncFromCsv();
        syncFromDatabase();
    }

    private void syncFromCsv() {
        List<Order> orders = orderFileRepository.getAll();
        for (Order order : orders) {
            orderRepository.create(order);
        }
    }

    private void syncFromDatabase() {
        List<Order> orders = orderDatabaseRepository.getAll();
        for (Order order : orders) {
            orderRepository.create(order);
        }
    }

    public Order createOrder(Customer customer) {
        List<ShoppingCartTicket> tickets = shoppingCartTicketService.getTicketsByShoppingCart(customer.getShoppingCart());
        if (tickets.isEmpty()) {
            return null; // No tickets to order
        }
        int newID = orderRepository.getAll().size() + 1;
        Order order = new Order(newID, customer.getID(), LocalDateTime.now(), OrderStatus.PENDING);
        orderRepository.create(order);

        for (ShoppingCartTicket cartTicket : tickets) {
            orderTicketService.addOrderTicket(order, cartTicket.getTicket());
            ticketService.reserveTicket(cartTicket.getTicket(), customer.getUsername());
        }
        shoppingCartService.clearCart(customer.getShoppingCart());
        return order;
    }

    public boolean processOrderPayment(Order order, String cardNumber, int cvv, String cardOwner, String expirationDate) {
        double total = calculateOrderTotal(order);
        if (!paymentProcessor.enterPaymentDetails(cardNumber, cvv, cardOwner, expirationDate)) {
            return false;
        }

        boolean success = paymentProcessor.processPayment(total);
        if (success) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.update(order);
        }
        return success;
    }

    public boolean cancelOrder(int orderID) {
        Order order = orderRepository.read(orderID);
        if (order != null && order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.update(order);
            List<Ticket> tickets = orderTicketService.getTicketsByOrder(order);
            for (Ticket ticket : tickets) {
                ticketService.releaseTicket(ticket);
            }
            return true;
        }
        return false;
    }

    public List<Order> getOrderHistory(Customer customer) {
        return orderRepository.getAll().stream()
                .filter(order -> order.getUserID() == customer.getID())
                .collect(Collectors.toList());
    }

    public Order getOrderByID(int orderID) {
        return orderRepository.read(orderID);
    }

    public double calculateOrderTotal(Order order) {
        return orderTicketService.getTicketsByOrder(order).stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
    }

    public Order orderAllTicketsFromCart(Customer customer) {
        return createOrder(customer);
    }

    public Order orderTicketsForEvent(Customer customer, Event event) {
        List<ShoppingCartTicket> eventTickets = shoppingCartTicketService.getTicketsByShoppingCart(customer.getShoppingCart()).stream()
                .filter(cartTicket -> cartTicket.getEvent().equals(event))
                .collect(Collectors.toList());
        if (eventTickets.isEmpty()) {
            return null;
        }
        int newID = orderRepository.getAll().size() + 1;
        Order order = new Order(newID, customer.getID(), LocalDateTime.now(), OrderStatus.PENDING);
        orderRepository.create(order);

        for (ShoppingCartTicket cartTicket : eventTickets) {
            orderTicketService.addOrderTicket(order, cartTicket.getTicket());
            ticketService.reserveTicket(cartTicket.getTicket(), customer.getUsername());
            shoppingCartService.removeTicketFromCart(customer.getShoppingCart(), cartTicket.getTicket());
        }

        return order;
    }

    public Order orderTicketsDirectlyForEvent(Customer customer, Event event, List<Integer> ticketIDs) {
        // Step 1: Fetch the tickets
        List<Ticket> ticketsToOrder = new ArrayList<>();
        for (Integer ticketID : ticketIDs) {
            Ticket ticket = ticketService.findTicketByID(ticketID);
            if (ticket != null && ticket.getEvent().equals(event) && !ticket.isSold()) {
                ticketsToOrder.add(ticket);
            } else {
                throw new IllegalArgumentException("Invalid or already sold ticket ID: " + ticketID);
            }
        }

        // Step 2: Create an order
        int newID = orderRepository.getAll().size() + 1;
        Order order = new Order(newID, customer.getID(), LocalDateTime.now(), OrderStatus.PENDING);
        orderRepository.create(order);

        // Step 3: Mark tickets as sold and reserve seats
        for (Ticket ticket : ticketsToOrder) {
            ticket.markAsSold(customer.getUsername());
            seatService.reserveSeatForEvent(ticket.getSeat(), event);

            // Associate the ticket with the order via OrderTicket
            orderTicketService.addOrderTicket(order, ticket);

            // Update ticket status in repositories
            ticketService.updateTicketInRepositories(ticket);
        }

        return order;
    }

}
