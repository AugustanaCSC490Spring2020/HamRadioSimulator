package com.example.puffinradio;

import android.media.SoundPool;
import android.util.Log;

import android.os.Handler;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MorseCreator {
    static String[] morseCode = {"-----", ".----", "..---", "...--", "....-", ".....", "-....", "--...",
            "---..", "----.", ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---",
            "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-",
            ".--", "-..-", "-.--", "--.."}; //0-9, A-Z

    static Handler handler = new Handler();

    static SoundPool soundPool;
    static int dot, dash;

    public static String createMorse(String callSign) {
        Log.d(TAG, "createMorse: HERE");
        String morse = "";
        for(int i = 0; i < callSign.length(); i++) {
            char letter = callSign.charAt(i);
            Log.d(TAG, "createMorse: " + letter);
            if(letter < 'A') {
                morse += morseCode[Integer.parseInt("" + letter)];
            } else {
                morse += morseCode[letter - 55];
            }
            morse += "/"; //represents two additional units of silence to separate letters
        }
        Log.d(TAG, "createMorse: " + morse);
        return morse;
    }

    public static void playSound(String morse, int unitLength) {
        int timer = 0;
        for(int i = 0; i < morse.length(); i++) {
            if(morse.charAt(i) == '.') {
                dot(timer);
                timer += unitLength * 2;
            } else if(morse.charAt(i) == '-') {
                dash(timer);
                timer += unitLength * 4;
            } else { // morse.charAt(i) == '/'
                rest(timer);
                timer += unitLength * 2;
            }
        }
    }

    public static void initializeMorseCreator(SoundPool soundPoolInit, int dotInit, int dashInit) {
        soundPool = soundPoolInit;
        dot = dotInit;
        dash = dashInit;
    }

    public static void dot(int length) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPool.play(dot, 1, 1, 0, 0, 3);
            }
        }, length);
    }

    public static void dash(int length) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPool.play(dash, 1, 1, 0, 0, 1);
            }
        }, length);
    }

    public static void rest(int length) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //
            }
        }, length);
    }
}
