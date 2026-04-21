package com.example.todo.models;

import java.io.Serializable;

public class Category implements Serializable {
    private String id;
    private String name;
    private int iconResId;

    public Category(String id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getIconResId() { return iconResId; }
    public void setIconResId(int iconResId) { this.iconResId = iconResId; }

    @Override
    public String toString() {
        return name;
    }
}
