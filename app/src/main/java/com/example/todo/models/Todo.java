package com.example.todo.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.List;

/**
 * Entity class representing a Todo item in the database.
 */
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

    /**
     * Constructs a new Todo item.
     * @param title The title of the todo.
     * @param description The description of the todo.
     * @param priorityId The ID of the associated priority.
     * @param dueDate The due date of the todo.
     * @param isCompleted Whether the todo is marked as completed.
     */
    public Todo(String title, String description, int priorityId, String dueDate, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.priorityId = priorityId;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    /**
     * Gets the unique identifier of the Todo.
     * @return The ID.
     */
    public int getId() { return id; }

    /**
     * Sets the unique identifier of the Todo.
     * @param id The new ID.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the title of the Todo.
     * @return The title string.
     */
    public String getTitle() { return title; }

    /**
     * Sets the title of the Todo.
     * @param title The new title.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Gets the description of the Todo.
     * @return The description string.
     */
    public String getDescription() { return description; }

    /**
     * Sets the description of the Todo.
     * @param description The new description.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Gets the priority ID associated with this Todo.
     * @return The priority ID.
     */
    public int getPriorityId() { return priorityId; }

    /**
     * Sets the priority ID for this Todo.
     * @param priorityId The new priority ID.
     */
    public void setPriorityId(int priorityId) { this.priorityId = priorityId; }

    /**
     * Gets the due date of the Todo.
     * @return The due date string.
     */
    public String getDueDate() { return dueDate; }

    /**
     * Sets the due date of the Todo.
     * @param dueDate The new due date string.
     */
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    /**
     * Checks if the Todo is completed.
     * @return true if completed, false otherwise.
     */
    public boolean isCompleted() { return isCompleted; }

    /**
     * Sets the completion status of the Todo.
     * @param completed The new completion status.
     */
    public void setCompleted(boolean completed) { isCompleted = completed; }

    /**
     * Gets the list of categories associated with this Todo (non-persistent).
     * @return The list of categories.
     */
    public List<Category> getCategories() { return categories; }

    /**
     * Sets the list of categories for this Todo (non-persistent).
     * @param categories The list of categories.
     */
    public void setCategories(List<Category> categories) { this.categories = categories; }

    /**
     * Gets the name of the priority (non-persistent).
     * @return The priority name.
     */
    public String getPriorityName() { return priorityName; }

    /**
     * Sets the name of the priority (non-persistent).
     * @param priorityName The priority name.
     */
    public void setPriorityName(String priorityName) { this.priorityName = priorityName; }

    /**
     * Gets the rank of the priority for sorting (non-persistent).
     * @return The priority rank.
     */
    public int getPriorityRank() { return priorityRank; }

    /**
     * Sets the rank of the priority (non-persistent).
     * @param priorityRank The priority rank.
     */
    public void setPriorityRank(int priorityRank) { this.priorityRank = priorityRank; }
}
