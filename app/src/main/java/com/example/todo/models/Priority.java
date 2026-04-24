package com.example.todo.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "priorities")
public class Priority {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int rank; // Je höher der Rank, desto wichtiger die Priorität

    public Priority(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    @Override
    public String toString() {
        return name;
    }
}
