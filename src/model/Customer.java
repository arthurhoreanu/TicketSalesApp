package model;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;


public class Customer extends User {
    private String role;
    private Set<FavouriteItem> favourites;
    private ShoppingCart shoppingCart;

    public Customer(int userId, String username, String email, String password) {
        super(userId, username, email, password);
        this.role = "Customer"; // default role
        this.favourites = new HashSet<>();
        this.shoppingCart = new ShoppingCart(userId, new ArrayList<>(), 0.0); // Initialize with customer's ID
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

    public boolean addFavorite(FavouriteItem item) {
        return favourites.add(item);
    }

    public boolean removeFavorite(FavouriteItem item) {
        return favourites.remove(item);
    }

    public Set<FavouriteItem> getFavorites() {
        return favourites;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    @Override
    public String toString() {
        return "Customer{" + "role='" + role + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }
}
