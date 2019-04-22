package com.example.orbitalflightpaths;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.Fragment;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment settingsFragment = new SettingsHolder();
        FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null){
            fTransaction.add(R.id.settings_holder, settingsFragment, "settings_screen");
        }
        fTransaction.commit();
    }


    public static class SettingsHolder extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.app_preferences);

        }
    }
}
