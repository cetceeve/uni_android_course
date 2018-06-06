package de.ur.mi.android.sightseerlite.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import de.ur.mi.android.sightseerlite.config.AppConfig;
import de.ur.mi.android.sightseerlite.domain.PointOfInterest;

public class NavigationController implements LocationListener {

    private static NavigationController mInstance;
    private LocationManager locationManager;
    private String bestProvider;
    private Location location;
    private PointOfInterest pointOfInterest;
    private NavigationListener navigationListener;

    public static NavigationController getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new NavigationController(ctx);
        }
        return mInstance;
    }

    private NavigationController(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        setBestProvider();
        location = locationManager.getLastKnownLocation(bestProvider);
    }

    private void setBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setBearingRequired(true);
        bestProvider = locationManager.getBestProvider(criteria, true);
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    public void setTarget(PointOfInterest poi) {
        pointOfInterest = poi;
        NavigationDetail navigationDetail = getNavigationDetail();
        navigationListener.onNavigationDetailChanged(navigationDetail);
    }

    private NavigationDetail getNavigationDetail() {
        float distance = location.distanceTo(pointOfInterest.getLocation());
        float bearing = location.bearingTo(pointOfInterest.getLocation());
        return new NavigationDetail("toPOI", distance, bearing);
    }

    @SuppressLint("MissingPermission")
    public void startNavigation() {
        locationManager.requestLocationUpdates(bestProvider, AppConfig.LOCATION_UPDATE_INTERVAL, AppConfig.LOCATION_DISTANCE_THRESHOLD, this);
    }

    public void stopNavigation() {
        locationManager.removeUpdates(this);
    }

    public float getEstimatedDistanceForLocation(Location location) {
        return location.distanceTo(this.location);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        if (navigationListener != null) {
            navigationListener.onNavigationDetailChanged(getNavigationDetail());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Hier keine Implementierung notwendig
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Hier keine Implementierung notwendig
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Hier keine Implementierung notwendig
    }

    public interface NavigationListener {
        public void onSignalFound();

        public void onSignalLost();

        public void onNavigationDetailChanged(NavigationDetail navDetail);
    }
}
