package com.example.todoapp;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddFragment extends Fragment {

    ImageView btnBackView;
    EditText projectNameView;
    EditText descriptionView;
    Button btnSaveView;
    TextView tvDueDateView;
    Calendar calendar = Calendar.getInstance();

    RxDataStore<Preferences> dataStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataStore = DataStoreManager.getInstance(requireContext());

        projectNameView = view.findViewById(R.id.projectName);
        descriptionView = view.findViewById(R.id.description);

        btnSaveView = view.findViewById(R.id.btnAddProject);
        btnSaveView.setOnClickListener(v -> {
            String projectName = projectNameView.getText().toString();
            saveTask(projectName);
        });

        btnBackView = view.findViewById(R.id.btnBack);
        btnBackView.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit());

        tvDueDateView = view.findViewById(R.id.tvDueDate);
        updateDateTime();
        tvDueDateView.setOnClickListener(v -> showDatePicker());
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
        tvDueDateView.setText(sdf.format(calendar.getTime()));
        tvDueDateView.setTextColor(getResources().getColor(android.R.color.black));
    }

    private static final Preferences.Key<String> TASK_KEY =
            PreferencesKeys.stringKey("task_title");

    @SuppressLint("CheckResult")
    private void saveTask(String title) {
        dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(TASK_KEY, title);
            return Single.just(mutablePreferences);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        preferences -> Log.i("MY PREFS", preferences.toString()),
                        error -> Log.e("MY ERROR", Objects.requireNonNull(error.getMessage()))
                );
    }
}