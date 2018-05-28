package de.ur.mi.bundesligatabelle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.ur.mi.bundesligatabelle.adapter.TableItemAdapter;
import de.ur.mi.bundesligatabelle.domain.TableItem;
import de.ur.mi.bundesligatabelle.download.DownloadListener;
import de.ur.mi.bundesligatabelle.download.TableDownloadTask;

public class TableActivity extends Activity implements DownloadListener{

    private final static String ADDRESS = "http://www.spiegel.de/staticgen/fussballticker/json/BUNDESLIGA/201718/34/tabellen.json";

    private TableItemAdapter tableItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        ListView listView = (ListView) findViewById(R.id.tabelle);
        ArrayList<TableItem> tableItems = new ArrayList<>();

        tableItemAdapter = new TableItemAdapter(this, tableItems);
        listView.setAdapter(tableItemAdapter);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = layoutInflater.inflate(R.layout.table_item, null);
        listView.addHeaderView(newView);

        TableDownloadTask tableDownloadTask = new TableDownloadTask(tableItems, this);
        tableDownloadTask.execute(ADDRESS);
    }


    @Override
    public void onDownloadFinished() {
        tableItemAdapter.notifyDataSetChanged();

        Context context = getApplicationContext();
        CharSequence text = "DOWNLOAD FERTIG";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
