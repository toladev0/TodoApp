package com.example.todoapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import adapters.TaskLocalAdapter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import models.TaskDao;
import models.TaskDb;
import models.TaskLocal;

public class DocumentsFragment extends Fragment {
    TaskDb taskDb;
    TaskDao taskDao;
    CompositeDisposable disposable = new CompositeDisposable();
    RecyclerView recyclerView;
    List<TaskLocal> tasks = new ArrayList<>();
    private TaskLocalAdapter taskLocalAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_documents, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Tabs
        tabLayout.addTab(tabLayout.newTab().setText("Today's Tasks"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));

        taskDb = TaskDb.getINSTANCE(requireContext());
        taskDao = taskDb.taskDao();

        taskLocalAdapter = new TaskLocalAdapter(this.tasks);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(taskLocalAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTasks();
    }

    private void loadTasks() {
        disposable.add(taskDao.getAllTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newTasks -> {
                            if (newTasks != null) {
                                tasks.clear();      // Clear old data
                                tasks.addAll(newTasks); // Add new data from DB
                                taskLocalAdapter.notifyDataSetChanged();
                            }
                        },
                        throwable -> Log.e("Room", "Error loading tasks", throwable)
                )
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear(); // Important: prevent memory leaks
    }
}
