package main.java.com.ticketsalesapp.service.user;

import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.user.FavouriteEntity;
import main.java.com.ticketsalesapp.repository.Repository;
import main.java.com.ticketsalesapp.repository.factory.RepositoryFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CustomerService {

    private final Repository<Customer> customerRepository;
    private final UserSession userSession;

    public CustomerService(RepositoryFactory repositoryFactory, UserSession userSession) {
        this.customerRepository = repositoryFactory.createCustomerRepository();
        this.userSession = userSession;
    }

    public void createCustomer(String username, String email, String password) {
        validateInput(username, "Username cannot be empty.");
        validateInput(email, "Email cannot be empty.");
        validateInput(password, "Password cannot be empty.");
        if (usernameExists(username)) {
            throw new ValidationException("Username already taken.");
        }
        Customer customer = new Customer(generateNewId(), username, email, password);
        customerRepository.create(customer);
    }

    public boolean usernameExists(String username) {
        return customerRepository.getAll().stream().anyMatch(c -> c.getUsername().equals(username));
    }

    public Customer login(String username, String password) throws BusinessLogicException {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessLogicException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessLogicException("Password cannot be empty");
        }

        Customer customer = customerRepository.getAll().stream()
                .filter(a -> a.getUsername().equals(username) && a.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Invalid credentials"));

        userSession.setCurrentUser(customer);
        return customer;
    }

    public void logout() throws BusinessLogicException {
        if (userSession.getCurrentUser().isEmpty()) {
            throw new BusinessLogicException("No user is currently logged in");
        }
        userSession.logout();
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getAll();
    }

    public Customer findCustomerById(int id) {
        return customerRepository.read(id)
                .orElseThrow(() -> new BusinessLogicException("Customer not found"));
    }

    public void deleteCustomer(int id) {
        findCustomerById(id);
        customerRepository.delete(id);
    }

    public void addFavourite(FavouriteEntity item) {
        if (item == null) {
            throw new ValidationException("Cannot add a null item to favourites.");
        }
        Customer customer = getCurrentCustomer();
        if (customer.getFavourites().contains(item)) {
            throw new BusinessLogicException("Item is already in the favourites.");
        }
        customer.addFavourite(item);
    }

    public void removeFavourite(FavouriteEntity item) {
        if (item == null) {
            throw new ValidationException("Cannot remove a null item from favourites.");
        }
        Customer customer = getCurrentCustomer();
        if (!customer.getFavourites().contains(item)) {
            throw new ValidationException("Item is not in the favourites.");
        }
        customer.removeFavourite(item);
    }

    public Set<FavouriteEntity> getFavourites() {
        return getCurrentCustomer().getFavourites();
    }

    public Customer getCurrentCustomer() {
        return userSession.getCurrentUser()
                .filter(user -> user instanceof Customer)
                .map(user -> (Customer) user)
                .orElseThrow(() -> new BusinessLogicException("No customer is logged in."));
    }

    private int generateNewId() {
        return customerRepository.getAll().stream()
                .mapToInt(Customer::getId)
                .max()
                .orElse(0) + 1;
    }

    public Customer findByUsernameAndPassword(String username, String password)
        throws BusinessLogicException {
        return customerRepository.getAll().stream()
            .filter(c -> c.getUsername().equals(username) && c.getPassword().equals(password))
            .findFirst()
            .orElseThrow(() -> new BusinessLogicException("Customer not found"));
        }

    private void validateInput(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(errorMessage);
        }
    }
}
