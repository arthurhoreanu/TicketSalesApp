package model;

/**
 * Represents the status of an order.
 * This enum defines three possible states for an order:
 */
public enum OrderStatus {
    /**
     * Indicates that the order has been created but is not yet completed.
     */
    PENDING,

    /**
     * Indicates that the order has been successfully processed and completed.
     */
    COMPLETED,

    /**
     * Indicates that the order has been canceled and will not be processed further.
     */
    CANCELED
}
