package com.example.todo.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todo.models.Category;
import com.example.todo.models.Priority;
import com.example.todo.models.Todo;
import com.example.todo.models.TodoCategoryJoin;

@Database(entities = {Todo.class, Category.class, Priority.class, TodoCategoryJoin.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract TodoDao todoDao();
    public abstract CategoryDao categoryDao();
    public abstract PriorityDao priorityDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "todo_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
