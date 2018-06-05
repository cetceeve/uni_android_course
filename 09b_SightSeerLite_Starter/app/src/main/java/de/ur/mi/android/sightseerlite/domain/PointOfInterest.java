package de.ur.mi.android.sightseerlite.domain;

import android.location.Location;
import android.location.LocationManager;

import de.ur.mi.android.sightseerlite.config.AppConfig;

public class PointOfInterest {
    private String title;
    private double latitude;
    private double longitude;
    private double altitude;

    public PointOfInterest(String title, double latitude, double longitude, double altitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public String getTitle() {
        return title;
    }

    public Location getLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setAltitude(altitude);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public String toJson() {
        return "{\"" + AppConfig.TITLE_KEY + "\":\"" + title + "\",\"" + AppConfig.LATITUDE_KEY + "\":" + latitude + ",\"" + AppConfig.LONGITUDE_KEY + "\":" + longitude + ",\"" + AppConfig.ALTITUDE_KEY + "\":" + altitude + "}";
    }
}
