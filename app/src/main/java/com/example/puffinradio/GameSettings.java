package com.example.puffinradio;

import android.content.SharedPreferences;

public class GameSettings {
    private SharedPreferences sharedPrefs;


    public GameSettings(SharedPreferences sharedPreferences) {
        sharedPrefs = sharedPreferences;
    }

    public int getWPM() {
        return Integer.parseInt(sharedPrefs.getString("WPM","20"));
    }

    public void setWPM(int newWPM) {
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putInt("WPM", newWPM);
        editor.commit();
    }

    public int getFrequency() {
        return Integer.parseInt(sharedPrefs.getString("frequency","700"));
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
