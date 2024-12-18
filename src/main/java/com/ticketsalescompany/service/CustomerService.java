package com.ticketsalescompany.service;

import com.ticketsalescompany.model.Customer;
import com.ticketsalescompany.model.FavouriteEntity;

import java.util.Set;

public class CustomerService {
    private Customer currentCustomer;

    public CustomerService() {}

    /**
     * Sets the current customer for the service.
     * @param customer The customer to be set as the current customer.
     */
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    /**
     * Retrieves the current customer.
     * @return The current customer, or null if no customer is set.
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * Adds an item to the current customer's list of favourites.
     * @param item The favourite entity to be added.
     * @return true if the item was successfully added to the favourites.
     */
    public boolean addFavourite(FavouriteEntity item) {
        currentCustomer.addFavourite(item);
        return true;
    }

    /**
     * Removes an item from the current customer's list of favourites.
     * @param item The favourite entity to be removed.
     * @return true if the item was successfully removed from the favourites.
     */
    public boolean removeFavourite(FavouriteEntity item) {
        currentCustomer.removeFavourite(item);
        return true;
    }

    /**
     * Retrieves the current customer's list of favourite items.
     * @return A set of favourite items belonging to the current customer.
     */
    public Set<FavouriteEntity> getFavourites() {
        return currentCustomer.getFavourites();
    }
}
