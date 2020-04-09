package com.example.puffinradio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private Button aboutButton;
    private Button leaderBoardButton;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference downloadReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseStorage = FirebaseStorage.getInstance();

        aboutButton = (Button)findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent);
            }

        });

        leaderBoardButton = (Button)findViewById(R.id.leaderBoardButton);
        leaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                startActivity(intent);
                download();
            }
        });

    }
    public void download() {
        storageReference = firebaseStorage.getReference();
        downloadReference = storageReference.child("callsigns.txt");
        try {
            final File fileNameOnDevice = new File(getFilesDir(),"callsigns.txt");
            downloadReference.getFile(fileNameOnDevice).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(MainActivity.this, "Successful",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("firebase ", ";local tem file not created");
                }
            });
        } catch (Exception e) {
            //Toast.makeText(MainActivity.this, "Failed to download file: " + e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
