package com.example.puffinradio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private Button settingsButton;
    private Button sendCallSignButton;
    private TextView timeTextView;
    private TextView scoreNumTextView;
    private EditText guessEditText;
    int cwSpeed = 0;
    int time = 0;
    boolean hasStatic = false;
    String callsign = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        settingsButton = (Button)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }


}
