package model;

/**
 * Represents a general user in the system, containing basic information such as ID, username, email, and password.
 * This class serves as a base for more specific user types (e.g., Admin, Customer).
 */
public abstract class User implements Identifiable {
    private int userID;
    protected String username;
    private String email;
    protected String password;

    /**
     * Constructs a User with the specified ID, username, email, and password.
     * @param userID   the unique ID of the user
     * @param username the username of the user
     * @param email    the email address of the user
     * @param password the password for the user's account
     */
    public User(int userID, String username, String email, String password) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the unique ID of the user.
     * @return the ID of the user
     */
    @Override
    public Integer getID() {
        return this.userID;
    }

    /**
     * Gets the username of the user.
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the email address of the user.
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns a string representation of the user, displaying the user's ID, username, email, and password.
     * @return a string containing the user details
     */
    @Override
    public String toString() {
        return "User [userId=" + userID + ", username=" + username + ", password=" + password + ", email=" + email + "}";
    }

    public static User fromCsvFormat(String csvLine) {
        String[] fields = csvLine.split(",");
        String type = fields[0].trim();
        int id = Integer.parseInt(fields[1].trim());
        String username = fields[2].trim();
        String email = fields[3].trim();
        String password = fields[4].trim();
        switch (type) {
            case "Admin":
                return new Admin(id, username, email, password);
            case "Customer":
                return new Customer(id, username, email, password);
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }

}
