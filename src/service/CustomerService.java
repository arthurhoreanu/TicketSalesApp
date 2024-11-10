package service;

import model.Customer;
import model.FavouriteItem;

import java.util.Set;

public class CustomerService {
    private Customer currentCustomer;

    public CustomerService() {}

    public CustomerService(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    public boolean addFavorite(FavouriteItem item) {
        currentCustomer.addFavorite(item);
        return true;
    }

    public boolean removeFavorite(FavouriteItem item) {
        currentCustomer.removeFavorite(item);
        return true;
    }

    public Set<FavouriteItem> getFavorites() {
        return currentCustomer.getFavorites();
    }
}
