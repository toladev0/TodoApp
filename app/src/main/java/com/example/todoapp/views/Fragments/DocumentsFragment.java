package com.example.todoapp.views.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todoapp.databinding.FragmentDocumentsBinding;
import com.example.todoapp.viewmodels.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

import com.example.todoapp.views.adapters.TaskLocalAdapter;
import com.example.todoapp.models.TaskLocal;

public class DocumentsFragment extends Fragment {
    private FragmentDocumentsBinding binding;
    private final List<TaskLocal> tasks = new ArrayList<>();
    private TaskLocalAdapter taskLocalAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDocumentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tabs
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Today's Tasks"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Completed"));

        taskLocalAdapter = new TaskLocalAdapter(this.tasks);
        binding.recyclerView.setAdapter(taskLocalAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TaskViewModel viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        viewModel.tasks.observe(getViewLifecycleOwner(), this::loadTask);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadTask(List<TaskLocal> newTasks) {
        tasks.clear();      // Clear old data
        tasks.addAll(newTasks); // Add new data from DB
        taskLocalAdapter.notifyDataSetChanged();
    }
}
