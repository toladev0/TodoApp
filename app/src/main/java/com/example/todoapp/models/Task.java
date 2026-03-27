package com.example.todoapp.models;

public class Task {
    private final String title;
    private final int totalTasks;
    private final int progress;

    public Task(String title, int totalTasks, int progress) {
        this.title = title;
        this.totalTasks = totalTasks;
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public int getProgress() {
        return progress;
    }
}
