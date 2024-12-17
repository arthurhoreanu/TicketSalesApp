package service;

import model.*;
import repository.IRepository;
import repository.FileRepository;
import repository.DBRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Service class for managing ShoppingCart-related operations.
 */
public class ShoppingCartService {

    private final IRepository<ShoppingCart> shoppingCartRepository;
    private final FileRepository<ShoppingCart> shoppingCartFileRepository;
    private final DBRepository<ShoppingCart> shoppingCartDatabaseRepository;
    private final ShoppingCartTicketService shoppingCartTicketService;

    /**
     * Constructs a ShoppingCartService with dependencies for managing shopping carts.
     *
     * @param shoppingCartRepository the repository for managing ShoppingCart persistence.
     * @param shoppingCartTicketService the service for managing ShoppingCartTicket entities.
     */
    public ShoppingCartService(IRepository<ShoppingCart> shoppingCartRepository,
                               ShoppingCartTicketService shoppingCartTicketService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartTicketService = shoppingCartTicketService;

        // Initialize file and database repositories
        this.shoppingCartFileRepository = new FileRepository<>("src/repository/data/shoppingCarts.csv", ShoppingCart::fromCSV);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketSalesPU");
        this.shoppingCartDatabaseRepository = new DBRepository<>(entityManagerFactory, ShoppingCart.class);

        // Sync data
        syncFromCsv();
        syncFromDatabase();
    }

    /**
     * Synchronizes shopping carts from the CSV file into the main repository.
     */
    private void syncFromCsv() {
        List<ShoppingCart> carts = shoppingCartFileRepository.getAll();
        for (ShoppingCart cart : carts) {
            shoppingCartRepository.create(cart);
        }
    }

    /**
     * Synchronizes shopping carts from the database into the main repository.
     */
    private void syncFromDatabase() {
        List<ShoppingCart> carts = shoppingCartDatabaseRepository.getAll();
        for (ShoppingCart cart : carts) {
            shoppingCartRepository.create(cart);
        }
    }

    /**
     * Creates a new shopping cart.
     *
     * @return the newly created ShoppingCart object.
     */
    public ShoppingCart createShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        shoppingCartRepository.create(cart);
        shoppingCartFileRepository.create(cart);
        shoppingCartDatabaseRepository.create(cart);
        return cart;
    }

    /**
     * Adds a ticket to a shopping cart.
     *
     * @param shoppingCart the shopping cart to add the ticket to.
     * @param event        the event associated with the ticket.
     * @param ticket       the ticket to add to the shopping cart.
     * @return true if the ticket was successfully added, false otherwise.
     */
    public boolean addTicketToCart(ShoppingCart shoppingCart, Event event, Ticket ticket) {
        boolean added = shoppingCartTicketService.addTicketToShoppingCart(shoppingCart, event, ticket);
        if (added) {
            updateTotalPrice(shoppingCart);
        }
        return added;
    }

    /**
     * Removes a ticket from the shopping cart.
     *
     * @param shoppingCart the shopping cart to remove the ticket from.
     * @param ticket       the ticket to remove.
     * @return true if the ticket was successfully removed, false otherwise.
     */
    public boolean removeTicketFromCart(ShoppingCart shoppingCart, Ticket ticket) {
        List<ShoppingCartTicket> tickets = shoppingCartTicketService.getTicketsByShoppingCart(shoppingCart);
        ShoppingCartTicket ticketToRemove = tickets.stream()
                .filter(cartTicket -> cartTicket.getTicket().equals(ticket))
                .findFirst()
                .orElse(null);

        if (ticketToRemove != null) {
            shoppingCartTicketService.removeTicketFromShoppingCart(ticketToRemove);
            updateTotalPrice(shoppingCart);
            return true;
        }
        return false;
    }

    /**
     * Updates the total price of the shopping cart based on its tickets.
     *
     * @param shoppingCart the shopping cart to update.
     */
    private void updateTotalPrice(ShoppingCart shoppingCart) {
        List<ShoppingCartTicket> tickets = shoppingCartTicketService.getTicketsByShoppingCart(shoppingCart);
        double totalPrice = tickets.stream()
                .mapToDouble(cartTicket -> cartTicket.getTicket().getPrice())
                .sum();
        shoppingCart.setTotalPrice(totalPrice);

        // Update in repositories
        shoppingCartRepository.update(shoppingCart);
        shoppingCartFileRepository.update(shoppingCart);
        shoppingCartDatabaseRepository.update(shoppingCart);
    }

    public void clearCart(ShoppingCart shoppingCart) {
        List<ShoppingCartTicket> tickets = shoppingCartTicketService.getTicketsByShoppingCart(shoppingCart);
        for (ShoppingCartTicket ticket : tickets) {
            shoppingCartTicketService.removeTicketFromShoppingCart(ticket);
        }
        shoppingCart.setTotalPrice(0.0);
        shoppingCartRepository.update(shoppingCart);
        shoppingCartFileRepository.update(shoppingCart);
        shoppingCartDatabaseRepository.update(shoppingCart);
    }

    public List<ShoppingCartTicket> getTicketsByShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartTicketService.getTicketsByShoppingCart(shoppingCart);
    }

    public ShoppingCart findShoppingCartByID(int shoppingCartID) {
        return shoppingCartRepository.read(shoppingCartID);
    }

}
