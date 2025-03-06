package main.java.com.ticketsalesapp.controller.user;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.FavouriteEntity;
import main.java.com.ticketsalesapp.service.user.CustomerService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void login(String username, String password) {
        if (customerService.login(username, password)) {
            System.out.println("✅ Customer logged in successfully.");
        } else {
            System.out.println("❌ Invalid credentials.");
        }
    }

    public void logout() {
        customerService.logout();
        System.out.println("✅ Customer logged out successfully.");
    }

    public void createCustomer(String username, String email, String password) {
        try {
            customerService.createCustomer(username, email, password);
            System.out.println("✅ Customer created successfully.");
        } catch (ValidationException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public void getCurrentCustomer() {
        Customer customer = customerService.getCurrentCustomer();
        if (customer != null) {
            System.out.println("👤 Logged in customer: " + customer);
        } else {
            System.out.println("❌ No customer is currently logged in.");
        }
    }

    public void findCustomerById(int id) {
        try {
            Customer customer = customerService.findCustomerById(id);
            System.out.println("🔍 Found customer: " + customer);
        } catch (BusinessLogicException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public void addFavourite(FavouriteEntity item) {
        try {
            customerService.addFavourite(item);
            System.out.println("✅ Added to favourites: " + item);
        } catch (ValidationException | BusinessLogicException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public void removeFavourite(FavouriteEntity item) {
        try {
            customerService.removeFavourite(item);
            System.out.println("✅ Removed from favourites: " + item);
        } catch (ValidationException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public void getFavourites() {
        Set<FavouriteEntity> favourites = customerService.getFavourites();
        if (favourites.isEmpty()) {
            System.out.println("❌ No favourites found.");
        } else {
            System.out.println("⭐ Favourite items: " + favourites);
        }
    }
}
