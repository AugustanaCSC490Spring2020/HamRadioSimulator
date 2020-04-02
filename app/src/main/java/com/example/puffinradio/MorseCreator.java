package com.example.puffinradio;

import android.media.SoundPool;
import android.util.Log;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MorseCreator {
    static String[] morseCode = {"-----", ".----", "..---", "...--", "....-", ".....", "-....", "--...",
            "---..", "----.", ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---",
            "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-",
            ".--", "-..-", "-.--", "--.."};

    public static String createMorse(String callSign) {
        Log.d(TAG, "createMorse: HERE");
        String morse = "";
        for(int i = 0; i < callSign.length(); i++) {
            char letter = callSign.charAt(i);
            Log.d(TAG, "createMorse: " + letter);
            if(letter < 'A') {
                morse += morseCode[Integer.parseInt("" + letter)];
            } else {
                morse += morseCode[letter - 32];
            }
            morse += "/"; //represents two additional units of silence to separate letters
        }
        Log.d(TAG, "createMorse: " + morse);
        return morse;
    }

    public static void playSound(SoundPool soundPool, String morse, int dotSound, int dashSound, int unitLength) {
        for(int i = 0; i < morse.length(); i++) {
            if(morse.charAt(i) == '.') {
                dot(soundPool, dotSound, unitLength);
            } else if(morse.charAt(i) == '-') {
                dash(soundPool, dashSound, unitLength);
            } else { // morse.charAt(i) == '/'
                rest(unitLength);
            }
        }
    }

    //TODO: change from thread.sleep to scheduled executor
    public static void dot(SoundPool soundPool, int dot, int length) {
        try {
            soundPool.play(dot, 1, 1, 0, 0, 3);
            Thread.sleep(length * 2);
        } catch(InterruptedException e) {
            Log.e("Error", "dot: Interrupted Exception");
        }
    }

    public static void dash(SoundPool soundPool, int dash, int length) {
        try {
            soundPool.play(dash, 1, 1, 0, 0, 1);
            Thread.sleep(length * 4);
        } catch(InterruptedException e) {
            Log.e("Error", "dash: Interrupted Exception");
        }
    }

    public static void rest(int length) {
        try {
            Thread.sleep(length * 2);
        } catch(InterruptedException e) {
            Log.e("Error", "rest: Interrupted Exception");
        }
    }
}
