package com.example.todoapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.todoapp.models.TaskLocal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface TaskDao {
    @Insert
    Completable insert(TaskLocal task);

    @Query("SELECT * FROM tasks")
    LiveData<List<TaskLocal>> getAllTasks();
}
