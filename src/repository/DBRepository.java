package repository;

import model.Identifiable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Generic repository for managing database persistence.
 * Supports CRUD operations for any Identifiable entity.
 *
 * @param <T> Type of objects managed by the repository.
 */
public class DBRepository<T extends Identifiable> implements IRepository<T> {
    private final Connection connection;
    private final String tableName;
    private final Function<ResultSet, T> fromDatabase;

    /**
     * Constructs a new DBRepository with the specified parameters.
     *
     * @param connection    The database connection to use.
     * @param tableName     The name of the table in the database.
     * @param fromDatabase  A function to convert a ResultSet to an object of type T.
     */
    public DBRepository(Connection connection, String tableName, Function<ResultSet, T> fromDatabase) {
        this.connection = connection;
        this.tableName = tableName;
        this.fromDatabase = fromDatabase;
    }

    @Override
    public void create(T obj) {
        String sql = "INSERT INTO " + tableName + " VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            obj.toDatabase(stmt); // Call toDatabase from entity
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting object into table " + tableName, e);
        }
    }

    @Override
    public T read(Integer id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return fromDatabase.apply(rs); // Use fromDatabase function
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading object from table " + tableName, e);
        }
        return null;
    }

    @Override
    public void update(T obj) {
        String sql = "UPDATE " + tableName + " SET name = ?, genre = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            obj.toDatabase(stmt); // Populate PreparedStatement with data
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating object in table " + tableName, e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting object from table " + tableName, e);
        }
    }

    @Override
    public List<T> getAll() {
        String sql = "SELECT * FROM " + tableName;
        List<T> items = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(fromDatabase.apply(rs)); // Use fromDatabase function
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all objects from table " + tableName, e);
        }
        return items;
    }
}