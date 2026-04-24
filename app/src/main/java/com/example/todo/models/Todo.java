package com.example.todo.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity(tableName = "todos",
        foreignKeys = @ForeignKey(entity = Priority.class,
                parentColumns = "id",
                childColumns = "priorityId",
                onDelete = ForeignKey.SET_DEFAULT),
        indices = {@Index("priorityId")})
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int priorityId;
    private String dueDate;
    private boolean isCompleted;
    
    @Ignore
    private List<Category> categories;
    
    @Ignore
    private String priorityName;

    @Ignore
    private int priorityRank;

    public Todo(String title, String description, int priorityId, String dueDate, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.priorityId = priorityId;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getPriorityId() { return priorityId; }
    public void setPriorityId(int priorityId) { this.priorityId = priorityId; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }

    public String getPriorityName() { return priorityName; }
    public void setPriorityName(String priorityName) { this.priorityName = priorityName; }

    public int getPriorityRank() { return priorityRank; }
    public void setPriorityRank(int priorityRank) { this.priorityRank = priorityRank; }
}
