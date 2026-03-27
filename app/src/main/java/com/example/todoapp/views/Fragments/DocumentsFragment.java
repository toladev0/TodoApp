package com.example.todoapp.views.Fragments;

import android.annotation.SuppressLint;
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

import com.example.todoapp.R;
import com.example.todoapp.controllers.TaskController;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import com.example.todoapp.views.adapters.TaskLocalAdapter;
import com.example.todoapp.database.TaskDb;
import com.example.todoapp.models.TaskLocal;

public class DocumentsFragment extends Fragment implements TaskController.TaskCallBack{
    TaskDb taskDb;
    RecyclerView recyclerView;
    List<TaskLocal> tasks = new ArrayList<>();
    private TaskLocalAdapter taskLocalAdapter;
    TaskController controller;

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

        taskLocalAdapter = new TaskLocalAdapter(this.tasks);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(taskLocalAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        controller = new TaskController(requireContext());
        controller.loadTasks(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onTaskLoaded(List<TaskLocal> newTasks) {
        tasks.clear();      // Clear old data
        tasks.addAll(newTasks); // Add new data from DB
        taskLocalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {
        // Handle error
        Log.e("Error: ", message);
    }
}
