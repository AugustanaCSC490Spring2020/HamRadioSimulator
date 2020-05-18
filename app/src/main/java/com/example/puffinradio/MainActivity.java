package com.example.puffinradio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference downloadReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CallSignLibrary.download(getFilesDir());
    }



    /**
     * Open whichever activity corresponds to the button pushed
     *
     * @param v the button pushed
     */
    public void openActivity(View v) {
        Intent intent;
        if(v.getId() == R.id.aboutButton) {
            intent = new Intent(getBaseContext(), AboutActivity.class);
        } else if(v.getId() == R.id.leaderBoardButton) {
            intent = new Intent(getBaseContext(), LeaderboardActivity.class);
        } else {
            intent = new Intent(getBaseContext(), SettingsActivity.class);
        }
        startActivity(intent);
    }

}
