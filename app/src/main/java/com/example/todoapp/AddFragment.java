package com.example.todoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddFragment extends Fragment {

    ImageView btnBackView;
    EditText projectNameView;
    EditText descriptionView;
    Button btnSaveView;

    RxDataStore<Preferences> dataStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataStore = new RxPreferenceDataStoreBuilder(requireContext(), "tasks").build();

        projectNameView = view.findViewById(R.id.projectName);
        descriptionView = view.findViewById(R.id.description);

        btnSaveView = view.findViewById(R.id.btnAddProject);
        btnSaveView.setOnClickListener(v -> {
            String projectName = projectNameView.getText().toString();
            saveTask(projectName);
        });

        btnBackView = view.findViewById(R.id.btnBack);
        btnBackView.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit());
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