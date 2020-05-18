package com.example.puffinradio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    private GameSettings gameSettings ;
    /**
     * This creates the activity and creates the settings
     * @param savedInstanceState returns the last thing the user inputs
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gameSettings = new GameSettings(PreferenceManager.getDefaultSharedPreferences(this));
        FrameLayout l = findViewById(R.id.settings);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    /**
     * This sets up the Settings and its fragments using preferences
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            final androidx.preference.EditTextPreference CWpref = getPreferenceManager().findPreference("WPM");
            CWpref.setDialogMessage("Speed must be between 5-40 WPM");
            CWpref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String s = newValue.toString();
                    if(Integer.parseInt(s) < 5 || Integer.parseInt(s) > 40) {
                        Toast toast = Toast.makeText(getContext(), "Speed must be between 5-40 WPM", Toast.LENGTH_SHORT);
                        toast.show();
                        return false;
                    }
                    return true;
                }
            });

            CWpref.setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener(){
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
            });

            androidx.preference.EditTextPreference frqPref = getPreferenceManager().findPreference("frequency");
            frqPref.setDialogMessage("Frequency must be between 500-800 Hz");

            frqPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String s = newValue.toString();
                    if(Integer.parseInt(s) < 500 || Integer.parseInt(s) > 800) {
                        Toast toast = Toast.makeText(getContext(), "Frequency must be between 500-800 Hz", Toast.LENGTH_SHORT);
                        toast.show();
                        return false;
                    }
                    return true;
                }
            });

            frqPref.setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener(){
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
            });

            androidx.preference.EditTextPreference timePref = getPreferenceManager().findPreference("edit_text_preference_2");
            timePref.setDialogMessage("Time must be between 1-15 minutes");

            timePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String s = newValue.toString();
                    if(Integer.parseInt(s) < 1 || Integer.parseInt(s) > 15) {
                        Toast toast = Toast.makeText(getContext(), "Time must be between 1-15 minutes", Toast.LENGTH_SHORT);
                        toast.show();
                        return false;
                    }
                    return true;
                }
            });

            timePref.setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener(){
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
            });
        }

    }

    /**
     * This opens the game activity after the user hits play
     * @param view The Button
     */
    public void openGameActivity(View view){
        startActivity(new Intent(SettingsActivity.this, GameActivity.class));
    }
}