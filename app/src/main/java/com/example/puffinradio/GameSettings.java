package com.example.puffinradio;

import android.content.SharedPreferences;

public class GameSettings {
    private SharedPreferences sharedPrefs;


    public GameSettings(SharedPreferences sharedPreferences) {
        sharedPrefs = sharedPreferences;
    }

    public int getWPM() {
        return sharedPrefs.getInt("WPM", 5);
    }

    public void setWPM(int newWPM) {
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putInt("WPM", newWPM);
        editor.commit();
    }

    public int getFrequency() {
        return sharedPrefs.getInt("Freq", 400);
    }

    /**
     * Calculate the speed of one unit
     *
     *
     * @return the size of one unit
     */
    public double getCWUnitSize(){
        return 1.2/getWPM();
    }
}
