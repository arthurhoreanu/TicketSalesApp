package model;

public class Customer extends User {
    private String role;

    public Customer(int userId, String username, String email, String password) {
        super(userId, username, email, password);
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
        return "Customer";
    }

    @Override
    public String toString() {
        return "Customer{" + "role='" + role + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }
}
