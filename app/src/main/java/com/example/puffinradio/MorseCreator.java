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

    static final int sampleRate = 8000;
    private static int freqOfTone; // hz

    private static byte[] generatedDit;
    private static byte[] generatedDah;

    private static boolean hasFarnsworth = false;
    private static double tA;
    private static double tC;
    private static double tW;
    private static int s;//overall speed

    static AudioTrack audioTrack;

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

    public static int playSound(String morse, final double unitLength, double transSpeed, int freq, int overallSpeed, int wpm) {

        freqOfTone = freq;
        if(transSpeed > 40)
            transSpeed = 40;
        if(transSpeed <= 18) {
            transSpeed = 18;
            hasFarnsworth = true;
        }
        tA = findtA(overallSpeed, wpm);
        tC = 3*tA / 19;
        tW = 7*tA / 19;


        int numUnits = 0;
        int indices = 1000;
        for(int i = 0; i < morse.length(); i++) {
            if(morse.charAt(i) == '-') {
                indices += generatedDah.length;
                numUnits += 4;
            } else {
                indices += generatedDit.length;
                numUnits += 2;
            }
            if(hasFarnsworth) {
                byte[] charSpace  = new byte[(int) (2*tC*sampleRate)];
                indices += charSpace.length;
            } else {
                indices += generatedDit.length;
            }

        }
        byte[] morseSound = new byte[indices];
        int morseInd = 1000;
        for(int i = 0; i < morse.length(); i++) {
            if(morse.charAt(i) == '-') {
                System.arraycopy(generatedDah, 0, morseSound, morseInd, generatedDah.length);
                morseInd += generatedDah.length;
            } else if(morse.charAt(i) == '.') {
                System.arraycopy(generatedDit, 0, morseSound, morseInd, generatedDit.length);
                morseInd += generatedDit.length;
            } else {
                morseInd += generatedDit.length;
            }
            morseInd += generatedDit.length;
        }

        playMorse(sampleRate, morseSound, (int) unitLength * numUnits);
        return (int) (unitLength * numUnits);
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
        for (int i = 0; i < numSamples-1; ++i) {
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
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);

        audioTrack.play();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                audioTrack.release();
            }
        }, length);

    }

    private static double findtA(int overAll, int charSpeed){
        return ((60.0*charSpeed - 37.2*overAll) / (overAll * charSpeed));
    }

    public static void setFreqOfTone(int frq) {
        freqOfTone = frq;
    }
}
