package models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TaskLocal.class}, version = 1, exportSchema = false)
public abstract class TaskDb extends RoomDatabase {
    public abstract TaskDao taskDao();

    public static TaskDb INSTANCE;

    public static TaskDb getINSTANCE(Context context) {
        if(INSTANCE == null) {
            synchronized (TaskDb.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskDb.class, "task_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}