package com.example.todo.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

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

    public TodoCategoryJoin(int todoId, int categoryId) {
        this.todoId = todoId;
        this.categoryId = categoryId;
    }
}
