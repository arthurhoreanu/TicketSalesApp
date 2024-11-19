package model;

/**
 * Represents an admin user with specific access privileges.
 */
public class Admin extends User {

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

    /**
     * Returns a string representation of the admin, including role, username, and password.
     * @return a string representing the admin's details
     */
    @Override
    public String toString() {
        return "Admin{" + "role='" + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }

    /**
     * Converts the current object into a CSV-formatted string.
     * The resulting string contains the object's ID, type ("Admin"), username, email, and password,
     * separated by commas.
     * @return A CSV-formatted string representing the current object.
     *         Format: {ID,Admin,username,email,password}
     */
    @Override
    public String toCsvFormat() {
        return getID() + "," + "Admin," + getUsername() + "," + getEmail() + "," + getPassword();
    }
}
