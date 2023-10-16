package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BihHeatMapActivity extends AppCompatActivity {

    private Map<String, String> iconMapping;

    private RunnableThread runnableThread;

    private Executor executor = Executors.newSingleThreadExecutor();

    private Handler mainHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bih_heat_map);

        Intent intent = getIntent();
        String receivedValue = intent.getStringExtra("heatmap");

        fetchData(receivedValue);

    }

    private void initializeIconMapping() {
        iconMapping = new HashMap<>();
        iconMapping.put("tra_date", "📅");
        iconMapping.put("sto_code", "💻");
        iconMapping.put("sto_desc", "📄");
        iconMapping.put("issuer", "🏛️");
        iconMapping.put("mat_date", "⌛");
        iconMapping.put("las_trd_pri", "📈");
        iconMapping.put("las_trd_yie", "📊");
        iconMapping.put("low_yie", "📉");
        iconMapping.put("high_yie", "📈");
        iconMapping.put("vol", "📊");
    }

    private void fetchData(String apiUrl) {
        runnableThread = new RunnableThread(apiUrl);
        new Thread(runnableThread).start();
    }

    private void displayDataWithIcons(TextView textView, String data) {

        initializeIconMapping();
        StringBuilder displayText = null;
        try {
            JSONObject jsonData = new JSONObject(data);
            JSONObject dataObject = jsonData.getJSONObject("data");

            displayText = new StringBuilder();

            for (Map.Entry<String, String> entry : iconMapping.entrySet()) {
                String key = entry.getKey();
                String icon = entry.getValue();

                if (dataObject.has(key)) {
                    String value = dataObject.getString(key);
                    displayText.append("<b>").append(icon).append(" ").append(key).append(": </b>").append(value).append("<br>").append("<br>").append("<br>");
                    textView.setText(Html.fromHtml(displayText.toString(), Html.FROM_HTML_MODE_COMPACT));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class RunnableThread implements Runnable
    {
        private String apiUrl;
        public RunnableThread(String apiUrl)
        {
            this.apiUrl = apiUrl;
        }
        @Override
        public void run() {
            executor.execute(() -> {
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    urlConnection.setRequestProperty("Accept", "application/vnd.BNM.API.v1+json");

                    try {
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(urlConnection.getInputStream()));

                        StringBuilder stringBuilder = new StringBuilder();
                        String line;

                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }

                        bufferedReader.close();
                        String result = stringBuilder.toString();
                        mainHandler.post(() -> {
                            TextView tvResponse = findViewById(R.id.tvBihHeatMapResponse);
                            displayDataWithIcons(tvResponse, result);
                            tvResponse.setText(result);

                        });
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                    });
                }
            });
        }
    }

}