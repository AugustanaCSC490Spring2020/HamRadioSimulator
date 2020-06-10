package net.augustana.puffinradio;

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


    /**
     * Get whether to play static from settings
     *
     * @return boolean of whether static should play
     */
    public boolean getStatic() {
        return sharedPrefs.getBoolean("switch_preference_1", true);
    }

    /**
     * Get the call sign from the settings
     *
     * @return the call sign
     */
    public String getUsersCallSign(){
        return sharedPrefs.getString("signature",CallSignLibrary.getRandomCallsign());
    }


    //This method is used to get the time input from the settings
    public String getTimePreferences(){
        return sharedPrefs.getString("edit_text_preference_2", "5");
    }

    public int getOverallSpeed() {
        return Integer.parseInt(sharedPrefs.getString("overall_speed", "5"));
    }

    public boolean getFarnsworth() {
        return sharedPrefs.getBoolean("hasFarnsworth", false);
    }
}
