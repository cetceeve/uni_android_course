package de.ur.mi.bundesligatabelle.download;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.ur.mi.bundesligatabelle.domain.TableItem;

public class TableDownloadTask extends AsyncTask<String, Void, String> {

    private static final String RANK = "";
    private static final String TEAM = "";
    private static final String GAMES = "";
    private static final String POINTS = "";
    private static final String GOALS = "";
    private static final String GOALS_AGAINST = "";

    private List<TableItem> tableItems;
    private DownloadListener downloadListener;

    public TableDownloadTask(ArrayList<TableItem> tableItems,  DownloadListener downloadListener) {
        this.tableItems = tableItems;
        this.downloadListener = downloadListener;
    }


    @Override
    protected String doInBackground(String... params) {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(params[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String charset = conn.getContentEncoding();
            if (charset == null) {
                charset = "UTF-8";
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return new String(baos.toByteArray(), charset);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject jsonRootObject = new JSONObject(jsonResponse);
            JSONObject jsonInnerObject = jsonRootObject.getJSONObject("SPIELTAG");
            JSONObject jsonInnerInnerObject = jsonInnerObject.getJSONObject("HEIMTABELLE");

            JSONArray jsonArray = jsonInnerInnerObject.optJSONArray("VEREIN");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int rank = jsonObject.getInt("PLATZ");
                String name = jsonObject.getString("content");
                int goals = jsonObject.getInt("TOREPOSITIV");
                int goalsAgainst = jsonObject.getInt("TORENEGATIV");
                int games = jsonObject.getInt("SPIELE");
                int points = jsonObject.getInt("PUNKTE");

                TableItem newTableItem = new TableItem(rank, name, goals, goalsAgainst, games, points);
                tableItems.add(newTableItem);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("JSON_Parser", "Problem parsing the JSON results", e);
        }

        downloadListener.onDownloadFinished();

    }

    private void processJson(String text) {
    }
}
