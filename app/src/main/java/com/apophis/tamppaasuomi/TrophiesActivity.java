package com.apophis.tamppaasuomi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TrophiesActivity extends AppCompatActivity {
    public static final String TAG = "TROPHY";
    private ArrayList<String> trophyNames = new ArrayList<>();
    private ArrayList<Trophy.TrophyItem> trophies;
    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophies);

        trophies = Trophy.getCompleted();

        for (Trophy.TrophyItem ti : trophies) {
            trophyNames.add(Trophy.getAsString(this.getResources(), ti, false));
        }

        itemsAdapter = new ArrayAdapter<String>(this, R.layout.trophy_list_item, trophyNames);

        final ListView trophyList;
        trophyList = findViewById(R.id.trophyList);
        trophyList.setAdapter(itemsAdapter);

        trophyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                Trophy.TrophyItem ti = trophies.get(position);
                if (ti != null && ti.type == Trophy.TrophyType.LOCATION) {
                    Intent resultIntent = new Intent();
                    int[] coords = {ti.x, ti.y};
                    resultIntent.putExtra("coords", coords);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}
