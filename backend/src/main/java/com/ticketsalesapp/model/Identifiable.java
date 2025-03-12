package com.ticketsalesapp.model;

/**
 * Interface representing an entity that can be uniquely identified by an ID.
 */
public interface Identifiable {

    Integer getId();
    void setId(int id);
}
