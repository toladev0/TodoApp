package services;

import java.util.List;

import models.Task;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskService {

    @GET("toladev0/TodoApp/refs/heads/main/app/sampledata/data.json")
    Call<List<Task>> getTasks();
}
