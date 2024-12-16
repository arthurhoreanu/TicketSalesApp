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
 * Service class for managing ShoppingCartTicket-related operations.
 */
public class ShoppingCartTicketService {

    private final IRepository<ShoppingCartTicket> shoppingCartTicketRepository;
    private final FileRepository<ShoppingCartTicket> shoppingCartTicketFileRepository;
    private final DBRepository<ShoppingCartTicket> shoppingCartTicketDatabaseRepository;
    private final TicketService ticketService;

    /**
     * Constructs a ShoppingCartTicketService with dependencies for managing shopping cart tickets.
     *
     * @param shoppingCartTicketRepository the repository for managing ShoppingCartTicket persistence.
     * @param ticketService                 the service for managing tickets.
     */
    public ShoppingCartTicketService(IRepository<ShoppingCartTicket> shoppingCartTicketRepository, TicketService ticketService) {
        this.shoppingCartTicketRepository = shoppingCartTicketRepository;
        this.ticketService = ticketService;

        // Initialize file and database repositories
        this.shoppingCartTicketFileRepository = new FileRepository<>("src/repository/data/shoppingCartTickets.csv", ShoppingCartTicket::fromCSV);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.shoppingCartTicketDatabaseRepository = new DBRepository<>(entityManagerFactory, ShoppingCartTicket.class);

        // Sync data
        syncFromCsv();
        syncFromDatabase();
    }

    /**
     * Synchronizes shopping cart tickets from the CSV file into the main repository.
     */
    private void syncFromCsv() {
        List<ShoppingCartTicket> tickets = shoppingCartTicketFileRepository.getAll();
        for (ShoppingCartTicket ticket : tickets) {
            shoppingCartTicketRepository.create(ticket);
        }
    }

    /**
     * Synchronizes shopping cart tickets from the database into the main repository.
     */
    private void syncFromDatabase() {
        List<ShoppingCartTicket> tickets = shoppingCartTicketDatabaseRepository.getAll();
        for (ShoppingCartTicket ticket : tickets) {
            shoppingCartTicketRepository.create(ticket);
        }
    }

    /**
     * Adds a ticket to a shopping cart for a specific event.
     *
     * @param shoppingCart the shopping cart to add the ticket to.
     * @param event        the event associated with the ticket.
     * @param ticket       the ticket to add to the shopping cart.
     * @return true if the ticket was successfully added, false otherwise.
     */
    public boolean addTicketToShoppingCart(ShoppingCart shoppingCart, Event event, Ticket ticket) {
        if (shoppingCart.getTickets().stream()
                .anyMatch(cartTicket -> !cartTicket.getEvent().equals(event))) {
            // Prevent adding tickets from different events
            return false;
        }

        ShoppingCartTicket shoppingCartTicket = new ShoppingCartTicket(shoppingCart, event, ticket);
        shoppingCartTicketRepository.create(shoppingCartTicket);
        shoppingCartTicketFileRepository.create(shoppingCartTicket);
        shoppingCartTicketDatabaseRepository.create(shoppingCartTicket);
        return true;
    }

    /**
     * Retrieves all tickets in a shopping cart.
     *
     * @param shoppingCart the shopping cart to retrieve tickets for.
     * @return a list of ShoppingCartTicket objects.
     */
    public List<ShoppingCartTicket> getTicketsByShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartTicketRepository.getAll().stream()
                .filter(cartTicket -> cartTicket.getShoppingCart().equals(shoppingCart))
                .collect(Collectors.toList());
    }

    /**
     * Removes a ticket from a shopping cart.
     *
     * @param shoppingCartTicket the ShoppingCartTicket to remove.
     * @return true if the ticket was successfully removed, false otherwise.
     */
    public boolean removeTicketFromShoppingCart(ShoppingCartTicket shoppingCartTicket) {
        shoppingCartTicketRepository.delete(shoppingCartTicket.getID());
        shoppingCartTicketFileRepository.delete(shoppingCartTicket.getID());
        shoppingCartTicketDatabaseRepository.delete(shoppingCartTicket.getID());
        return true;
    }
}
