package com.example.todo.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.models.Category;

import java.util.List;

/**
 * Data Access Object for the "categories" table.
 * Defines the database operations for Category entities.
 */
@Dao
public interface CategoryDao {
    /**
     * Inserts a new category into the database.
     * @param category The category to insert.
     * @return The row ID of the newly inserted category.
     */
    @Insert
    long insert(Category category);

    /**
     * Updates an existing category in the database.
     * @param category The category to update.
     */
    @Update
    void update(Category category);

    /**
     * Deletes a category from the database.
     * @param category The category to delete.
     */
    @Delete
    void delete(Category category);

    /**
     * Retrieves all categories from the database.
     * @return A list of all categories.
     */
    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    /**
     * Retrieves a specific category by its ID.
     * @param id The ID of the category.
     * @return The category with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM categories WHERE id = :id")
    Category getCategoryById(int id);
}
