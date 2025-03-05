package main.java.com.ticketsalesapp.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.java.com.ticketsalesapp.model.ticket.Cart;
import main.java.com.ticketsalesapp.model.FavouriteEntity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a customer user with specific preferences, favourites, and a shopping cart.
 */
@Getter
@Entity
@Table(name = "customer")
@NoArgsConstructor
public class Customer extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int userID;

    @Setter
    @Column(name = "username", nullable = false)
    private String username;

    @Setter
    @Column(name = "email", nullable = false)
    private String email;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Setter
    @Transient
    private Set<FavouriteEntity> favourites = new HashSet<>();

    /**
     * -- GETTER --
     *  Gets the customer's shopping cart.
     *
     */
    @Setter
    @Transient
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /**
     * -- GETTER --
     *  Gets the customer's preferred sections with preference counts.
     *
     */
    @Setter
    @Transient
    private Map<Integer, Integer> preferredSections; // Tracks section preferences with section ID as key and preference count as value

    /**
     * Constructs a Customer with the specified user details and initializes default values.
     * @param userId   the unique ID for the customer
     * @param username the username of the customer
     * @param email    the email address of the customer
     * @param password the password for the customer account
     */
    public Customer(int userId, String username, String email, String password) {
        super(userId, username, email, password);
//        this.cart = new Cart();
        this.preferredSections = new HashMap<>();
    }

    public Set<FavouriteEntity> getFavourites() {
        if (favourites == null) {
            favourites = new HashSet<>();
        }
        return favourites;
    }

    /**
     * Returns a string representation of the customer, including role, username, password, favourites, and preferred sections.
     * @return a string representing the customer's details
     */
    @Override
    public String toString() {
        return "Customer{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", favourites=" + favourites +
                ", preferredSections=" + preferredSections +
                '}';
    }

    /**
     * Converts the current object into a CSV-formatted string.
     * The resulting string contains the object's ID, type ("Customer"), username, email, and password,
     * separated by commas.
     * @return A CSV-formatted string representing the current object.
     *         Format: {ID,Customer,username,email,password}
     */
    @Override
    public String toCsv() {
        return getID() + "," + getUsername() + "," + getEmail() + "," + getPassword();
    }

    /**
     * Parses a CSV-formatted string and creates a Customer object.
     * The string is expected to contain fields for the customer's ID, username, email, and password.
     * Format: {ID,username,email,password}.
     * @param csvLine The CSV-formatted string representing a customer.
     * @return A Customer object parsed from the input string.
     */
    public static Customer fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0].trim());
        String username = fields[1].trim();
        String email = fields[2].trim();
        String password = fields[3].trim();
        Customer customer = new Customer();
        customer.setID(id);
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(password);
        return customer;
    }

    /**
     * Adds an item to the customer's list of favourite entities.
     *
     * @param item the favourite entity to add
     */
    public void addFavourite(FavouriteEntity item) {
        favourites.add(item);
    }

    /**
     * Removes an item from the customer's list of favourite entities.
     *
     * @param item the favourite entity to remove
     */
    public void removeFavourite(FavouriteEntity item) {
        favourites.remove(item);
    }
}