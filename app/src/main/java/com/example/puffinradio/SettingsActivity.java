package com.example.puffinradio;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
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
    }


    /**
     * This sets up the Settings and its fragments using preferences
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            androidx.preference.EditTextPreference CWpref = getPreferenceManager().findPreference("WPM");
            CWpref.setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener(){
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
            });

            androidx.preference.EditTextPreference frqPref = getPreferenceManager().findPreference("frequency");
            frqPref.setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener(){
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
            });

            androidx.preference.EditTextPreference timePref = getPreferenceManager().findPreference("edit_text_preference_2");
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