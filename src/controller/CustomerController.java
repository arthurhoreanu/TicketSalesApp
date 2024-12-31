package controller;

import model.Customer;
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
     * Sets the current customer in the service.
     * @param customer The customer to be set as the current customer.
     */
    public void setCurrentCustomer(Customer customer) {
        customerService.setCurrentCustomer(customer);
        System.out.println("Current customer set to: " + customer.getUsername());
    }

    /**
     * Retrieves the current customer.
     * Displays the username of the current customer or a message if no customer is set.
     * @return The current customer, or null if no customer is set.
     */
    public Customer getCurrentCustomer() {
        return customerService.getCurrentCustomer();
    }

    /**
     * Adds an entity to the current customer's favorites.
     * Displays a success message if the entity is added to favorites, or a message indicating it is already a favorite.
     * @param item The entity to be marked as favorite.
     * @return true if the entity was successfully added to favorites, false otherwise.
     */
    public boolean addFavourite(FavouriteEntity item) {
        boolean success = customerService.addFavourite(item);
        if (success) {
            System.out.println("Entity was marked as favourite.");
        } else {
            System.out.println("Entity is already in favourites.");
        }
        return success;
    }

    /**
     * Removes an entity from the current customer's favorites.
     * Displays a success message if the entity is removed from favorites, or a message indicating it was not found in the favorites.
     * @param item The entity to be removed from favorites.
     * @return true if the entity was successfully removed from favorites, false otherwise.
     */
    public boolean removeFavourite(FavouriteEntity item) {
        boolean success = customerService.removeFavourite(item);
        if (success) {
            System.out.println("Entity was removed from favourites.");
        } else {
            System.out.println("Entity not found in favourites.");
        }
        return success;
    }

    /**
     * Retrieves the current customer's favorite entities.
     * Displays the list of favorites or a message if no favorites are found.
     * @return A set of entities that are marked as favorites by the current customer.
     */
    public Set<FavouriteEntity> getFavourites() {
        return customerService.getFavourites();
    }
}
