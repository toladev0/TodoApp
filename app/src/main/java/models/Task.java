package models;

public class Task {
    private String title;
    private int totalTasks;
    private int progress;

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
