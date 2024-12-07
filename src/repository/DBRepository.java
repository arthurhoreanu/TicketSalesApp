package repository;

import model.Identifiable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRepository<T extends Identifiable> implements IRepository<T> {
    private final Connection connection;
    private final String tableName;

    public DBRepository(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    @Override
    public void create(T obj) {
        String sql = "INSERT INTO " + tableName + " VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            obj.toPreparedStatement(stmt);
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
                return T.fromResultSet(rs);
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
            obj.toPreparedStatement(stmt);
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
                items.add(T.fromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all objects from table " + tableName, e);
        }
        return items;
    }
}
