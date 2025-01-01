package repository;

import model.*;

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
        this.tableName = isAbstract(type) ? null : getTableNameFromHibernate(type);
    }

    private boolean isAbstract(Class<?> clazz) {
        return clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers());
    }

    private String getTableNameFromHibernate(Class<?> entityClass) {
        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Class " + entityClass.getSimpleName() + " is not annotated with @Entity");
        }

        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (!table.name().isEmpty()) {
                return table.name();
            }
        }

        return entityClass.getSimpleName().toLowerCase();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public void create(T obj) {
        try (Connection conn = getConnection()) {
            Class<?> actualType = resolveConcreteType(obj.getClass());
            String actualTable = getTableNameFromHibernate(actualType);

            Field[] fields = actualType.getDeclaredFields();
            List<String> columns = getFieldNames(fields);
            List<String> placeholders = getPlaceholders(fields);

            if (columns.isEmpty() || placeholders.isEmpty()) {
                throw new IllegalStateException("Cannot generate SQL: no columns or placeholders available.");
            }

            String sql = "INSERT INTO " + actualTable + " (" + String.join(", ", columns) + ") VALUES (" + String.join(", ", placeholders) + ")";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                setStatementParameters(stmt, fields, obj);
                stmt.executeUpdate();

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
            if (type.equals(User.class)) {
                return (T) readFromSubtypes(conn, id, getUserSubtypes());
            } else if (type.equals(Event.class)) {
                return (T) readFromSubtypes(conn, id, getEventSubtypes());
            } else {
                String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return mapResultSetToEntity(rs);
                        }
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
            if (type.equals(User.class)) {
                results.addAll((java.util.Collection<? extends T>) getAllFromSubtypes(conn, getUserSubtypes()));
            } else if (type.equals(Event.class)) {
                results.addAll((java.util.Collection<? extends T>) getAllFromSubtypes(conn, getEventSubtypes()));
            } else {
                String sql = "SELECT * FROM " + tableName;
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        results.add(mapResultSetToEntity(rs));
                    }
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
            Class<?> actualType = resolveConcreteType(obj.getClass());
            String actualTable = getTableNameFromHibernate(actualType);

            Field[] fields = actualType.getDeclaredFields();
            String setClause = String.join(", ", getSetClauses(fields));

            String sql = "UPDATE " + actualTable + " SET " + setClause + " WHERE id = ?";
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
            if (type.equals(User.class)) {
                for (Class<? extends User> subtype : getUserSubtypes()) {
                    String tableName = getTableNameFromHibernate((Class<T>) subtype);
                    String sql = "DELETE FROM " + tableName + " WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, id);
                        stmt.executeUpdate();
                    }
                }
            } else if (type.equals(Event.class)) {
                for (Class<? extends Event> subtype : getEventSubtypes()) {
                    String tableName = getTableNameFromHibernate((Class<T>) subtype);
                    String sql = "DELETE FROM " + tableName + " WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, id);
                        stmt.executeUpdate();
                    }
                }
            } else {
                String sql = "DELETE FROM " + tableName + " WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper Methods
    private Class<?> resolveConcreteType(Class<?> clazz) {
        return isAbstract(clazz) ? null : clazz;
    }

    private List<Class<? extends User>> getUserSubtypes() {
        return List.of(Admin.class, Customer.class);
    }

    private List<Class<? extends Event>> getEventSubtypes() {
        return List.of(Concert.class, SportsEvent.class);
    }

    private <S> S readFromSubtypes(Connection conn, Integer id, List<Class<? extends S>> subtypes) throws Exception {
        for (Class<? extends S> subtype : subtypes) {
            String tableName = getTableNameFromHibernate((Class<T>) subtype);
            String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        S entity = subtype.getDeclaredConstructor().newInstance();
                        mapResultSetToFields(rs, entity);
                        return entity;
                    }
                }
            }
        }
        return null;
    }

    private <S> List<S> getAllFromSubtypes(Connection conn, List<Class<? extends S>> subtypes) throws Exception {
        List<S> results = new ArrayList<>();
        for (Class<? extends S> subtype : subtypes) {
            String tableName = getTableNameFromHibernate((Class<T>) subtype);
            String sql = "SELECT * FROM " + tableName;
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    S entity = subtype.getDeclaredConstructor().newInstance();
                    mapResultSetToFields(rs, entity);
                    results.add(entity);
                }
            }
        }
        return results;
    }

    private <S> void mapResultSetToFields(ResultSet rs, S entity) throws Exception {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String columnName = field.isAnnotationPresent(Column.class)
                    ? field.getAnnotation(Column.class).name()
                    : field.getName();
            Object value = rs.getObject(columnName);
            if (value != null) {
                field.set(entity, value);
            }
        }
    }

    private List<String> getFieldNames(Field[] fields) {
        List<String> names = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name().isEmpty() ? field.getName() : column.name();

                // Exclude câmpurile care se termină cu "_id"
                if (!columnName.endsWith("_id")) {
                    names.add(columnName);
                }
            }
        }
        return names;
    }

    private List<String> getPlaceholders(Field[] fields) {
        List<String> placeholders = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                String columnName = field.getAnnotation(Column.class).name().isEmpty() ? field.getName() : field.getAnnotation(Column.class).name();

                // Exclude câmpurile care se termină cu "_id"
                if (!columnName.endsWith("_id")) {
                    placeholders.add("?");
                }
            }
        }
        return placeholders;
    }


    private List<String> getSetClauses(Field[] fields) {
        List<String> clauses = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase("id")) {
                clauses.add(field.getName() + " = ?");
            }
        }
        return clauses;
    }

    private void setStatementParameters(PreparedStatement stmt, Field[] fields, T obj) throws Exception {
        int index = 1;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name().isEmpty() ? field.getName() : column.name();

                // Exclude câmpurile care se termină cu "_id"
                if (!columnName.endsWith("_id")) {
                    field.setAccessible(true);
                    stmt.setObject(index++, field.get(obj));
                }
            }
        }
    }

    private T mapResultSetToEntity(ResultSet rs) throws Exception {
        if (type.isInterface() || java.lang.reflect.Modifier.isAbstract(type.getModifiers())) {
            throw new InstantiationException("Cannot instantiate abstract class or interface: " + type.getName());
        }
        T obj = type.getDeclaredConstructor().newInstance();
        mapResultSetToFields(rs, obj);
        return obj;
    }

}
