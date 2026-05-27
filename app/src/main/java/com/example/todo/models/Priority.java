package com.example.todo.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a Priority level for Todo items in the database.
 */
@Entity(tableName = "priorities")
public class Priority {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int rank; // Higher rank means higher importance

    /**
     * Constructs a new Priority.
     * @param name The name of the priority (e.g., "High", "Medium", "Low").
     * @param rank The numerical rank indicating importance.
     */
    public Priority(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    /**
     * Gets the unique identifier of the Priority.
     * @return The ID.
     */
    public int getId() { return id; }

    /**
     * Sets the unique identifier of the Priority.
     * @param id The new ID.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the name of the Priority.
     * @return The name string.
     */
    public String getName() { return name; }

    /**
     * Sets the name of the Priority.
     * @param name The new name.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the rank of the Priority.
     * @return The rank value.
     */
    public int getRank() { return rank; }

    /**
     * Sets the rank of the Priority.
     * @param rank The new rank value.
     */
    public void setRank(int rank) { this.rank = rank; }

    /**
     * Returns a string representation of the Priority, which is its name.
     * @return The name of the priority.
     */
    @Override
    public String toString() {
        return name;
    }
}
