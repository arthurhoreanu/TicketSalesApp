package repository;

import model.Identifiable;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.function.Function;

/**
 * Repository for managing persistence of objects to and from CSV files.
 * Supports basic CRUD operations on any objects implementing {@link Identifiable}.
 * Thread-safe implementation using locks for concurrency.
 *
 * @param <T> Type of objects managed by the repository, which must implement {@link Identifiable}.
 */
public class FileRepository<T extends Identifiable> implements IRepository<T> {
    private final String filePath;
    private final Function<String, T> fromCsvFormat;
    private final Lock lock = new ReentrantLock();

    /**
     * Constructs a new repository with a specified file path and CSV parser.
     *
     * @param filePath      The path to the CSV file where data will be stored.
     * @param fromCsvFormat Function to convert a CSV line to an object of type T.
     */
    public FileRepository(String filePath, Function<String, T> fromCsvFormat) {
        this.filePath = filePath;
        this.fromCsvFormat = fromCsvFormat;
        initializeFile();
    }

    /**
     * Ensures the file exists during initialization.
     */
    private void initializeFile() {
        try {
            Path path = Paths.get(filePath);
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error initializing file: " + filePath, e);
        }
    }

    /**
     * Creates a new object in the CSV file.
     *
     * @param obj The object to create and store.
     */
    @Override
    public void create(T obj) {
        lock.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(obj.toCsvFormat());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + filePath, e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Reads an object by ID from the repository.
     *
     * @param id The ID of the object to retrieve.
     * @return The object with the specified ID, or null if not found.
     */
    @Override
    public T read(Integer id) {
        lock.lock();
        try {
            return getAll().stream()
                    .filter(obj -> obj.getID().equals(id))
                    .findFirst()
                    .orElse(null);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Updates an existing object in the repository.
     *
     * @param obj The updated object to save.
     */
    @Override
    public void update(T obj) {
        lock.lock();
        try {
            delete(obj.getID());
            create(obj);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Deletes an object by ID from the repository.
     *
     * @param id The ID of the object to delete.
     */
    @Override
    public void delete(Integer id) {
        lock.lock();
        try {
            File originalFile = new File(filePath);
            File tempFile = new File("tempfile.csv");
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    Integer lineId = Integer.parseInt(values[0].trim());
                    if (lineId.equals(id)) {
                        found = true;
                        continue;
                    }
                    writer.write(line);
                    writer.newLine();
                }
            }

            if (!found) {
                throw new IllegalArgumentException("No object found with ID: " + id);
            }

            if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
                throw new RuntimeException("Error replacing original file with updated content.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing delete operation: " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves all objects from the CSV file by reading each line and converting it to an object.
     *
     * @return A list of all objects in the file.
     */
    @Override
    public List<T> getAll() {
        lock.lock();
        List<T> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    T item = fromCsvFormat.apply(line);
                    if (item != null) {
                        items.add(item);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line, skipping: " + line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        } finally {
            lock.unlock();
        }
        return items;
    }
}


