package model;

/**
 * Represents the type of a ticket, specifying the level of access or service provided.
 */
public enum TicketType {
    /**
     * Standard ticket, provides regular access to the event.
     */
    STANDARD,

    /**
     * VIP ticket, provides enhanced access or premium services at the event.
     */
    VIP,

    /**
     * Early access ticket, allows entry before the general audience.
     */
    EARLY_ACCESS
}
