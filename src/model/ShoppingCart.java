package model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements Identifiable {
    private int shoppingCartID;
    private List<Ticket> items = new ArrayList<>();
    private double totalPrice;

    public ShoppingCart(int shoppingCartID,List<Ticket> items, double totalPrice) {
        this.shoppingCartID = shoppingCartID;
        this.items = items;
        this.totalPrice = totalPrice;
    }
    @Override
    public Integer getID() {
        return shoppingCartID;
    }
    public int getShoppingCartID() {
        return shoppingCartID;
    }

    public List<Ticket> getItems() {
        return items;
    }

    public void setItems(List<Ticket> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }


    //TODO check it again in service

    @Override
    public String toString() {
        return "Shopping Cart [shoppingCartID=" + shoppingCartID + ", items=" + items + ", totalPrice=" + totalPrice + "]";
    }
}


