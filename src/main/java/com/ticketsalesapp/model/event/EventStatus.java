package main.java.com.ticketsalesapp.model.event;

/**
 * Enum representing the possible statuses for an event.
 */
public enum EventStatus {
    /**
     * Indicates that the event is scheduled to take place.
     */
    SCHEDULED,

    /**
     * Indicates that the event has been cancelled and will not take place.
     */
    CANCELLED,

    /**
     * Indicates that the event has been completed.
     */
    COMPLETED
}
