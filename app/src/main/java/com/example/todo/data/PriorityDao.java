package com.example.todo.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.models.Priority;

import java.util.List;

/**
 * Data Access Object (DAO) for the "priorities" table.
 * Defines the database operations for Priority entities.
 */
@Dao
public interface PriorityDao {
    /**
     * Inserts a new priority into the database.
     * @param priority The priority to insert.
     * @return The row ID of the newly inserted priority.
     */
    @Insert
    long insert(Priority priority);

    /**
     * Updates an existing priority in the database.
     * @param priority The priority to update.
     */
    @Update
    void update(Priority priority);

    /**
     * Deletes a priority from the database.
     * @param priority The priority to delete.
     */
    @Delete
    void delete(Priority priority);

    /**
     * Retrieves all priorities from the database.
     * @return A list of all priorities.
     */
    @Query("SELECT * FROM priorities")
    List<Priority> getAllPriorities();
}
