package edu.northeastern.s3kb;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class DataFetcherTask extends AsyncTask<Void, Void, String> {

    private final String apiUrl;
    private final DataFetchListener listener;

    public DataFetcherTask(String apiUrl, DataFetchListener listener) {
        this.apiUrl = apiUrl;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/vnd.BNM.API.v1+json");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle error appropriately
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            // Data fetched successfully, pass it back to the UI thread
            listener.onDataFetched(result);
        } else {
            Log.v("ERROR", "Could not fetch data");
        }
    }

    public interface DataFetchListener {
        void onDataFetched(String data);
    }
}
