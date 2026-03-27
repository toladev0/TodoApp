package com.example.todoapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.todoapp.models.TaskLocal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface TaskDao {
    @Insert
    Completable insert(TaskLocal task);

    @Query("SELECT * FROM tasks")
    Flowable<List<TaskLocal>> getAllTasks();
}
