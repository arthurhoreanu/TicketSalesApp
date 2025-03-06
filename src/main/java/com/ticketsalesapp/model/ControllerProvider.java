package main.java.com.ticketsalesapp.model;
import main.java.com.ticketsalesapp.controller.ApplicationController;

/**
 * A utility class that provides a global access point for the Controller instance.
 * This is useful for ensuring a single instance of the controller is shared across different parts of the application.
 */
public class ControllerProvider {
    private static ApplicationController applicationController;

    /**
     * Sets the Controller instance to be used globally.
     * @param applicationControllerInstance The Controller instance to set.
     * @throws IllegalStateException If the controller is already initialized.
     */
    public static void setController(ApplicationController applicationControllerInstance) {
        applicationController = applicationControllerInstance;
    }

    /**
     * Retrieves the globally set Controller instance.
     * @return The Controller instance.
     * @throws IllegalStateException If the controller has not been initialized.
     */
    public static ApplicationController getController() {
        if (applicationController == null) {
            throw new IllegalStateException("Controller has not been initialized.");
        }
        return applicationController;
    }
    public static void initializeController(ApplicationController newApplicationController) {
        if (applicationController == null) {
            applicationController = newApplicationController;
        }
    }
}