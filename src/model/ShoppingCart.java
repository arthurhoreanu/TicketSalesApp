package model;

public class ShoppingCart implements Identifiable {
    private int shoppingCartID;
    private double totalPrice;

    /**
     * Constructs a ShoppingCart with the specified attributes.
     *
     * @param shoppingCartID the unique ID of the shopping cart
     * @param totalPrice     the total price of tickets in the cart
     */
    public ShoppingCart(int shoppingCartID, double totalPrice) {
        this.shoppingCartID = shoppingCartID;
        this.totalPrice = totalPrice;
    }

    @Override
    public Integer getID() {
        return shoppingCartID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "shoppingCartID=" + shoppingCartID +
                ", totalPrice=" + totalPrice +
                '}';
    }

    /**
     * Converts the ShoppingCart to a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the shopping cart
     */
    public String toCSV() {
        return shoppingCartID + "," + totalPrice;
    }

    /**
     * Creates a ShoppingCart object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string
     * @return the created ShoppingCart object
     */
    public static ShoppingCart fromCSV(String csvLine) {
        String[] fields = csvLine.split(",");
        int shoppingCartID = Integer.parseInt(fields[0].trim());
        double totalPrice = Double.parseDouble(fields[1].trim());
        return new ShoppingCart(shoppingCartID, totalPrice);
    }
}
