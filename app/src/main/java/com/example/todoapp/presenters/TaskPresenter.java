package com.example.todoapp.presenters;

import android.content.Context;

import com.example.todoapp.database.TaskDao;
import com.example.todoapp.database.TaskDb;
import com.example.todoapp.models.TaskLocal;
import com.example.todoapp.views.TaskView;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TaskPresenter {
    private final TaskDao taskDao;

    private final TaskView taskView;
    CompositeDisposable disposable = new CompositeDisposable();

    public TaskPresenter(Context context, TaskView taskView) {
        this.taskView = taskView;
        taskDao = TaskDb.getINSTANCE(context).taskDao();
    }

    public void saveTask(String name, String description, String dueDate, String group, String priority) {
        disposable.add(taskDao.insert(new TaskLocal(0, name, description, dueDate, group, priority, false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taskView::onTaskSave,
                        error -> taskView.showError("Error saving task")
                )
        );
    }

    public void loadTasks() {
        disposable.add(taskDao.getAllTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newTasks -> {
                            if (newTasks != null) {
                                taskView.loadTask(newTasks);
                            }
                            else {
                                taskView.showError("Error loading tasks");
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