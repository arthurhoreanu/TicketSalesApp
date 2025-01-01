package model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a customer user with specific preferences, favourites, and a shopping cart.
 */
@Entity
@Table(name = "customer")
public class Customer extends User {

    @Column(name = "user_id", nullable = false)
    private int userID;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private Set<FavouriteEntity> favourites;

    @Transient
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", nullable = true)
    private Cart cart;

    @Transient
    private Map<Integer, Integer> preferredSections; // Tracks section preferences with section ID as key and preference count as value

    protected Customer() {}

    /**
     * Constructs a Customer with the specified user details and initializes default values.
     * @param userId   the unique ID for the customer
     * @param username the username of the customer
     * @param email    the email address of the customer
     * @param password the password for the customer account
     */
    public Customer(int userId, String username, String email, String password) {
        super(userId, username, email, password);
        this.favourites = new HashSet<>();
//        this.cart = new Cart(); TODO dacă-l las, crapă
        this.preferredSections = new HashMap<>();
    }

    /**
     * Adds an item to the customer's list of favourite entities.
     * @param item the favourite entity to add
     * @return true if the item was added to favourites, false if it was already in the list
     */
    public boolean addFavourite(FavouriteEntity item) {
        return favourites.add(item);
    }

    /**
     * Removes an item from the customer's list of favourite entities.
     * @param item the favourite entity to remove
     * @return true if the item was removed from favourites, false if it was not found
     */
    public boolean removeFavourite(FavouriteEntity item) {
        return favourites.remove(item);
    }

    /**
     * Gets the set of the customer's favourite entities.
     * @return a set of favourite entities
     */
    public Set<FavouriteEntity> getFavourites() {
        return favourites;
    }

    /**
     * Gets the customer's shopping cart.
     * @return the customer's shopping cart
     */
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    /**
     * Gets the customer's preferred sections with preference counts.
     * @return a map where the keys are section IDs and values are preference counts
     */
    public Map<Integer, Integer> getPreferredSections() {
        return preferredSections;
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
        return new Customer(id, username, email, password);
    }

    // Getters and setters

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFavourites(Set<FavouriteEntity> favourites) {
        this.favourites = favourites;
    }

    public void setPreferredSections(Map<Integer, Integer> preferredSections) {
        this.preferredSections = preferredSections;
    }
}