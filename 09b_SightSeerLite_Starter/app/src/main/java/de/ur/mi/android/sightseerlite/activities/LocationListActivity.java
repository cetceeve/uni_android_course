package de.ur.mi.android.sightseerlite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.ur.mi.android.sightseerlite.R;
import de.ur.mi.android.sightseerlite.adapter.LocationListAdapter;
import de.ur.mi.android.sightseerlite.config.AppConfig;
import de.ur.mi.android.sightseerlite.domain.PointOfInterest;
import de.ur.mi.android.sightseerlite.jsonhelper.JSONParser;


public class LocationListActivity extends AppCompatActivity {

    private LocationListAdapter listAdapter;
    private ArrayList<PointOfInterest> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initUI();
        initLocationData();
    }

    private void initUI() {
        setContentView(R.layout.activity_location_list);
        locations = new ArrayList<>();
        listAdapter = new LocationListAdapter(this, locations);
        ListView listView = (ListView) findViewById(R.id.location_list);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                locationSelected(locations.get(position));
            }
        });
    }

    private void initLocationData() {
        locations.clear();
        locations.addAll(JSONParser.getPOIList(AppConfig.JSONString.JSON_STRING));
        listAdapter.notifyDataSetChanged();
    }

    private void locationSelected(PointOfInterest poi) {
        String json = poi.toJson();
        Intent navigationActivity = new Intent(LocationListActivity.this, NavigationActivity.class);
        navigationActivity.putExtra(AppConfig.INTENT_KEY_JSON_STRING, json);
        startActivity(navigationActivity);
    }

    @Override
    protected void onResume() {
        initLocationData();
        super.onResume();
    }
}
