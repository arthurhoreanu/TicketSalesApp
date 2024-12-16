package service;

import model.*;
import repository.IRepository;
import repository.FileRepository;
import repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing OrderTicket operations.
 */
public class OrderTicketService {

    private final IRepository<OrderTicket> orderTicketRepository;
    private final FileRepository<OrderTicket> orderTicketFileRepository;
    private final DBRepository<OrderTicket> orderTicketDatabaseRepository;
    private final TicketService ticketService;

    /**
     * Constructs an OrderTicketService with dependencies for managing OrderTicket entities.
     *
     * @param orderTicketRepository the repository for managing OrderTicket persistence.
     * @param ticketService         the service for managing tickets.
     */
    public OrderTicketService(IRepository<OrderTicket> orderTicketRepository, TicketService ticketService) {
        this.orderTicketRepository = orderTicketRepository;
        this.ticketService = ticketService;

        this.orderTicketFileRepository = new FileRepository<>("src/repository/data/orderTickets.csv", OrderTicket::fromCsv);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.orderTicketDatabaseRepository = new DBRepository<>(entityManagerFactory, OrderTicket.class);

        syncFromCsv();
        syncFromDatabase();
    }

    private void syncFromCsv() {
        List<OrderTicket> tickets = orderTicketFileRepository.getAll();
        for (OrderTicket ticket : tickets) {
            orderTicketRepository.create(ticket);
        }
    }

    private void syncFromDatabase() {
        List<OrderTicket> tickets = orderTicketDatabaseRepository.getAll();
        for (OrderTicket ticket : tickets) {
            orderTicketRepository.create(ticket);
        }
    }

    /**
     * Adds an OrderTicket linking an order and a ticket.
     *
     * @param order  the order to associate with the ticket.
     * @param ticket the ticket to associate with the order.
     */
    public void addOrderTicket(Order order, Ticket ticket) {
        OrderTicket orderTicket = new OrderTicket(order.getID(), ticket.getID());
        orderTicketRepository.create(orderTicket);
        orderTicketFileRepository.create(orderTicket);
        orderTicketDatabaseRepository.create(orderTicket);
    }

    /**
     * Retrieves all tickets associated with an order.
     *
     * @param order the order to retrieve tickets for.
     * @return a list of Ticket objects associated with the order.
     */
    public List<Ticket> getTicketsByOrder(Order order) {
        return orderTicketRepository.getAll().stream()
                .filter(orderTicket -> orderTicket.getOrderID() == order.getID())
                .map(orderTicket -> ticketService.findTicketByID(orderTicket.getTicketID()))
                .collect(Collectors.toList());
    }
}
