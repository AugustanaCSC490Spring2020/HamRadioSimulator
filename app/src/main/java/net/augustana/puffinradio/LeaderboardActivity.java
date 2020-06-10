package net.augustana.puffinradio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import net.augustana.puffinradio.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class LeaderboardActivity extends AppCompatActivity{

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

        final DatabaseReference myRef = database.getReference();
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
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                ArrayList<HighScore> scoreList = new ArrayList<>();

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    String key = next.getKey();
                    Iterable<DataSnapshot> data = next.getChildren();

                    //get all the children of both events and users, and then put them in their respective lists
                    for(DataSnapshot item : data) {
                        scoreList.add(new HighScore(key, item.getKey(), ((Long) item.getValue()).intValue()));
                    }
                }

                Collections.sort(scoreList);

                if(scoreList.size() > 10) {
                    for(int i = scoreList.size() - 1; i >= 10; i--) {
                        HighScore score = scoreList.get(i);
                        myRef.child(score.getKey()).removeValue();
                        scoreList.remove(i);
                    }
                }

                for(int i = 0; i < scoreList.size(); i++) {
                    callSigns[i].setText(scoreList.get(i).getCallSign());
                    scores[i].setText(scoreList.get(i).getScore() + "");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error: ", "Database error");
            }
        });
    }
}
