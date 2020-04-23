package com.example.puffinradio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.util.Log;

import android.os.Handler;

import androidx.annotation.LongDef;

import static androidx.constraintlayout.widget.Constraints.TAG;
// originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
// and modified by Steve Pomeroy <steve@staticfree.info>
public class MorseCreator {
    static String[] morseCode = {"-----", ".----", "..---", "...--", "....-", ".....", "-....", "--...",
            "---..", "----.", ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---",
            "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-",
            ".--", "-..-", "-.--", "--.."}; //0-9, A-Z

    static Handler handler = new Handler();

    static SoundPool soundPool;
    static int dot, dash;
    static int wpm;

    static final int sampleRate = 8000;
    private static final double freqOfTone = 200; // hz

    static double  unit;
    static double  c;//wpm
    double soundLength;

    private static byte[] generatedDit;
    private static byte[] generatedDah;


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

    public static int playSound(String morse, final double unitLength, double transSpeed) {
        int timer = 0;

        genDah(transSpeed);
        genDit(transSpeed);
        for(int i = 0; i < morse.length(); i++) {
            if(morse.charAt(i) == '.') {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playMorse(sampleRate, generatedDit, (int)unitLength);
                    }
                }, timer);
                timer += unitLength * 2;
            } else if(morse.charAt(i) == '-') {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playMorse(sampleRate, generatedDah, (int)unitLength*3);
                    }
                },timer);

                timer += unitLength * 4;
            } else { // morse.charAt(i) == '/'
                rest(timer);
                timer += unitLength * 2;
            }
        }
        return timer;
    }

    static void genDah(double u) {
        //Log.d(TAG, "genDah:" + unitLength);
        final double duration = u*3.0;//in sec

        final double numSamples = duration * sampleRate;
        final double sample[] = new double[(int) numSamples];

        generatedDah = new byte[2 *  (int) numSamples];

        // fill out the array
        for (int i = 0; i < numSamples - 1; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;

        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedDah[idx++] = (byte) (val & 0x00ff);
            generatedDah[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }
    static void genDit(double u){

        final double duration = u;//in sec

        final double numSamples = duration * sampleRate;
        final double sample[] = new double[(int) numSamples];

        generatedDit =  new byte[2 * (int)numSamples];



        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;

        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedDit[idx++] = (byte) (val & 0x00ff);
            generatedDit[idx++] = (byte) ((val & 0xff00) >>> 8);

        }

    }

    static void playMorse(int sampleRate, byte[] generatedSnd, int length){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);

            audioTrack.play();
        try {
            Thread.sleep((int) length);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioTrack.release();

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
