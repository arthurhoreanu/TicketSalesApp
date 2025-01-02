package model;

import javax.persistence.*;

/**
 * Represents an admin user with specific access privileges.
 */
@Entity
@Table(name = "admin")
public class Admin extends User {

    @Id
    @Column(name = "user_id", nullable = false)
    private int userID;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    public Admin() {}

    /**
     * Constructs an Admin with the specified user details.
     * @param userID   the unique ID for the admin user
     * @param username the username of the admin
     * @param email    the email address of the admin
     * @param password the password for the admin account
     */
    public Admin(int userID, String username, String email, String password) {
        super(userID, username, email, password);
    }

    // Representations

    /**
     * Returns a string representation of the admin, including role, username, and password.
     * @return a string representing the admin's details
     */
    @Override
    public String toString() {
        return "Admin{" + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }

    /**
     * Converts the current object into a CSV-formatted string.
     * The resulting string contains the object's ID, type ("Admin"), username, email, and password,
     * separated by commas.
     * @return A CSV-formatted string representing the current object.
     *         Format: {ID,Admin,username,email,password}
     */
    @Override
    public String toCsv() {
        return getID() + "," + getUsername() + "," + getEmail() + "," + getPassword();
    }

    /**
     * Parses a CSV-formatted string and creates an Admin object.
     * The string is expected to contain fields for the admin's ID, username, email, and password.
     * Format: {ID,username,email,password}.
     * @param csvLine The CSV-formatted string representing an admin.
     * @return An Admin object parsed from the input string.
     */
    public static Admin fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0].trim());
        String username = fields[1].trim();
        String email = fields[2].trim();
        String password = fields[3].trim();
        return new Admin(id, username, email, password);
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
}
