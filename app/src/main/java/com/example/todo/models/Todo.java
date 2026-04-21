package com.example.todo.models;

import java.io.Serializable;
import java.util.List;

public class Todo implements Serializable {
    private String title;
    private String description;
    private String priority;
    private String dueDate;
    private List<Category> categories;

    public Todo(String title, String description, String priority, String dueDate, List<Category> categories) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.categories = categories;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getDueDate() { return dueDate; }
    public List<Category> getCategories() { return categories; }
}
