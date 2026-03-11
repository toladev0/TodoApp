package com.example.todoapp;

import android.content.Context;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

public class DataStoreManager {
    private static RxDataStore<Preferences> instance;

    public static synchronized RxDataStore<Preferences> getInstance(Context context) {
        if (instance == null) {
            instance = new RxPreferenceDataStoreBuilder(context.getApplicationContext(), "tasks").build();
        }
        return instance;
    }
}
