package de.ur.mi.android.sightseerlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.ur.mi.android.sightseerlite.R;
import de.ur.mi.android.sightseerlite.domain.PointOfInterest;
import de.ur.mi.android.sightseerlite.navigation.NavigationController;

public class LocationListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PointOfInterest> locations;

    public LocationListAdapter(Context context, ArrayList<PointOfInterest> locations) {
        this.context = context;
        this.locations = locations;
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(locations, new LocationDistanceComparator());
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.location_list_item, null);
        }

        PointOfInterest poi = locations.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.list_item_title);
        TextView distance = (TextView) convertView.findViewById(R.id.list_item_distance);

        float targetDistance = NavigationController.getInstance(context).getEstimatedDistanceForLocation(poi.getLocation());
        title.setText(poi.getTitle());
        distance.setText(Math.round(targetDistance) + "m");
        return convertView;
    }

    private class LocationDistanceComparator implements Comparator<PointOfInterest> {
        @Override
        public int compare(PointOfInterest poi1, PointOfInterest poi2) {
            float distanceToFirstLocation = NavigationController.getInstance(context).getEstimatedDistanceForLocation(poi1.getLocation());
            float distanceToSecondLocation = NavigationController.getInstance(context).getEstimatedDistanceForLocation(poi2.getLocation());
            return (int) (distanceToFirstLocation - distanceToSecondLocation);
        }
    }
}

