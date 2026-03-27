package com.example.todoapp.controllers;

import android.content.Context;
import android.util.Log;

import com.example.todoapp.database.TaskDao;
import com.example.todoapp.database.TaskDb;
import com.example.todoapp.models.TaskLocal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TaskController {
    private final TaskDao taskDao;
    CompositeDisposable disposable = new CompositeDisposable();

    public interface TaskCallBack {
        void onTaskLoaded(List<TaskLocal> tasks);
        void onError(String message);
    }

    public TaskController(Context context){
        taskDao = TaskDb.getINSTANCE(context).taskDao();
    }

    public void saveTask(String name, String description, String dueDate, String group, String priority) {
        disposable.add(taskDao.insert(new TaskLocal(0, name, description, dueDate, group, priority, false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.i("Room", "Task saved"),
                        error -> Log.e("Room", "Error saving task", error)
                )
        );
    }

    public void loadTasks(TaskCallBack callBack) {
        disposable.add(taskDao.getAllTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newTasks -> {
                            if (newTasks != null) {
                                callBack.onTaskLoaded(newTasks);
                            }
                            else {
                                callBack.onError("Error loading tasks");
                            }
                        }
                )
        );
    }

    public void dispose() {
        if (disposable != null) {
            disposable.clear(); // Cancels pending operations when Fragment is destroyed
        }
    }
}