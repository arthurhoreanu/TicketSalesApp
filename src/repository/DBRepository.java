package repository;

import model.Identifiable;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRepository<T extends Identifiable> implements IRepository<T> {

    private final Class<T> type;
    private final String tableName;

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/ticketsalescompany";
    private static final String DB_USER = "map";
    private static final String DB_PASSWORD = "map";

    public DBRepository(Class<T> type) {
        this.type = type;
        this.tableName = getTableNameFromHibernate(type);
    }

    private String getTableNameFromHibernate(Class<T> entityClass) {
        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Class " + entityClass.getSimpleName() + " is not annotated with @Entity");
        }

        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (!table.name().isEmpty()) {
                return table.name();
            }
        }

        // Dacă nu există @Table sau numele nu este definit, folosește numele clasei
        return entityClass.getSimpleName().toLowerCase();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public void create(T obj) {
        try (Connection conn = getConnection()) {
            Field[] fields = type.getDeclaredFields();
            String columns = String.join(", ", getFieldNames(fields));
            String placeholders = String.join(", ", getPlaceholders(fields));

            String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                setStatementParameters(stmt, fields, obj);
                stmt.executeUpdate();

                // Setează ID-ul generat automat în obiect
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        obj.setID(rs.getInt(1));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public T read(Integer id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM " + tableName;
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToEntity(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public void update(T obj) {
        try (Connection conn = getConnection()) {
            Field[] fields = type.getDeclaredFields();
            String setClause = String.join(", ", getSetClauses(fields));

            String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                setStatementParameters(stmt, fields, obj);
                stmt.setInt(fields.length + 1, obj.getID());
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper Methods
    private List<String> getFieldNames(Field[] fields) {
        List<String> names = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase("id")) { // Exclude "id" field
                names.add(field.getName());
            }
        }
        return names;
    }

    private List<String> getPlaceholders(Field[] fields) {
        List<String> placeholders = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase("id")) { // Exclude "id" field
                placeholders.add("?");
            }
        }
        return placeholders;
    }

    private List<String> getSetClauses(Field[] fields) {
        List<String> clauses = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase("id")) { // Exclude "id" field
                clauses.add(field.getName() + " = ?");
            }
        }
        return clauses;
    }

    private void setStatementParameters(PreparedStatement stmt, Field[] fields, T obj) throws Exception {
        int index = 1;
        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase("id")) { // Exclude "id" field
                field.setAccessible(true);
                stmt.setObject(index++, field.get(obj));
            }
        }
    }

    private T mapResultSetToEntity(ResultSet rs) throws Exception {
        T obj = type.getDeclaredConstructor().newInstance();
        Field[] fields = type.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            // Obține numele coloanei din @Column sau folosește numele câmpului
            String columnName = field.isAnnotationPresent(Column.class)
                    ? field.getAnnotation(Column.class).name()
                    : field.getName();

            Object value = rs.getObject(columnName);
            if (value != null) {
                field.set(obj, value);
            }
        }
        return obj;
    }

}