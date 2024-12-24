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
        System.out.println("Your purchased were added to your history.");
    }

    public void getPurchaseHistoryForCustomer(Customer customer) {
        System.out.println("Your previous purchases:");
        purchaseHistoryService.getPurchaseHistoryForCustomer(customer);
    }

    public List<PurchaseHistory> getAllPurchaseHistories() {
        return purchaseHistoryService.getAllPurchaseHistories();
    }
}
