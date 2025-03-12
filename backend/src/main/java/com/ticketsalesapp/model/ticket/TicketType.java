package com.ticketsalesapp.model.ticket;

/**
 * Represents the type of ticket, specifying the level of access or service provided.
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
     * A certain quantity of tickets bought at a certain time before the event.
     */
    EARLY_BIRD
}