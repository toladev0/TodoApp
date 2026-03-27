package com.example.todoapp.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.todoapp.R;
import com.example.todoapp.views.Fragments.AddFragment;
import com.example.todoapp.views.Fragments.CalendarFragment;
import com.example.todoapp.views.Fragments.DocumentsFragment;
import com.example.todoapp.views.Fragments.HomeFragment;
import com.example.todoapp.views.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_cal) {
                loadFragment(new CalendarFragment());
                return true;
            } else if (itemId == R.id.nav_add) {
                loadFragment(new AddFragment());
                return true;
            } else if (itemId == R.id.nav_doc) {
                loadFragment(new DocumentsFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}