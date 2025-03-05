package main.java.com.ticketsalesapp.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.FavouriteEntity;
import main.java.com.ticketsalesapp.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final BaseRepository<Customer> customerRepository;

    @Getter
    private Customer currentCustomer;

    public boolean login(String username, String password) {
        var customer = customerRepository.getAll().stream()
                .filter(c -> c.getUsername().equals(username) && c.getPassword().equals(password))
                .findFirst();

        if (customer.isPresent()) {
            currentCustomer = customer.get();
            return true;
        }
        return false;
    }

    public boolean logout() {
        if (currentCustomer == null) {
            return false;
        }
        currentCustomer = null;
        return true;
    }

    public boolean createCustomer(String username, String email, String password) {
        if (takenUsername(username)) {
            throw new ValidationException("Username already taken");
        }
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(password);
        customerRepository.create(customer);
        return true;
    }

    private boolean takenUsername(String username) {
        return customerRepository.getAll().stream()
                .anyMatch(customer -> customer.getUsername().equals(username));
    }

    public Customer findCustomerById(int id) {
        return customerRepository.read(id)
                .orElseThrow(() -> new BusinessLogicException("Customer not found"));
    }

    public boolean addFavourite(FavouriteEntity item) {
        if (item == null) {
            throw new ValidationException("Cannot add a null item to favourites.");
        }
        if (currentCustomer.getFavourites().contains(item)) {
            throw new BusinessLogicException("Item is already in the favourites.");
        }
        currentCustomer.addFavourite(item);
        customerRepository.create(currentCustomer);
        return true;
    }

    public boolean removeFavourite(FavouriteEntity item) {
        if (item == null) {
            throw new ValidationException("Cannot remove a null item from favourites.");
        }
        if (!currentCustomer.getFavourites().contains(item)) {
            throw new ValidationException("Item is not in the favourites.");
        }
        currentCustomer.removeFavourite(item);
        customerRepository.create(currentCustomer);
        return true;
    }

    public Set<FavouriteEntity> getFavourites() {
        return currentCustomer.getFavourites();
    }
}