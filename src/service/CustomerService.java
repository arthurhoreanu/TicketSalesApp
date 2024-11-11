package service;

import model.Customer;
import model.FavouriteEntity;

import java.util.Set;

public class CustomerService {
    private Customer currentCustomer;

    public CustomerService() {}

    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    public boolean addFavorite(FavouriteEntity item) {
        currentCustomer.addFavorite(item);
        return true;
    }

    public boolean removeFavorite(FavouriteEntity item) {
        currentCustomer.removeFavorite(item);
        return true;
    }

    public Set<FavouriteEntity> getFavourites() {
        return currentCustomer.getFavourites();
    }
}
