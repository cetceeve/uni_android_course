package de.ur.mi.android.sightseerlite.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import de.ur.mi.android.sightseerlite.R;
import de.ur.mi.android.sightseerlite.config.AppConfig;
import de.ur.mi.android.sightseerlite.domain.PointOfInterest;
import de.ur.mi.android.sightseerlite.jsonhelper.JSONParser;
import de.ur.mi.android.sightseerlite.navigation.NavigationController;
import de.ur.mi.android.sightseerlite.navigation.NavigationDetail;

public class NavigationActivity extends Activity implements NavigationController.NavigationListener {

    private TextView navigation_title;
    private TextView navigation_distance;
    private ImageView navigation_compass;
    private PointOfInterest target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startNavigation();
    }

    private void init() {
        initUI();
        initNavigation();
    }

    private void initUI() {
        setContentView(R.layout.activity_navigation);
        navigation_title = (TextView) findViewById(R.id.navigation_title);
        navigation_distance = (TextView) findViewById(R.id.navigation_distance);
        navigation_compass = (ImageView) findViewById(R.id.compass_view);
    }

    private void initNavigation() {
        NavigationController.getInstance(this).setNavigationListener(this);
    }

    private void startNavigation() {
        String jsonTarget = getIntent().getStringExtra(
                AppConfig.INTENT_KEY_JSON_STRING);
        try {
            JSONObject obj = new JSONObject(jsonTarget);
            target = JSONParser.getPOI(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (target != null) {
            NavigationController.getInstance(this).setTarget(target);
            NavigationController.getInstance(this).startNavigation();
        }
    }

    private void updateUI(String title, float distance, float bearing) {
        navigation_title.setText(title);
        navigation_distance.setText(Math.round(distance) + "m");
        rotateCompass(bearing);
    }

    private void rotateCompass(float bearing) {
        navigation_compass.setRotation(0);
        navigation_compass.setRotation(bearing);
    }

    @Override
    public void onNavigationDetailChanged(NavigationDetail navDetail) {
        updateUI(navDetail.getTitle(), navDetail.getDistance(), navDetail.getBearing());
    }

    @Override
    public void onSignalFound() {
        TextView gps_status = (TextView) findViewById(R.id.navigation_signal_info);
        gps_status.setText(getResources().getString(R.string.navigation_signal_info_connected));
        navigation_compass.setImageResource(R.drawable.compass);
    }

    @Override
    public void onSignalLost() {
        TextView gps_status = (TextView) findViewById(R.id.navigation_signal_info);
        gps_status.setText(getResources().getString(R.string.navigation_signal_info_searching));
        navigation_compass.setImageResource(R.drawable.compass_no_gps);
    }

    @Override
    protected void onPause() {
        NavigationController.getInstance(this).stopNavigation();
        super.onPause();
    }

}
