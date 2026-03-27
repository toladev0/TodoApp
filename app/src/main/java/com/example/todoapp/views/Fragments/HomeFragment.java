package com.example.todoapp.views.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.todoapp.R;
import com.example.todoapp.views.adapters.TaskAdapter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.example.todoapp.models.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.todoapp.database.services.TaskService;
import com.example.todoapp.views.DataStoreManager;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;

    private TaskAdapter taskAdapter;
    private final List<Task> tasks = new ArrayList<>();

    RxDataStore<Preferences> dataStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataStore = DataStoreManager.getInstance(requireContext());

        taskAdapter = new TaskAdapter(this.tasks);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTasks();
    }

    private void loadTasks(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TaskService taskService = retrofit.create(TaskService.class);
        Call<List<Task>> call = taskService.getTasks();

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tasks.addAll(response.body());
                    taskAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
                loadTaskOffline();
            }
        });
    }

    private static final Preferences.Key<String> TASK_KEY =
            PreferencesKeys.stringKey("task_title");

    @SuppressLint({"CheckResult", "NotifyDataSetChanged"})
    private void loadTaskOffline(){
        dataStore.data().map(prefs -> {
                    String task = prefs.get(TASK_KEY);
                    return task != null ? task : "";
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        task -> {
                            tasks.add(new Task(task, 1, 100));
                            taskAdapter.notifyDataSetChanged();
                        },
                        error -> Log.e("MY ERROR", Objects.requireNonNull(error.getMessage()))
                );
    }
}
