package com.example.todoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import models.TaskDao;
import models.TaskDb;
import models.TaskLocal;

public class AddFragment extends Fragment {
    ImageView btnBackView;
    EditText nameView;
    EditText descriptionView;
    TextView DueDateView;
    RadioGroup TaskGroupView;
    RadioGroup PriorityView;
    Button btnSaveView;
    Calendar calendar = Calendar.getInstance();
    TaskDao taskDao;
    CompositeDisposable disposable = new CompositeDisposable();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskDao = TaskDb.getINSTANCE(requireContext()).taskDao();
        nameView = view.findViewById(R.id.name);
        descriptionView = view.findViewById(R.id.description);
        DueDateView = view.findViewById(R.id.DueDate);
        TaskGroupView = view.findViewById(R.id.TaskGroup);
        PriorityView = view.findViewById(R.id.Priority);
        btnSaveView = view.findViewById(R.id.btnAddProject);
        btnBackView = view.findViewById(R.id.btnBack);

        btnSaveView.setOnClickListener(v -> {
            String name = nameView.getText().toString().trim();
            String description = descriptionView.getText().toString().trim();
            String dueDate = DueDateView.getText().toString();

            if (name.isEmpty()) {
                nameView.setError("Name is required");
                nameView.requestFocus();
                return;
            }

            if (description.isEmpty()) {
                descriptionView.setError("Description is required");
                descriptionView.requestFocus();
                return;
            }

            int selectedGroupId = TaskGroupView.getCheckedRadioButtonId();
            if (selectedGroupId == -1) {
                Toast.makeText(requireContext(), "Please select a task group", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedGroupBtn = view.findViewById(selectedGroupId);
            String group = selectedGroupBtn.getText().toString();

            int selectedPriorityId = PriorityView.getCheckedRadioButtonId();
            if (selectedPriorityId == -1) {
                Toast.makeText(requireContext(), "Please select a priority", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedPriorityBtn = view.findViewById(selectedPriorityId);
            String priority = selectedPriorityBtn.getText().toString();

            saveTask(name, description, dueDate, group, priority);
        });

        btnBackView.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit());

        updateDateTime();
        DueDateView.setOnClickListener(v -> showDatePicker());
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
        DueDateView.setText(sdf.format(calendar.getTime()));
        DueDateView.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void saveTask(String name, String description, String dueDate, String group, String priority) {
        disposable.add(taskDao.insert(new TaskLocal(0, name, description, dueDate, group, priority, false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.i("Room", "Task saved");
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Success")
                                    .setMessage("Task has been saved successfully!")
                                    .setPositiveButton("Go to List", (dialog, which) -> {
                                        requireActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_container, new DocumentsFragment())
                                                .commit();
                                    })
                                    .setNegativeButton("Add Another", (dialog, which) -> {
                                        clearFields();
                                    })
                                    .setCancelable(false)
                                    .show();
                        },
                        error -> {
                            Log.e("Room", "Error saving task", error);
                            Toast.makeText(requireContext(), "Error saving task", Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void clearFields() {
        nameView.setText("");
        descriptionView.setText("");
        TaskGroupView.clearCheck();
        PriorityView.clearCheck();
        calendar = Calendar.getInstance();
        updateDateTime();
        nameView.requestFocus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.clear(); // Cancels pending operations when Fragment is destroyed
        }
    }
}
