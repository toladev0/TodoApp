package com.example.todoapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskLocal {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String name;
    private final String description;
    private final String dueDate;
    private final String group;
    private final String priority;
    private final boolean isCompleted;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getGroup() {
        return group;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public TaskLocal(int id, String name, String description, String dueDate, String group, String priority, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.group = group;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }
}
