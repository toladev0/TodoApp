package com.example.todoapp.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todoapp.models.TaskLocal;
import com.example.todoapp.repositories.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repository;
    public LiveData<List<TaskLocal>> tasks;

    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
        tasks = repository.getAllTasks();
    }
    public void insert(String name, String description, String dueDate, String group, String priority){
        repository.insert(name, description, dueDate, group, priority);
    }
}
