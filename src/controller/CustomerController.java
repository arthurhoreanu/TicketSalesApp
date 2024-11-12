package controller;

import model.FavouriteEntity;
import service.CustomerService;

import java.util.Set;

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Adds an item to the current customer's favorites
    public void addFavourite(FavouriteEntity item) {
        boolean success = customerService.addFavourite(item);
        if (success) {
            System.out.println("Entity was marked as favourite.");
        } else {
            System.out.println("Entity is already in favourites.");
        }
    }

    // Removes an item from the current customer's favorites
    public void removeFavourite(FavouriteEntity item) {
        boolean success = customerService.removeFavourite(item);
        if (success) {
            System.out.println("Entity was removed from favourites.");
        } else {
            System.out.println("Entity not found in favourites.");
        }
    }

    // Retrieves the current customer's favorites
    public Set<FavouriteEntity> getFavourites() {
        return customerService.getFavourites();
    }
}
