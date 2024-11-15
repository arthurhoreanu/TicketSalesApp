package repository;

import model.Identifiable;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Repository for managing persistence of objects to and from CSV files.
 * Supports basic CRUD operations on any objects implementing {@link Identifiable}.
 *
 * @param <T> Type of objects managed by the repository, which must implement {@link Identifiable}.
 */
public class FileRepository<T extends Identifiable> implements IRepository<T> {
    private final String filePath;
    private final Function<String, T> fromCsvFormat;

    /**
     * Constructs a new repository with a specified file path and type.
     * @param filePath The path to the CSV file where data will be stored.
     */
    public FileRepository(String filePath, Function<String, T> fromCsvFormat) {
        this.filePath = filePath;
        this.fromCsvFormat = fromCsvFormat;
    }

    /**
     * Creates a new object in the CSV file.
     * @param obj The object to create and store.
     */
    @Override
    public void create(T obj) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(obj.toCsvFormat() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads an object by ID from the repository.
     * @param id The ID of the object to retrieve.
     * @return The object with the specified ID, or null if not found.
     */
    @Override
    public T read(Integer id) {
        return getAll().stream()
                .filter(obj -> obj.getID().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing object in the repository by first deleting the old entry and creating a new one.
     * @param obj The updated object to save.
     */
    @Override
    public void update(T obj) {
        delete(obj.getID());
        create(obj);
    }

    /**
     * Deletes an object by ID from the repository.
     * @param id The ID of the object to delete.
     */
    @Override
    public void delete(Integer id) {
        File originalFile = new File(filePath);
        File tempFile = new File("tempfile.csv");
        boolean found = false;
        try (
                BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
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
        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
        }
        // Înlocuiește fișierul original doar dacă linia a fost găsită și ștersă
        if (found) {
            if (originalFile.delete()) { // Ștergem fișierul original
                if (!tempFile.renameTo(originalFile)) { // Redenumim fișierul temporar
                    System.out.println("Error: Could not rename temp file to original file name.");
                }
            } else {
                System.out.println("Error: Could not delete original file.");
            }
        } else {
            System.out.println("ID not found: " + id);
            tempFile.delete(); // Ștergem fișierul temporar dacă ID-ul nu a fost găsit
        }
    }

    /**
     * Retrieves all objects from the CSV file by reading each line and converting it to an object.
     * @return A list of all objects in the file.
     */

    @Override
    public List<T> getAll() {
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
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

}