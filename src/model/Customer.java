package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

public class Customer extends User {
    private String role;
    private Set<FavouriteEntity> favourites;
    private ShoppingCart shoppingCart;
    private Map<Integer, Integer> preferredSections; // Tracks section preferences with section ID as key and preference count as value

    public Customer(int userId, String username, String email, String password) {
        super(userId, username, email, password);
        this.role = "Customer"; // default role
        this.favourites = new HashSet<>();
        this.shoppingCart = new ShoppingCart(userId, new ArrayList<>(), 0.0); // Initialize with customer's ID
        this.preferredSections = new HashMap<>();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAccessLevel() {
        return "Customer";
    }

    public boolean addFavorite(FavouriteEntity item) {
        return favourites.add(item);
    }

    public boolean removeFavorite(FavouriteEntity item) {
        return favourites.remove(item);
    }

    public Set<FavouriteEntity> getFavourites() {
        return favourites;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public Map<Integer, Integer> getPreferredSections() {
        return preferredSections;
    }
    //TODO IMPORTANT TO TEST THIS
    /**
     * Adds a preference for a section based on the section ID.
     * Each time the customer reserves a seat in a section, the preference count for that section is increased.
     */
    public void addSeatPreference(Section section) {
        int sectionId = section.getID();
        preferredSections.put(sectionId, preferredSections.getOrDefault(sectionId, 0) + 1);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", favourites=" + favourites +
                ", preferredSections=" + preferredSections +
                '}';
    }
}
