package controller;

import model.FavouriteEntity;
import service.CustomerService;

import java.util.Set;

public class CustomerController {
    private final CustomerService customerService;

    /**
     * Constructor for CustomerController.
     * @param customerService The instance of CustomerService used for customer-related operations.
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Adds an entity to the current customer's favorites.
     * Displays a success message if the entity is added to favorites, or a message indicating it is already a favorite.
     * @param item The entity to be marked as favorite.
     */
    public void addFavourite(FavouriteEntity item) {
        boolean success = customerService.addFavourite(item);
        if (success) {
            System.out.println("Entity was marked as favourite.");
        } else {
            System.out.println("Entity is already in favourites.");
        }
    }

    /**
     * Removes an entity from the current customer's favorites.
     * Displays a success message if the entity is removed from favorites, or a message indicating it was not found in the favorites.
     * @param item The entity to be removed from favorites.
     */
    public void removeFavourite(FavouriteEntity item) {
        boolean success = customerService.removeFavourite(item);
        if (success) {
            System.out.println("Entity was removed from favourites.");
        } else {
            System.out.println("Entity not found in favourites.");
        }
    }

    /**
     * Retrieves the current customer's favorite entities.
     * @return A set of entities that are marked as favorites by the current customer.
     */
    public Set<FavouriteEntity> getFavourites() {
        return customerService.getFavourites();
    }

}
