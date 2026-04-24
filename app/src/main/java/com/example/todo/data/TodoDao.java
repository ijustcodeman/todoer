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

@Dao
public interface TodoDao {
    @Insert
    long insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("SELECT * FROM todos")
    List<Todo> getAllTodos();

    @Insert
    void insertTodoCategoryJoin(TodoCategoryJoin join);

    @Transaction
    @Query("SELECT categories.* FROM categories " +
           "INNER JOIN todo_category_join ON categories.id = todo_category_join.categoryId " +
           "WHERE todo_category_join.todoId = :todoId")
    List<Category> getCategoriesForTodo(int todoId);

    @Query("DELETE FROM todo_category_join WHERE todoId = :todoId")
    void deleteCategoriesForTodo(int todoId);

    @Query("SELECT COUNT(*) FROM todos WHERE priorityId = :priorityId")
    int getTodoCountByPriority(int priorityId);
}
