package com.example.puffinradio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LeaderboardActivity extends AppCompatActivity{
   private String difficulty = "Easy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
     if (difficulty == null) {
            difficulty = "Easy";
        }
        final DatabaseReference myRef = database.getReference().child(difficulty);
        final HashMap<String, Integer> callSignScores = new HashMap<String, Integer>();

        final TextView[] callSigns = new TextView[10];
        callSigns[0] = (TextView) findViewById(R.id.callSign1);
        callSigns[1] = (TextView) findViewById(R.id.callSign2);
        callSigns[2] = (TextView) findViewById(R.id.callSign3);
        callSigns[3] = (TextView) findViewById(R.id.callSign4);
        callSigns[4] = (TextView) findViewById(R.id.callSign5);
        callSigns[5] = (TextView) findViewById(R.id.callSign6);
        callSigns[6] = (TextView) findViewById(R.id.callSign7);
        callSigns[7] = (TextView) findViewById(R.id.callSign8);
        callSigns[8] = (TextView) findViewById(R.id.callSign9);
        callSigns[9] = (TextView) findViewById(R.id.callSign10);

        final TextView[] scores = new TextView[10];
        scores[0] = (TextView) findViewById(R.id.score1);
        scores[1] = (TextView) findViewById(R.id.score2);
        scores[2] = (TextView) findViewById(R.id.score3);
        scores[3] = (TextView) findViewById(R.id.score4);
        scores[4] = (TextView) findViewById(R.id.score5);
        scores[5] = (TextView) findViewById(R.id.score6);
        scores[6] = (TextView) findViewById(R.id.score7);
        scores[7] = (TextView) findViewById(R.id.score8);
        scores[8] = (TextView) findViewById(R.id.score9);
        scores[9] = (TextView) findViewById(R.id.score10);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    callSignScores.put(snapshot.getKey().toUpperCase(), Integer.parseInt(snapshot.getValue().toString()));
                }
                Set<Map.Entry<String, Integer>> entries = callSignScores.entrySet();

                Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> valOne, Map.Entry<String, Integer> valTwo) {
                        Integer scoreOne = valOne.getValue();
                        Integer scoreTwo = valTwo.getValue();
                        return scoreTwo.compareTo(scoreOne);
                    }
                };
                List<Map.Entry<String, Integer>> listOfEntries = new ArrayList<Map.Entry<String, Integer>>(entries);
                Collections.sort(listOfEntries, valueComparator);
                LinkedHashMap<String, Integer> sortedByValue = new LinkedHashMap<String, Integer>(listOfEntries.size());
                for (Map.Entry<String, Integer> entry : listOfEntries) {
                    sortedByValue.put(entry.getKey(), entry.getValue());
                }
                Set<Map.Entry<String, Integer>> entrySetSortedByValue = sortedByValue.entrySet();
                for (Map.Entry<String, Integer> mapping : entrySetSortedByValue) {
                    if (counter <= 9) {
                        callSigns[counter].setText(mapping.getKey().toUpperCase());
                        scores[counter].setText(mapping.getValue().toString());
                        counter++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error: ", "Database error");
            }
        });
    }
}
