package com.example.todo.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todo.models.Category;
import com.example.todo.models.Priority;
import com.example.todo.models.Todo;
import com.example.todo.models.TodoCategoryJoin;

/**
 * Main database class for the application, using Room persistence.
 * Defines the entities and provides access to the DAOs.
 */
@Database(entities = {Todo.class, Category.class, Priority.class, TodoCategoryJoin.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    /**
     * Provides access to the Todo data access object.
     */
    public abstract TodoDao todoDao();

    /**
     * Provides access to the Category data access object.
     */
    public abstract CategoryDao categoryDao();

    /**
     * Provides access to the Priority data access object.
     */
    public abstract PriorityDao priorityDao();

    /**
     * Returns a singleton instance of the AppDatabase.
     * If the instance does not exist, it is created using Room's databaseBuilder.
     */
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
