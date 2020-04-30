package com.example.puffinradio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {
    private Button replayCallSignButton;
    private TextView timeTextView;
    private TextView scoreNumTextView;
    private EditText guessEditText;
    private List<String> callSignList = new ArrayList<String>();
    private CountDownTimer countDownTimer;
    private Button startGameButton;

    SharedPreferences sharedPreferences;
    private long time;
    String callsign = "";
    SoundPool soundPool;
    int staticSound;
    LinkedHashMap<String, Boolean> guesses;
    RecyclerView recyclerView;
    boolean donePlaying = false;
    static Handler handler = new Handler();
    int frq = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        timeTextView = findViewById(R.id.timeTextView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        staticSound = soundPool.load(this, R.raw.staticsound, 1);

        guessEditText = findViewById(R.id.guessEditText);
        scoreNumTextView = findViewById(R.id.scoreNumTextView);

        guessEditText.setEnabled(false);

        recyclerView = findViewById(R.id.recycler);
        guesses = new LinkedHashMap<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        replayCallSignButton = findViewById(R.id.replayCallSignButton);
        replayCallSignButton.setEnabled(false);

        fileToList();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setEnabled(false);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                startGameButton.setEnabled(true);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.autoPause();
        MorseCreator.handler.removeCallbacksAndMessages(null);
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * Get the call sign file from Firebase and add all call signs to a list
     */
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

    //This method is used to get the time input from the settings
    private String getTimePreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        return sharedPreferences.getString("edit_text_preference_2", "5");
    }

    /**
     * Get a random call sign to be played
     *
     * @return the call sign
     */
    private String getRandomCallsign() {
        Random rand = new Random();
        return callSignList.get(rand.nextInt(callSignList.size()));
    }

    //This method is used to start the timer
    private void timer(){
       int minTime  = Integer.parseInt(getTimePreferences()); // changes string input into an int
       time = TimeUnit.MINUTES.toMillis(minTime) + 1000; // Since time input is is in min, this changes it into miliseconds
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished ) {
               time = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                    Intent intent = new Intent(GameActivity.this, EndActivity.class);
                    intent.putExtra("score", scoreNumTextView.getText().toString());
                    startActivity(intent); // when the timer is done it goes to the end activity
                    countDownTimer.cancel();
            }
        }.start();
    }

    /**
     * Displays timer
     */
    private void updateCountDownText(){
        int hour = (int)(time / 1000) / 3600; // changes miliseconds into hours
        int min = (int)((time / 1000) % 3600) / 60; //changes miliseconds into mins
        int sec = (int)(time / 1000) % 60; // changes miliseconds into sec
        String timeLeftFormatted;
        if (hour > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, min, sec); // if the user inputs more than 60 mins
        }else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", min, sec);// if the user inputs less than or equal to 60 mins
        }
        timeTextView.setText(timeLeftFormatted);
    }

    /**
     * Get the call sign from the settings
     *
     * @return the call sign
     */
    private String getUsersCallSign(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        return sharedPreferences.getString("signature",getRandomCallsign());
    }

    /**
     * Get the speed to play the call signs
     *
     * @return the speed
     */
    private String getC() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        return sharedPreferences.getString("WPM", "20");
    }

    private int getFrequency() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        String frequen = sharedPreferences.getString("frequency", "200");
        int freq = Integer.parseInt(frequen);
        return freq;
    }

    /**
     * Calculate the speed of one unit
     *
     * @param C the speed in WPM
     * @return the size of one unit
     */
    private double findCWUnitSize(String C){
        return 1.2/Integer.parseInt(C);
    }

    /**
     * Get whether to play static from settings
     *
     * @return boolean of whether static should play
     */
    private boolean getStatic() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        return sharedPreferences.getBoolean("switch_preference_1", true);
    }

    /**
     * Starts game when start game button is pressed
     *
     * @param v the button
     */
    public void startGame(View v) {
        if(getStatic()) {
            soundPool.play(staticSound, 1, 1, 0, -1, 1);
        }
        timer();
        frq = getFrequency();
        guessEditText.setEnabled(true);
        v.setVisibility(View.INVISIBLE);
        callsign = getRandomCallsign();
        String cw = MorseCreator.createMorse(callsign);
        Log.d("CW: ", "onClick: " + cw);
        String WPM = getC();
        double transmissionSpeed = findCWUnitSize(WPM);
        donePlaying = false;
        replayCallSignButton.setEnabled(false);
        Log.d("FREQ", "startGame: freq is " + frq);
        int length = MorseCreator.playSound(cw, transmissionSpeed * 1000, transmissionSpeed, frq);

        enableReplayButton(length);

        //sets up the guess edit text to play new call sign once user presses enter
        guessEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (donePlaying && keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        i == KeyEvent.KEYCODE_ENTER && !callsign.equals("")) {
                    if (guessEditText.getText().toString().equalsIgnoreCase(callsign)) {
                        Log.d("onkey: ", "onKey: " + Integer.parseInt(scoreNumTextView.getText().toString()));
                        scoreNumTextView.setText(Integer.parseInt(scoreNumTextView.getText().toString()) + 1 + "");
                        guesses.put(callsign, true);
                    } else {
                        guesses.put(callsign, false);
                    }
                    guessEditText.setText("");
                    callsign = getRandomCallsign();
                    String cw = MorseCreator.createMorse(callsign); //test
                    Log.d("CW: ", "onClick: " + cw);
                    String WPM = getC();
                    double transmissionSpeed = findCWUnitSize(WPM);
                    donePlaying = false;
                    replayCallSignButton.setEnabled(false);
                    int length = MorseCreator.playSound(cw, transmissionSpeed * 1000, transmissionSpeed, frq);

                    enableReplayButton(length);

                    RVAdapter adapter = new RVAdapter(guesses);
                    recyclerView.setAdapter(adapter);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Replays call sign when button is pressed
     *
     * @param v the button
     */
    public void replayCallSign(View v) {
        String cw = MorseCreator.createMorse(callsign); //test
        Log.d("CW: ", "onClick: " + cw);
        String WPM = getC();
        double transmissionSpeed = findCWUnitSize(WPM);

        donePlaying = false;
        replayCallSignButton.setEnabled(false);
        int length = MorseCreator.playSound(cw, transmissionSpeed * 1000, transmissionSpeed, frq);

        enableReplayButton(length);
    }

    /**
     * Enable replay button after sound finishes
     *
     * @param length of the call sign (in milliseconds)
     */
    public void enableReplayButton(int length) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                donePlaying = true;
                replayCallSignButton.setEnabled(true);
            }
        }, length);
    }


}


