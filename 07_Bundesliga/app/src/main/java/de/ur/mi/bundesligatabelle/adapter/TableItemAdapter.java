package de.ur.mi.bundesligatabelle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.ur.mi.bundesligatabelle.R;
import de.ur.mi.bundesligatabelle.domain.TableItem;

public class TableItemAdapter extends ArrayAdapter<TableItem> {

    private List<TableItem> table;
    private Context context;

    public TableItemAdapter(Context context, List<TableItem> table) {
        super(context, R.layout.table_item, table);
        this.context = context;
        this.table = table;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.table_item, null);
        }

        TextView rank = (TextView) v.findViewById(R.id.rank);
        TextView team = (TextView) v.findViewById(R.id.team);
        TextView playedGames = (TextView) v.findViewById(R.id.played_games);
        TextView points = (TextView) v.findViewById(R.id.points);
        TextView goalDifference = (TextView) v.findViewById(R.id.goals);

        //set data here
        TableItem tableItem = table.get(position);
        rank.setText(Integer.toString(tableItem.getRank()));
        team.setText(tableItem.getName());
        playedGames.setText(Integer.toString(tableItem.getPlayedGames()));
        points.setText(Integer.toString(tableItem.getPoints()));

        String goalDifferenceString = Integer.toString(tableItem.getGoals()) + ":" + Integer.toString(tableItem.getGoalsAgainst());
        goalDifference.setText(goalDifferenceString);

        v.setBackgroundColor(context.getResources().getColor(colorResolver(position)));

        return v;
    }

    private int colorResolver(int pos) {
        if (pos < 3) {
            return android.R.color.holo_green_dark;
        }
        if (pos < 6) {
            return android.R.color.holo_green_light;
        }
        if (pos < 15) {
            return android.R.color.darker_gray;
        }
        if (pos == 15) {
            return android.R.color.holo_red_light;
        }
        return android.R.color.holo_red_dark;
    }

}