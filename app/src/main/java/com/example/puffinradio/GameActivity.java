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
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private Button settingsButton;
    private Button sendCallSignButton;
    private TextView timeTextView;
    private TextView scoreNumTextView;
    private EditText guessEditText;
    private List<String> callSignList = new ArrayList<String>();
    int cwSpeed = 0;
    int time = 0;
    boolean hasStatic = false;
    String callsign = "";
    SoundPool soundPool;
    int dash, dot; //http://onlinetonegenerator.com/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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


        settingsButton = (Button) findViewById(R.id.settingsButton);
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
        MorseCreator.initializeMorseCreator(soundPool, dot, dash);
        // Just using the button for testing purposes for now; "hello world"
        sendCallSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cw = MorseCreator.createMorse("123ABC"); //test
                Log.d("CW: ", "onClick: " + cw);
                MorseCreator.playSound(cw, 333);
            }
        });
        fileToList();
    }
    private void fileToList(){
        String fileName = getFilesDir().getPath() + "/" + "callsigns.txt";
        File file = new File(fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream inputStream = new DataInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                callSignList.add(strLine);
            }
            inputStream.close();
        } catch (Exception e) {
            Toast.makeText(GameActivity.this, "Failed", Toast.LENGTH_LONG).show();

        }
    }

    private String getRandomCallsign() {
        Random rand = new Random();

        String randomCallsign = callSignList.get(rand.nextInt(callSignList.size()));

        return randomCallsign;
    }
}


