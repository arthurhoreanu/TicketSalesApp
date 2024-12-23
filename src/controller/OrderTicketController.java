package controller;

import model.Ticket;
import service.OrderTicketService;

import java.util.List;

/**
 * The OrderTicketController class handles operations related to OrderTicket management,
 * such as adding tickets to orders and retrieving tickets associated with orders.
 */
public class OrderTicketController {

    private final OrderTicketService orderTicketService;

    /**
     * Constructs an OrderTicketController with the specified OrderTicketService.
     *
     * @param orderTicketService the service managing OrderTicket-related operations
     */
    public OrderTicketController(OrderTicketService orderTicketService) {
        this.orderTicketService = orderTicketService;
    }

    /**
     * Adds an OrderTicket, linking an order with a ticket.
     *
     * @param order  the order to associate with the ticket
     * @param ticket the ticket to associate with the order
     */
    public void addOrderTicket(Order order, Ticket ticket) {
        orderTicketService.addOrderTicket(order, ticket);
        System.out.println("OrderTicket created successfully: Order ID = " + order.getID() +
                ", Ticket ID = " + ticket.getID());
    }

    /**
     * Retrieves all tickets associated with a specific order.
     *
     * @param order the order for which to retrieve tickets
     * @return a list of tickets associated with the specified order
     */
    public List<Ticket> getTicketsByOrder(Order order) {
        List<Ticket> tickets = orderTicketService.getTicketsByOrder(order);
        if (tickets.isEmpty()) {
            System.out.println("No tickets found for Order ID: " + order.getID());
        } else {
            System.out.println("Tickets for Order ID " + order.getID() + ":");
            tickets.forEach(ticket -> System.out.println("- Ticket ID: " + ticket.getID() +
                    ", Price: $" + ticket.getPrice()));
        }
        return tickets;
    }

    /**
     * Retrieves all OrderTicket entities.
     *
     * @return a list of all OrderTickets
     */
    public List<OrderTicket> getAllOrderTickets() {
        List<OrderTicket> orderTickets = orderTicketService.getAllOrderTickets();
        if (orderTickets.isEmpty()) {
            System.out.println("No OrderTickets found.");
        } else {
            System.out.println("All OrderTickets:");
            orderTickets.forEach(orderTicket -> System.out.println(
                    "- OrderTicket ID: " + orderTicket.getID() +
                            ", Order ID: " + orderTicket.getOrderID() +
                            ", Ticket ID: " + orderTicket.getTicketID()
            ));
        }
        return orderTickets;
    }
}
