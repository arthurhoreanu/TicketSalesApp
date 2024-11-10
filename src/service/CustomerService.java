package service;

import model.Customer;
import repository.IRepository;

import java.util.List;

public class CustomerService {
    private final IRepository<Customer> customerRepository;

    public CustomerService(IRepository<Customer> customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Creates a new customer account
    public boolean createCustomer(int userId, String username, String email, String password) {
        Customer customer = new Customer(userId, username, email, password);
        customerRepository.create(customer);
        return true;
    }

    // Updates a customer account
    public boolean updateCustomer(int customerId, String newUsername, String newEmail, String newPassword) {
        Customer customer = findCustomerById(customerId);
        if (customer != null) {
            customer.setUsername(newUsername);
            customer.setEmail(newEmail);
            customer.setPassword(newPassword);
            customerRepository.update(customer);
            return true;
        }
        return false;
    }

    // Deletes a customer account
    public boolean deleteCustomer(int customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer != null) {
            customerRepository.delete(customerId);
            return true;
        }
        return false;
    }

    // Finds a customer by their ID
    private Customer findCustomerById(int customerId) {
        List<Customer> customers = customerRepository.getAll();
        for (Customer customer : customers) {
            if (customer.getID() == customerId) {
                return customer;
            }
        }
        return null;
    }

    // Retrieves all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.getAll();
    }
}
