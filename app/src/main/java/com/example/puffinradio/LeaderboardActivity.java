package com.example.puffinradio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final TextView[] callSigns = new TextView[10];
        callSigns[0] = (TextView)findViewById(R.id.callSign1);
        callSigns[1] = (TextView)findViewById(R.id.callSign2);
        callSigns[2] = (TextView)findViewById(R.id.callSign3);
        callSigns[3] = (TextView)findViewById(R.id.callSign4);
        callSigns[4] = (TextView)findViewById(R.id.callSign5);
        callSigns[5] = (TextView)findViewById(R.id.callSign6);
        callSigns[6] = (TextView)findViewById(R.id.callSign7);
        callSigns[7] = (TextView)findViewById(R.id.callSign8);
        callSigns[8] = (TextView)findViewById(R.id.callSign9);
        callSigns[9] = (TextView)findViewById(R.id.callSign10);

        final TextView[] scores = new TextView[10];
        scores[0] = (TextView)findViewById(R.id.score1);
        scores[1] = (TextView)findViewById(R.id.score2);
        scores[2] = (TextView)findViewById(R.id.score3);
        scores[3] = (TextView)findViewById(R.id.score4);
        scores[4] = (TextView)findViewById(R.id.score5);
        scores[5] = (TextView)findViewById(R.id.score6);
        scores[6] = (TextView)findViewById(R.id.score7);
        scores[7] = (TextView)findViewById(R.id.score8);
        scores[8] = (TextView)findViewById(R.id.score9);
        scores[9] = (TextView)findViewById(R.id.score10);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    callSigns[counter].setText(snapshot.getKey());
                    scores[counter].setText(snapshot.getValue().toString());
                    counter++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error: ", "Database error");
            }
        });
    }
}
