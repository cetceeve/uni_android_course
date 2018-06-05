package de.ur.mi.android.sightseerlite.navigation;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import de.ur.mi.android.sightseerlite.config.AppConfig;
import de.ur.mi.android.sightseerlite.domain.PointOfInterest;

public class NavigationController implements LocationListener {

    private static NavigationController mInstance;

    public static NavigationController getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new NavigationController(ctx);
        }
        return mInstance;
    }

    private NavigationController(Context context) {

    }

    public void setNavigationListener(NavigationListener navigationListener) {

    }

    public void setTarget(PointOfInterest poi) {

    }

    public void startNavigation() {

    }

    public void stopNavigation() {
    }

    public float getEstimatedDistanceForLocation(Location location) {
        return -1;
    }

    @Override
    public void onLocationChanged(Location location) {

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
