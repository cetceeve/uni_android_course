package de.ur.mi.bundesligatabelle.download;

import android.os.AsyncTask;

public class TableDownloadTask extends AsyncTask<String, Integer, String> {

    private static final String RANK = "";
    private static final String TEAM = "";
    private static final String GAMES = "";
    private static final String POINTS = "";
    private static final String GOALS = "";
    private static final String GOALS_AGAINST = "";


    @Override
    protected String doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }

    private void processJson(String text) {
    }
}
