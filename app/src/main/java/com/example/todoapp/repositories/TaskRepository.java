package com.example.todoapp.repositories;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.todoapp.database.TaskDao;
import com.example.todoapp.database.TaskDb;
import com.example.todoapp.models.TaskLocal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TaskRepository {
    private final TaskDao taskDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TaskRepository(Application app) {
        TaskDb db = TaskDb.getINSTANCE(app);
        taskDao = db.taskDao();
    }

    public LiveData<List<TaskLocal>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    @SuppressLint("CheckResult")
    public void insert(String name, String description, String dueDate, String group, String priority) {
        executorService.execute(() -> taskDao.insert(new TaskLocal(0, name, description, dueDate, group, priority, false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("TaskRepository", "Task inserted successfully"),
                        error -> Log.e("TaskRepository", "Error inserting task", error)
                ));
    }
}
