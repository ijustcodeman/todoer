package com.example.todo.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

/**
 * Junction table entity for the many-to-many relationship between Todo items and Categories.
 */
@Entity(tableName = "todo_category_join",
        primaryKeys = {"todoId", "categoryId"},
        foreignKeys = {
                @ForeignKey(entity = Todo.class,
                        parentColumns = "id",
                        childColumns = "todoId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Category.class,
                        parentColumns = "id",
                        childColumns = "categoryId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("categoryId")})
public class TodoCategoryJoin {
    public final int todoId;
    public final int categoryId;

    /**
     * Constructs a new junction entry.
     * @param todoId The ID of the Todo item.
     * @param categoryId The ID of the Category.
     */
    public TodoCategoryJoin(int todoId, int categoryId) {
        this.todoId = todoId;
        this.categoryId = categoryId;
    }
}
