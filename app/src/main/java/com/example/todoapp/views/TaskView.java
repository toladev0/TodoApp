package com.example.todoapp.views;

import com.example.todoapp.models.TaskLocal;

import java.util.List;

public interface TaskView {
    void loadTask(List<TaskLocal> tasks);
    void showError(String message);
    void onTaskSave();
}
