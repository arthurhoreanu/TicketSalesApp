package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

/**
 * Represents a customer user with specific preferences, favourites, and a shopping cart.
 */
public class Customer extends User {
    private Set<FavouriteEntity> favourites;
    private ShoppingCart shoppingCart;
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
        this.favourites = new HashSet<>();
        this.shoppingCart = new ShoppingCart(userId, new ArrayList<>(), 0.0); // Initialize with customer's ID
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
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Gets the customer's preferred sections with preference counts.
     * @return a map where the keys are section IDs and values are preference counts
     */
    public Map<Integer, Integer> getPreferredSections() {
        return preferredSections;
    }

    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, getID());
        stmt.setString(2, "Customer");
        stmt.setString(3, getUsername());
        stmt.setString(4, getEmail());
        stmt.setString(5, getPassword());
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
        return getID() + "," + "Customer," + getUsername() + "," + getEmail() + "," + getPassword();
    }
}
