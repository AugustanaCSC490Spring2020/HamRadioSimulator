package com.example.puffinradio;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GameActivity extends AppCompatActivity {
    private Button settingsButton;
    private Button sendCallSignButton;
    private Button open;
    private TextView timeTextView;
    private TextView scoreNumTextView;
    private EditText guessEditText;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference downloadReference;
    int cwSpeed = 0;
    int time = 0;
    boolean hasStatic = false;
    SoundPool soundPool;
    int dash, dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        open =(Button)findViewById(R.id.load);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }


        settingsButton = (Button)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        dash = soundPool.load(this, R.raw.dash, 1);
        dot = soundPool.load(this, R.raw.dot, 1);
        sendCallSignButton = (Button)findViewById(R.id.sendCallSignButton);
        // Just using the button for testing purposes for now; "hello world"
        sendCallSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helloWorld();
            }
        });

    }

    public void rest(int length) {
        try {
            Thread.sleep(length);
        } catch(InterruptedException e) {
            Log.e("Error", "rest: Interrupted Exception");
        }
    }

    public void dot() {
        try {
            soundPool.play(dot, 1, 1, 0, 0, 3);
            Thread.sleep(666);
        } catch(InterruptedException e) {
            Log.e("Error", "dot: Interrupted Exception");
        }
    }

    public void dash() {
        try {
            soundPool.play(dash, 1, 1, 0, 0, 1);
            Thread.sleep(1333);
        } catch(InterruptedException e) {
            Log.e("Error", "dash: Interrupted Exception");
        }
    }

    // Test: spell out "hello world" in Morse code
    public void helloWorld() {
        dot();
        dot();
        dot();
        dot();
        rest(666);
        dot();
        rest(666);
        dot();
        dash();
        dot();
        dot();
        rest(666);
        dot();
        dash();
        dot();
        dot();
        rest(666);
        dash();
        dash();
        dash();
        rest(2000);
        dot();
        dash();
        dash();
        rest(666);
        dash();
        dash();
        dash();
        rest(666);
        dot();
        dash();
        dot();
        rest(666);
        dot();
        dash();
        dot();
        dot();
        rest(666);
        dash();
        dot();
        dot();
    }
    public void OpenCallSign(View view) {
        startActivity(new Intent(GameActivity.this,CallSignActivity.class));
    }
}
