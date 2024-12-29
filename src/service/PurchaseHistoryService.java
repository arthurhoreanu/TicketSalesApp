package service;

import model.Cart;
import model.Customer;
import model.PurchaseHistory;
import model.Ticket;
import repository.FileRepository;
import repository.IRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing purchase histories.
 */
public class PurchaseHistoryService {

    private final IRepository<PurchaseHistory> purchaseHistoryRepository;
    private final FileRepository<PurchaseHistory> purchaseHistoryFileRepository;

    /**
     * Constructs a PurchaseHistoryService with necessary dependencies.
     *
     * @param purchaseHistoryRepository The repository for managing purchase history persistence.
     */
    public PurchaseHistoryService(IRepository<PurchaseHistory> purchaseHistoryRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.purchaseHistoryFileRepository = new FileRepository<>("src/repository/data/purchaseHistories.csv", PurchaseHistory::fromCsv);
        syncFromCsv();
    }

    /**
     * Synchronizes purchase histories from the CSV file into the main repository.
     */
    private void syncFromCsv() {
        List<PurchaseHistory> histories = purchaseHistoryFileRepository.getAll();
        for (PurchaseHistory history : histories) {
            purchaseHistoryRepository.create(history);
        }
    }

    /**
     * Creates a new purchase history for a customer based on a cart.
     *
     * @param cart The cart whose tickets will be added to the purchase history.
     * @return The created PurchaseHistory object.
     */
    public PurchaseHistory createPurchaseHistory(Cart cart) {
        if (cart == null || cart.getCustomer() == null) {
            throw new IllegalArgumentException("Cart and associated customer cannot be null.");
        }
        if (cart.getTickets().isEmpty()) {
            throw new IllegalArgumentException("Cannot create purchase history from an empty cart.");
        }

        Customer customer = cart.getCustomer();
        PurchaseHistory purchaseHistory = new PurchaseHistory(customer, LocalDate.now());

        // Add tickets to purchase history
        for (Ticket ticket : cart.getTickets()) {
            purchaseHistory.addTicket(ticket);
        }

        // Calculate total amount
        purchaseHistory.calculateTotalAmount();

        // Save to repositories
        purchaseHistoryRepository.create(purchaseHistory);
        purchaseHistoryFileRepository.create(purchaseHistory);

        return purchaseHistory;
    }

    /**
     * Retrieves the purchase history for a specific customer.
     *
     * @param customer The customer whose purchase history is to be retrieved.
     * @return A list of PurchaseHistory objects associated with the customer.
     */
    public List<PurchaseHistory> getPurchaseHistoryForCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        return purchaseHistoryRepository.getAll().stream()
                .filter(history -> history.getCustomer().equals(customer))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all purchase histories from the repository.
     *
     * @return A list of all PurchaseHistory objects.
     */
    public List<PurchaseHistory> getAllPurchaseHistories() {
        return purchaseHistoryRepository.getAll();
    }

    public PurchaseHistory findPurchaseHistoryByID(int id) {
        return purchaseHistoryRepository.read(id);
    }
}