package main.java.com.ticketsalesapp.service.user;

import lombok.RequiredArgsConstructor;
import main.java.com.ticketsalesapp.exception.BusinessLogicException;
import main.java.com.ticketsalesapp.exception.ValidationException;
import main.java.com.ticketsalesapp.model.user.Customer;
import main.java.com.ticketsalesapp.model.FavouriteEntity;
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

    public boolean login(String username, String password) {
        var customer = customerRepository.getAll().stream()
                .filter(c -> c.getUsername().equals(username) && c.getPassword().equals(password))
                .findFirst();

        if (customer.isPresent()) {
            userSession.setCurrentUser(customer.get());
            return true;
        }
        return false;
    }

    public void logout() {
        userSession.logout();
    }

    public void createCustomer(String username, String email, String password) {
        if (customerRepository.getAll().stream().anyMatch(a -> a.getUsername().equals(username))) {
            throw new ValidationException("Username already taken");
        }
        Customer customer = new Customer(generateNewId(), username, email, password);
        customerRepository.create(customer);
    }

    public Customer findCustomerById(int id) {
        return customerRepository.read(id)
                .orElseThrow(() -> new BusinessLogicException("Customer not found"));
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
                .mapToInt(Customer::getID)
                .max()
                .orElse(0) + 1;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getAll();
    }

    public Customer findByUsernameAndPassword(String username, String password)
        throws BusinessLogicException {
        return customerRepository.getAll().stream()
            .filter(c -> c.getUsername().equals(username) && c.getPassword().equals(password))
            .findFirst()
            .orElseThrow(() -> new BusinessLogicException("Customer not found"));
        }
}
