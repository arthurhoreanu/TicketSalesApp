package model;

import controller.Controller;

public class ControllerProvider {
    private static Controller controller;

    public static void setController(Controller controllerInstance) {
        controller = controllerInstance;
    }

    public static Controller getController() {
        if (controller == null) {
            throw new IllegalStateException("Controller has not been initialized.");
        }
        return controller;
    }
}