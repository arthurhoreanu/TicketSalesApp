package repository;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalIdGenerator {

    private static final AtomicInteger currentId = new AtomicInteger(0);

    /**
     * Initializes the global ID to the maximum ID found across all repositories.
     *
     * @param maxId The highest ID found during initialization.
     */
    public static void initialize(int maxId) {
        currentId.set(maxId);
    }

    /**
     * Generates the next unique global ID.
     *
     * @return The next unique ID.
     */
    public static int getNextId() {
        return currentId.incrementAndGet();
    }

    /**
     * Gets the current highest ID.
     *
     * @return The current highest ID.
     */
    public static int getCurrentId() {
        return currentId.get();
    }
}
