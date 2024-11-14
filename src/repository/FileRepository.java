package repository;

import model.Identifiable;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FileRepository<T extends Identifiable> implements IRepository<T> {
    private final String filePath;
    private final Class<T> type;

    public FileRepository(String filePath, Class<T> type) {
        this.filePath = filePath;
        this.type = type;
    }

    @Override
    public void create(T obj) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(obj.toCsvFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void read(Integer id) {
        getAll().stream()
                .filter(obj -> obj.getID().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(T obj) {
        List<T> allData = getAll();
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (T item : allData) {
                if (item.getID().equals(obj.getID())) {
                    writer.println(obj.toCsvFormat());
                } else {
                    writer.println(item.toCsvFormat());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        List<T> allData = getAll();
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (T item : allData) {
                if (!item.getID().equals(id)) {
                    writer.println(item.toCsvFormat());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> getAll() {
        List<T> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Method fromCsvMethod = type.getMethod("fromCsvFormat", String.class);

            while ((line = reader.readLine()) != null) {
                T item = (T) fromCsvMethod.invoke(null, line);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
