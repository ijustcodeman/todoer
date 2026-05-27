package com.example.todo.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

/**
 * Base activity class that provides common functionality for theme management and font scaling.
 */
public abstract class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Initializes the activity, sets the default night mode, and registers a preference change listener.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Unregisters the shared preference change listener when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Attaches the base context and applies font scaling based on user preferences.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(newBase);
        String fontSizeStr = prefs.getString("font_size", "16");
        float scale = Float.parseFloat(fontSizeStr) / 16f;

        Configuration configuration = newBase.getResources().getConfiguration();
        configuration.fontScale = scale;
        Context context = newBase.createConfigurationContext(configuration);
        super.attachBaseContext(context);
    }

    /**
     * Called when a shared preference is changed. Recreates the activity if the font size preference is modified.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("font_size".equals(key)) {
            recreate();
        }
    }
}
