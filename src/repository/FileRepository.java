package repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import model.Identifiable;

/**
 * A repository implementation that stores data in a file.
 *
 * @param <T> The type of objects stored in the repository, which must implement HasId.
 */
public class FileRepository<T extends Identifiable> implements IRepository<T> {
    private final String filePath;

    /**
     * Constructs a new FileRepository with the specified file path.
     *
     * @param filePath The path to the file where data will be stored.
     */
    public FileRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Creates a new object in the repository if it doesn't already exist.
     *
     * @param obj The object to be added to the repository.
     */
    @Override
    public void create(T obj) {
        doInFile(data -> data.putIfAbsent(obj.getID(), obj));
    }

    /**
     * Retrieves an object from the repository by its ID.
     *
     * @param id The ID of the object to retrieve.
     * The object with the specified ID, or null if it doesn't exist.
     */
    @Override
    public void read(Integer id) {
        readDataFromFile().get(id);
    }

    /**
     * Updates an existing object in the repository.
     *
     * @param obj The object to update in the repository.
     */
    @Override
    public void update(T obj) {
        doInFile(data -> data.replace(obj.getID(), obj));
    }

    /**
     * Deletes an object from the repository by its ID.
     *
     * @param id The ID of the object to delete.
     */
    @Override
    public void delete(Integer id) {
        doInFile(data -> data.remove(id));
    }

    /**
     * Retrieves all objects stored in the repository.
     *
     * @return A list of all objects in the repository.
     */
    @Override
    public List<T> getAll() {
        return readDataFromFile().values().stream().collect(Collectors.toList());
    }

    /**
     * Performs a function on the data in the file, then saves the result.
     *
     * @param function The function to apply to the data.
     */
    private void doInFile(Consumer<Map<Integer, T>> function) {
        Map<Integer, T> data = readDataFromFile();
        function.accept(data);
        writeDataToFile(data);
    }

    /**
     * Reads the data from the file.
     *
     * @return The data stored in the file, or an empty map if the file is empty or not found.
     */
    private Map<Integer, T> readDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<Integer, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("File not found or empty. Creating new data storage.");
            return new HashMap<>();
        }
    }

    /**
     * Writes the updated data to the file.
     *
     * @param data The data to save to the file.
     */
    private void writeDataToFile(Map<Integer, T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}