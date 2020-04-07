package com.example.puffinradio;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CallSignActivity extends AppCompatActivity {
    ListView listView;
    private ArrayAdapter adapter;
    private List<String> fileList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsignlist);
        listView = (ListView) findViewById(R.id.callSignList);
        String fileName = getFilesDir().getPath() + "/" + "callsigns.txt";
        File file = new File(fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream inputStream = new DataInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                fileList.add(strLine);
            }
            inputStream.close();
        } catch (Exception e) {
            Toast.makeText(CallSignActivity.this, "Exception", Toast.LENGTH_LONG).show();

        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fileList);
        listView.setAdapter(adapter);
    }
}
