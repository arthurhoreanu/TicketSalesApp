package main.java.com.ticketsalesapp.controller;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.FavouriteEntity;
import main.java.com.ticketsalesapp.service.CustomerService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void login(String username, String password) {
        boolean success = customerService.login(username, password);
        if (success) {
            System.out.println("Customer logged in successfully");
        } else {
            System.out.println("Invalid credentials");
        }
    }

    public void logout() {
        boolean success = customerService.logout();
        if (success) {
            System.out.println("Customer logged out successfully");
        } else {
            System.out.println("No customer currently logged in");
        }
    }

    public boolean createCustomer(String username, String email, String password) {
        try {
            return customerService.createCustomer(username, email, password);
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public Customer getCurrentCustomer() {
        return customerService.getCurrentCustomer();
    }

    public Customer findCustomerById(int id) {
        try {
            return customerService.findCustomerById(id);
        } catch (BusinessLogicException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void addFavourite(FavouriteEntity item) {
        customerService.addFavourite(item);
    }

    public void removeFavourite(FavouriteEntity item) {
        customerService.removeFavourite(item);
    }

    public Set<FavouriteEntity> getFavourites() {
        return customerService.getFavourites();
    }
}