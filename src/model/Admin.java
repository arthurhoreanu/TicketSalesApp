package model;

/**
 * Represents an admin user with specific access privileges.
 */
public class Admin extends User {
    private String role;

    /**
     * Constructs an Admin with the specified user details.
     * @param userID   the unique ID for the admin user
     * @param username the username of the admin
     * @param email    the email address of the admin
     * @param password the password for the admin account
     */
    public Admin(int userID, String username, String email, String password) {
        super(userID, username, email, password);
        this.role = role;
    }

    /**
     * Retrieves the access level of the user, specific to admins.
     * @return a string representing the access level, which is "Admin" for this class
     */
    @Override
    public String getAccessLevel() {
        return "Admin";
    }

    /**
     * Returns a string representation of the admin, including role, username, and password.
     * @return a string representing the admin's details
     */
    @Override
    public String toString() {
        return "Admin{" + "role='" + role + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }
}
