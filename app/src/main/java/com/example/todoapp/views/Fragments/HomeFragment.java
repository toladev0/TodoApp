package com.example.todoapp.views.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.todoapp.R;
import com.example.todoapp.views.adapters.TaskAdapter;
import com.example.todoapp.models.Task;
import com.example.todoapp.models.TaskLocal;
import com.example.todoapp.viewmodels.TaskViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.todoapp.database.services.TaskService;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private final List<Task> tasks = new ArrayList<>();
    private TaskViewModel taskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskAdapter = new TaskAdapter(this.tasks);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTasks();
    }

    private void loadTasks() {
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
                    tasks.clear();
                    tasks.addAll(response.body());
                    taskAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    loadTaskOffline();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
                loadTaskOffline();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadTaskOffline() {
        taskViewModel.tasks.observe(getViewLifecycleOwner(), taskLocals -> {
            if (taskLocals != null) {
                tasks.clear();
                
                // Grouping TaskLocal data by 'group' to create Task objects for HomeFragment
                Map<String, List<TaskLocal>> groupedTasks = new HashMap<>();
                for (TaskLocal local : taskLocals) {
                    String groupName = local.getGroup();
                    if (!groupedTasks.containsKey(groupName)) {
                        groupedTasks.put(groupName, new ArrayList<>());
                    }
                    groupedTasks.get(groupName).add(local);
                }

                for (Map.Entry<String, List<TaskLocal>> entry : groupedTasks.entrySet()) {
                    String groupName = entry.getKey();
                    List<TaskLocal> groupTasks = entry.getValue();
                    
                    int total = groupTasks.size();
                    int completed = 0;
                    for (TaskLocal t : groupTasks) {
                        if (t.isCompleted()) completed++;
                    }
                    
                    int progress = (total > 0) ? (completed * 100 / total) : 0;
                    tasks.add(new Task(groupName, total, progress));
                }

                taskAdapter.notifyDataSetChanged();
            }
        });
    }
}
