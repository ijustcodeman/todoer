package com.example.todo.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.todo.models.Category;
import com.example.todo.models.Todo;
import com.example.todo.models.TodoCategoryJoin;

import java.util.List;

/**
 * Data Access Object (DAO) for the "todos" table.
 * Defines the database operations for Todo items and their relationship with categories.
 */
@Dao
public interface TodoDao {
    /**
     * Inserts a new Todo item into the database.
     * @param todo The Todo item to insert.
     * @return The row ID of the newly inserted item.
     */
    @Insert
    long insert(Todo todo);

    /**
     * Updates an existing Todo item in the database.
     * @param todo The Todo item to update.
     */
    @Update
    void update(Todo todo);

    /**
     * Deletes a Todo item from the database.
     * @param todo The Todo item to delete.
     */
    @Delete
    void delete(Todo todo);

    /**
     * Retrieves all Todo items from the database.
     * @return A list of all Todo items.
     */
    @Query("SELECT * FROM todos")
    List<Todo> getAllTodos();

    /**
     * Retrieves a specific Todo item by its ID.
     * @param id The ID of the Todo item.
     * @return The Todo item with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM todos WHERE id = :id")
    Todo getTodoById(int id);

    /**
     * Inserts a junction entry to associate a Todo with a Category.
     * @param join The TodoCategoryJoin object representing the relationship.
     */
    @Insert
    void insertTodoCategoryJoin(TodoCategoryJoin join);

    /**
     * Retrieves all categories associated with a specific Todo item.
     * This operation is wrapped in a transaction to ensure data consistency.
     * @param todoId The ID of the Todo item.
     * @return A list of categories linked to the specified Todo.
     */
    @Transaction
    @Query("SELECT categories.* FROM categories " +
           "INNER JOIN todo_category_join ON categories.id = todo_category_join.categoryId " +
           "WHERE todo_category_join.todoId = :todoId")
    List<Category> getCategoriesForTodo(int todoId);

    /**
     * Deletes all category associations for a specific Todo item.
     * @param todoId The ID of the Todo item.
     */
    @Query("DELETE FROM todo_category_join WHERE todoId = :todoId")
    void deleteCategoriesForTodo(int todoId);

    /**
     * Counts how many Todo items are assigned to a specific priority.
     * @param priorityId The ID of the priority.
     * @return The number of Todo items with the given priority ID.
     */
    @Query("SELECT COUNT(*) FROM todos WHERE priorityId = :priorityId")
    int getTodoCountByPriority(int priorityId);
}
