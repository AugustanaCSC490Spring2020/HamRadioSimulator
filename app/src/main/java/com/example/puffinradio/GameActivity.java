package com.example.puffinradio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {
    private Button replayCallSignButton;
    private TextView timeTextView;
    private TextView scoreNumTextView;
    private EditText guessEditText;
    private CountDownTimer countDownTimer;
    private Button startGameButton;
    private DatabaseReference reference;
    SharedPreferences sharedPreferences;
    private long time;
    String callsign = "";
    SoundPool soundPool;
    int staticSound;
    LinkedHashMap<String, String> guesses;
    RecyclerView recyclerView;
    boolean donePlaying = false;
    static Handler handler = new Handler();
    int frq;
    double WPM;
    int highScore = 0;

    double transmissionSpeed;
    int overallSpeed;


    private GameSettings gameSettings;
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
        gameSettings = new GameSettings(PreferenceManager.getDefaultSharedPreferences(this));
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

        transmissionSpeed = gameSettings.getCWUnitSize();
        overallSpeed = gameSettings.getOverallSpeed();
        frq = gameSettings.getFrequency();
        MorseCreator.setFreqOfTone(frq);
        MorseCreator.genDah(transmissionSpeed);
        MorseCreator.genDit(transmissionSpeed);
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





    //This method is used to start the timer
    private void timer(){
        int minTime  = Integer.parseInt(gameSettings.getTimePreferences()); // changes string input into an int
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
                intent.putExtra("score", Integer.parseInt(scoreNumTextView.getText().toString()) * (int) WPM);
                Log.d("spd", "onFinish: " + Integer.parseInt(scoreNumTextView.getText().toString()) * WPM + " " + WPM);
                startActivity(intent); // when the timer is done it goes to the end activity
                sendScore();
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
     * Starts game when start game button is pressed
     *
     * @param v the button
     */
    public void startGame(View v) {
        if(gameSettings.getStatic()) {
            soundPool.play(staticSound, 1, 1, 0, -1, 1);
        }
        timer();
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        guessEditText.setEnabled(true);
        guessEditText.requestFocus();
        v.setVisibility(View.INVISIBLE);
        callsign = CallSignLibrary.getRandomCallsign();
        String cw = MorseCreator.createMorse(callsign);
        Log.d("CW: ", "onClick: " + cw);
        WPM = gameSettings.getWPM();
        double transmissionSpeed = gameSettings.getCWUnitSize();
        donePlaying = false;
        replayCallSignButton.setEnabled(false);
        Log.d("FREQ", "startGame: freq is " + frq);
        int length = MorseCreator.playSound(cw, transmissionSpeed * 1000, transmissionSpeed, frq,overallSpeed,gameSettings.getWPM(), gameSettings.getFarnsworth());
        final double speed = transmissionSpeed;

        enableReplayButton(length);

        guessEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    if(donePlaying && !callsign.equals("")) {
                        String userGuess = guessEditText.getText().toString();
                        guesses.put(callsign, userGuess);

                        if (userGuess.equalsIgnoreCase(callsign)) {
                            Log.d("onkey: ", "onKey: " + Integer.parseInt(scoreNumTextView.getText().toString()));
                            scoreNumTextView.setText(Integer.parseInt(scoreNumTextView.getText().toString()) + 1 + "");
                        }
                        guessEditText.setText("");
                        callsign = CallSignLibrary.getRandomCallsign();
                        String cw = MorseCreator.createMorse(callsign); //test
                        Log.d("CW: ", "onClick: " + cw);
                        donePlaying = false;
                        replayCallSignButton.setEnabled(false);
                        int length = MorseCreator.playSound(cw, speed * 1000, speed, frq,overallSpeed,gameSettings.getWPM(),gameSettings.getFarnsworth());

                        enableReplayButton(length);
                        RVAdapter adapter = new RVAdapter(guesses);
                        recyclerView.setAdapter(adapter);
                    }
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

        donePlaying = false;
        replayCallSignButton.setEnabled(false);
        int length = MorseCreator.playSound(cw, transmissionSpeed * 1000, transmissionSpeed, frq,overallSpeed,gameSettings.getWPM(), gameSettings.getFarnsworth());

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

    /**  This sets the highest score of the user based on what difficulty was chosen.
     *
     */
    private void sendScore(){
            reference = FirebaseDatabase.getInstance().getReference().push().child(gameSettings.getUsersCallSign());
            int userScore = Integer.parseInt(scoreNumTextView.getText().toString() )* (int) WPM;
            reference.setValue(userScore);
    }

}
