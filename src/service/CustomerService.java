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

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public boolean addFavourite(FavouriteEntity item) {
        currentCustomer.addFavourite(item);
        return true;
    }

    public boolean removeFavourite(FavouriteEntity item) {
        currentCustomer.removeFavourite(item);
        return true;
    }

    public Set<FavouriteEntity> getFavourites() {
        return currentCustomer.getFavourites();
    }
}
