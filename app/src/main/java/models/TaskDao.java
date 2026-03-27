package models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface TaskDao {
    @Insert
    Completable insert(TaskLocal task);

    @Query("SELECT * FROM tasks")
    Flowable<List<TaskLocal>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE isCompleted = 1")
    Flowable<List<TaskLocal>> getCompletedTasks();

    @Query("SELECT * FROM tasks WHERE isCompleted = 0")
    Flowable<List<TaskLocal>> getUncompletedTasks();

    @Query("UPDATE tasks SET isCompleted = 1 WHERE id = :id")
     void completeTask(int id);

    @Query("DELETE FROM tasks WHERE id = :id")
    public void deleteTask(int id);
}
