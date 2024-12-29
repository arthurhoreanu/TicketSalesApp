package controller;

import model.Cart;
import model.Customer;
import model.PurchaseHistory;
import service.PurchaseHistoryService;

import java.util.List;

public class PurchaseHistoryController {
    private final PurchaseHistoryService purchaseHistoryService;

    public PurchaseHistoryController(PurchaseHistoryService purchaseHistoryService) {
        this.purchaseHistoryService = purchaseHistoryService;
    }

    public void createPurchaseHistory(Cart cart) {
        purchaseHistoryService.createPurchaseHistory(cart);
        System.out.println("Your purchases were added to your history.");
    }

    public List<PurchaseHistory> getPurchaseHistoryForCustomer(Customer customer) {
        List<PurchaseHistory> history = purchaseHistoryService.getPurchaseHistoryForCustomer(customer);
        if (history.isEmpty()) {
            System.out.println("No previous purchases found.");
        } else {
            System.out.println("Your previous purchases:");
            history.forEach(System.out::println);
        }
        return history;
    }

    public List<PurchaseHistory> getAllPurchaseHistories() {
        List<PurchaseHistory> histories = purchaseHistoryService.getAllPurchaseHistories();
        if (histories.isEmpty()) {
            System.out.println("No purchase histories found.");
        } else {
            histories.forEach(System.out::println);
        }
        return histories;
    }

    public PurchaseHistory findPurchaseHistoryByID(int id) {
        PurchaseHistory history = purchaseHistoryService.findPurchaseHistoryByID(id);
        if (history == null) {
            System.out.println("Purchase history with ID " + id + " not found.");
        } else {
            System.out.println("Found purchase history: " + history);
        }
        return history;
    }
}
