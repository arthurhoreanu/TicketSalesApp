package model;

public class Admin extends User {
    private String role;

    public Admin(int userId, String username, String password, String email) {
        super(userId, username, password, email);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAccessLevel() {
        return "Admin";
    }

    @Override
    public String toString() {
        return "Admin{" + "role='" + role + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }
}
