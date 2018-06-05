package de.ur.mi.android.sightseerlite.jsonhelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.ur.mi.android.sightseerlite.config.AppConfig;
import de.ur.mi.android.sightseerlite.domain.PointOfInterest;

public class JSONParser {

    public static ArrayList<PointOfInterest> getPOIList(String JSONArrayString) {
        ArrayList<PointOfInterest> points = new ArrayList<PointOfInterest>();

        try {
            JSONArray jsonArray = new JSONArray(JSONArrayString);
            for (int idx = 0; idx < jsonArray.length(); idx++) {
                JSONObject jsonObject = jsonArray.getJSONObject(idx);
                points.add(getPOI(jsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return points;
    }

    public static PointOfInterest getPOI(JSONObject jsonObject) throws JSONException {
        String title = jsonObject.getString(AppConfig.TITLE_KEY);
        double latitude = jsonObject.getDouble(AppConfig.LATITUDE_KEY);
        double longitude = jsonObject.getDouble(AppConfig.LONGITUDE_KEY);
        double altitude = jsonObject.getDouble(AppConfig.ALTITUDE_KEY);

        return new PointOfInterest(title,latitude,longitude,altitude);
    }
}
