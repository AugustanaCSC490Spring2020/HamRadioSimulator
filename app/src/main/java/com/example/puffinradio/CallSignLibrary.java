package com.example.puffinradio;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CallSignLibrary {
    private static final String CALLSIGNS_FILE_NAME = "callsigns.txt";

    private static List<String> callsigns = new ArrayList<>();
    private static Random rand = new Random();

    /**
     * Pull the call signs from Firebase
     */
    public static void download(final File filesDir, Resources res) {
        InputStream localStream = res.openRawResource(R.raw.callsigns);
        fileToList(localStream);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference downloadReference = storageReference.child(CALLSIGNS_FILE_NAME);
        try {
            final File fileNameOnDevice = new File(filesDir,CALLSIGNS_FILE_NAME);
            downloadReference.getFile(fileNameOnDevice).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("race c", "fileToList: ");
                    String fileName = filesDir.getPath() + "/" + CALLSIGNS_FILE_NAME;
                    File file = new File(fileName);
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        fileToList(fileInputStream);
                    } catch(FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("firebase ", ";local tem file not created");
                }
            });
        } catch (Exception e) {
            //Toast.makeText(MainActivity.this, "Failed to download file: " + e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Get the call sign file from Firebase and add all call signs to a list
     */
    private static void fileToList(InputStream stream){
        try {
            DataInputStream inputStream = new DataInputStream(stream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            ArrayList<String> tempList = new ArrayList<>();
            while ((strLine = bufferedReader.readLine()) != null) {
                tempList.add(strLine);
            }
            inputStream.close();
            callsigns = tempList;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static List<String> getList() {
        return callsigns;
    }

    /**
     * Get a random call sign to be played
     *
     * @return the call sign
     */
    public static String getRandomCallsign() {
        //to avoid a race condition where callsigns is replaced from firebase between the .size and .get calls
        List<String> tempCallSigns = callsigns;
        return tempCallSigns.get(rand.nextInt(tempCallSigns.size()));
    }



}
