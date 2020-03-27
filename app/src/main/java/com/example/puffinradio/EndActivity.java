package com.example.puffinradio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    Button mainButton;
    Button leaderboardButton;
    TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        mainButton = (Button)findViewById(R.id.mainMenuButton);
        leaderboardButton = (Button)findViewById(R.id.endLeaderboardButton);
        scoreView = (TextView)findViewById(R.id.scoreTextView);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LeaderboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
