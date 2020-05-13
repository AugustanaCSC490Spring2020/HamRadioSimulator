package com.example.puffinradio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
    private static String diff;
    private static boolean competitive;

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
        Intent intent = this.getIntent();
        competitive = intent.getBooleanExtra("competitive", false);
        Spinner difficulty = findViewById(R.id.difficultySpinner);
        TextView diffTextView = findViewById(R.id.difficultyText);
        if(competitive) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulties, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            difficulty.setAdapter(adapter);
            difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    diff = parent.getItemAtPosition(pos).toString();
                    parent.getItemAtPosition(pos);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }

            });
        } else {
            difficulty.setVisibility(View.GONE);
            diffTextView.setVisibility(View.GONE);
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
            CWpref.setDialogMessage("Speed must be between 5-60 WPM");
            CWpref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String s = newValue.toString();
                    if(Integer.parseInt(s) < 5 || Integer.parseInt(s) > 60) {
                        Toast toast = Toast.makeText(getContext(), "Speed must be between 5-60 WPM", Toast.LENGTH_SHORT);
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
    public static String getText(){
        return diff;
    }
    public static boolean getMode(){
        return competitive;
    }
}