package net.augustana.puffinradio;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.augustana.puffinradio.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference downloadReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CallSignLibrary.download(getFilesDir(), getResources());
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
