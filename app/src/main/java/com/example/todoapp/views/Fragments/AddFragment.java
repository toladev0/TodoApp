package com.example.todoapp.views.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.todoapp.R;
import com.example.todoapp.databinding.FragmentAddBinding;
import com.example.todoapp.viewmodels.TaskViewModel;

public class AddFragment extends Fragment {
    private FragmentAddBinding binding;
    private Calendar calendar = Calendar.getInstance();
    private TaskViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        binding.btnAddProject.setOnClickListener(v -> {
            String name = binding.name.getText().toString().trim();
            String description = binding.description.getText().toString().trim();
            String dueDate = binding.DueDate.getText().toString();

            if (name.isEmpty()) {
                binding.name.setError("Name is required");
                binding.name.requestFocus();
                return;
            }

            if (description.isEmpty()) {
                binding.description.setError("Description is required");
                binding.description.requestFocus();
                return;
            }

            int selectedGroupId = binding.TaskGroup.getCheckedRadioButtonId();
            if (selectedGroupId == -1) {
                Toast.makeText(requireContext(), "Please select a task group", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedGroupBtn = view.findViewById(selectedGroupId);
            String group = selectedGroupBtn.getText().toString();

            int selectedPriorityId = binding.Priority.getCheckedRadioButtonId();
            if (selectedPriorityId == -1) {
                Toast.makeText(requireContext(), "Please select a priority", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedPriorityBtn = view.findViewById(selectedPriorityId);
            String priority = selectedPriorityBtn.getText().toString();

            viewModel.insert(name, description, dueDate, group, priority);
            clearFields();
            Toast.makeText(requireContext(), "Task added successfully", Toast.LENGTH_SHORT).show();
            
            // Navigate back to Home after saving
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DocumentsFragment())
                    .commit();
        });

        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DocumentsFragment())
                .commit());

        updateDateTime();
        binding.DueDate.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, y, m, d) -> {
                    calendar.set(Calendar.YEAR, y);
                    calendar.set(Calendar.MONTH, m);
                    calendar.set(Calendar.DAY_OF_MONTH, d);
                    showTimePicker();
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, h, m) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, m);
                    updateDateTime();
                },
                hour, minute, false // false = AM/PM
        );

        timePickerDialog.show();
    }

    private void updateDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
        binding.DueDate.setText(sdf.format(calendar.getTime()));
        binding.DueDate.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void clearFields() {
        binding.name.setText("");
        binding.description.setText("");
        binding.TaskGroup.clearCheck();
        binding.Priority.clearCheck();
        calendar = Calendar.getInstance();
        updateDateTime();
        binding.name.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
