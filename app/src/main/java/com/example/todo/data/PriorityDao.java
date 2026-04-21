package com.example.todo.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.models.Priority;

import java.util.List;

@Dao
public interface PriorityDao {
    @Insert
    long insert(Priority priority);

    @Update
    void update(Priority priority);

    @Delete
    void delete(Priority priority);

    @Query("SELECT * FROM priorities")
    List<Priority> getAllPriorities();
}
