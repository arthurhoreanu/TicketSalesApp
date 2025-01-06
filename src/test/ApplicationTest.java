package test;

import model.Admin;
import model.Customer;
import model.User;
import org.junit.jupiter.api.*;
import repository.InMemoryRepository;
import repository.IRepository;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {

    private static IRepository<User> userRepository;

    @BeforeAll
    public static void setUp() {
        userRepository = new InMemoryRepository<>();
    }

    @Order(1)
    @DisplayName("Simulate Full CRUD Operations for Admins and Customers")
    @Test
    public void UsersCRUD() {
        // 1. CREATE
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setEmail("admin@tsc.com");
        admin.setPassword("password");

        Customer customer = new Customer();
        customer.setUsername("customer");
        customer.setEmail("customer@gmail.com");
        customer.setPassword("123456");

        userRepository.create(admin);
        userRepository.create(customer);

        assertEquals(2, userRepository.getAll().size(), "Two users should be created.");

        // 2. READ
        User retrievedAdmin = userRepository.read(1);
        User retrievedCustomer = userRepository.read(2);

        assertNotNull(retrievedAdmin, "Admin should be retrievable.");
        assertNotNull(retrievedCustomer, "Customer should be retrievable.");
        assertEquals("admin", retrievedAdmin.getUsername(), "Retrieved Admin username should match.");
        assertEquals("customer", retrievedCustomer.getUsername(), "Retrieved Customer username should match.");

        // 3. UPDATE
        admin.setEmail("newadmin@tsc.com");
        userRepository.update(admin);

        User updatedAdmin = userRepository.read(1);
        assertNotNull(updatedAdmin, "Updated Admin should still exist.");
        assertEquals("newadmin@tsc.com", updatedAdmin.getEmail(), "Admin email should be updated.");

        // 4. DELETE
        userRepository.delete(1); // Delete Admin
        userRepository.delete(2); // Delete Customer

        assertNull(userRepository.read(1), "Admin should be deleted.");
        assertNull(userRepository.read(2), "Customer should be deleted.");
        assertEquals(0, userRepository.getAll().size(), "No users should remain after deletion.");
    }
}
