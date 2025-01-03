package model;
import controller.Controller;

/**
 * A utility class that provides a global access point for the Controller instance.
 * This is useful for ensuring a single instance of the controller is shared across different parts of the application.
 */
public class ControllerProvider {
    private static Controller controller;

    /**
     * Sets the Controller instance to be used globally.
     * @param controllerInstance The Controller instance to set.
     * @throws IllegalStateException If the controller is already initialized.
     */
    public static void setController(Controller controllerInstance) {
        controller = controllerInstance;
    }

    /**
     * Retrieves the globally set Controller instance.
     * @return The Controller instance.
     * @throws IllegalStateException If the controller has not been initialized.
     */
    public static Controller getController() {
        if (controller == null) {
            throw new IllegalStateException("Controller has not been initialized.");
        }
        return controller;
    }
    public static void initializeController(Controller newController) {
        if (controller == null) {
            controller = newController;
        }
    }
}