package net.augustana.puffinradio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.augustana.puffinradio.R;

public class EndActivity extends AppCompatActivity {
    TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        scoreView = findViewById(R.id.scoreTextView);
        String score = this.getIntent().getIntExtra("score", 0) + "";
        scoreView.setText(score);
    }

    /**
     * Open the activity that corresponds to the button pushed
     * @param v the button pushed
     */
    public void openActivity(View v) {
        Intent intent;
        if(v.getId() == R.id.endLeaderboardButton) {
            intent = new Intent(getBaseContext(), LeaderboardActivity.class);
        } else { //mainMenuButton
            intent = new Intent(getBaseContext(), MainActivity.class);
        }
        startActivity(intent);
    }
}
