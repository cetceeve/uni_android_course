package de.ur.mi.bundesligatabelle;

import android.app.Activity;
import android.os.Bundle;

public class TableActivity extends Activity {

    private final static String ADDRESS = "http://www.spiegel.de/staticgen/fussballticker/json/BUNDESLIGA/201718/34/tabellen.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
    }

}
