package com.example.puffinradio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private boolean competitive = false;

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
            intent.putExtra("competitive", competitive);
            startActivity(intent);
        } else if(v.getId() == R.id.leaderBoardButton) {
            intent = new Intent(getBaseContext(), LeaderboardActivity.class);
            intent.putExtra("competitive", competitive);
            startActivity(intent);
        } else {
            registerForContextMenu(v);
            openContextMenu(v);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mode, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.competitive) {
            competitive = true;
        }
        Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
        intent.putExtra("competitive", competitive);
        startActivity(intent);
        return true;
    }
}
