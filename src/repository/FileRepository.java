package repository;

import model.Identifiable;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing persistence of objects to and from CSV files.
 * Supports basic CRUD operations on any objects implementing {@link Identifiable}.
 *
 * @param <T> Type of objects managed by the repository, which must implement {@link Identifiable}.
 */
public class FileRepository<T extends Identifiable> implements IRepository<T> {
    private final String filePath;
    private final Class<T> type;

    /**
     * Constructs a new repository with a specified file path and type.
     * @param filePath The path to the CSV file where data will be stored.
     * @param type     The class type of the objects stored in this repository.
     */
    public FileRepository(String filePath, Class<T> type) {
        this.filePath = filePath;
        this.type = type;
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
        boolean found = false; // Flag pentru a verifica dacă ID-ul a fost găsit

        try (
                BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;

            // Citește fiecare linie din fișierul original
            while ((line = reader.readLine()) != null) {
                // Extrage ID-ul din linie (presupunând că ID-ul este primul câmp)
                String[] values = line.split(",");
                Integer lineId = Integer.parseInt(values[0].trim());

                // Dacă ID-ul este cel pe care vrem să-l ștergem, îl sărim
                if (lineId.equals(id)) {
                    found = true;
                    continue; // Trecem la următoarea linie, astfel "ștergând-o"
                }

                // Scriem în fișierul temporar dacă ID-ul nu corespunde celui de șters
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
        try {
            // Obtain the static `fromCsvFormat` method
            Method fromCsvFormatMethod = type.getDeclaredMethod("fromCsvFormat", String.class);

            // Read each line from the file and convert it to an object
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        @SuppressWarnings("unchecked")
                        T item = (T) fromCsvFormatMethod.invoke(null, line);
                        if (item != null) {
                            items.add(item);
                        } else {
                            System.out.println("Warning: Skipping null or malformed line: " + line);
                        }
                    } catch (Exception e) {
                        System.out.println("Error parsing line, skipping: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            System.out.println("Reflection error invoking fromCsvFormat: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

}