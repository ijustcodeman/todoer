package com.example.todo.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a Category for Todo items in the database.
 */
@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int iconResId;

    /**
     * Constructs a new Category.
     * @param name The name of the category.
     * @param iconResId The resource ID of the icon associated with the category.
     */
    public Category(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    /**
     * Gets the unique identifier of the Category.
     * @return The ID.
     */
    public int getId() { return id; }

    /**
     * Sets the unique identifier of the Category.
     * @param id The new ID.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the name of the Category.
     * @return The name string.
     */
    public String getName() { return name; }

    /**
     * Sets the name of the Category.
     * @param name The new name.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the icon resource ID for the Category.
     * @return The icon resource ID.
     */
    public int getIconResId() { return iconResId; }

    /**
     * Sets the icon resource ID for the Category.
     * @param iconResId The new icon resource ID.
     */
    public void setIconResId(int iconResId) { this.iconResId = iconResId; }

    /**
     * Returns a string representation of the Category, which is its name.
     * @return The name of the category.
     */
    @Override
    public String toString() {
        return name;
    }
}
