package main.java.com.ticketsalesapp.controller.user;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.user.FavouriteEntity;
import main.java.com.ticketsalesapp.service.user.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void createCustomer(String username, String email, String password) {
        customerService.createCustomer(username, email, password);
    }

    public boolean usernameExists(String username) {
        return customerService.usernameExists(username);
    }

    public Customer login(String username, String password) throws BusinessLogicException {
        return customerService.login(username, password);
    }

    public void logout() throws BusinessLogicException {
        customerService.logout();
    }

    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public Customer findCustomerById(int id) {
        return customerService.findCustomerById(id);
    }

    public void deleteCustomer(int id) {
        customerService.deleteCustomer(id);
    }

    public void getCurrentCustomer() {
        Customer customer = customerService.getCurrentCustomer();
        if (customer != null) {
            System.out.println("👤 Logged in customer: " + customer);
        } else {
            System.out.println("❌ No customer is currently logged in.");
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

    public void diplayAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("There are no customers in the database.");
        }
        customers.forEach(System.out::println);
    }
}
