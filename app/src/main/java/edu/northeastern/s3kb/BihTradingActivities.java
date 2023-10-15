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

public class BihTradingActivities extends AppCompatActivity {

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

    }

    private void fetchData(String apiUrl) {
        runnableThread = new RunnableThread(apiUrl);
        new Thread(runnableThread).start();
    }

    private void displayDataWithIcons(TextView textView, String data) {
        data = "{\"data\":{\"tra_date\":\"2023-10-13\",\"sto_code\":\"VK230364\",\"sto_desc\":\"PRESS METAL IMTN 4.450% 18.09.2030\",\"issuer\":\"PRESS METAL\",\"mat_date\":\"2030-09-18\",\"las_trd_pri\":\"100.020\",\"las_trd_yie\":\"4.450\",\"low_yie\":\"4.450\",\"high_yie\":\"4.450\",\"vol\":\"5.00000000000\"},\"meta\":{\"last_updated\":\"2023-10-13 20:38:32\",\"total_result\":1}}";
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
                    String htmlText = "<html><head><style type='text/css'>body {line-height: 3.5;}</style></head><body>" +
                            displayText.toString() + "</body></html>";
                    textView.setText(Html.fromHtml(displayText.toString(), Html.FROM_HTML_MODE_COMPACT));
                }
            }

            //textView.setText(displayText.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String something = displayText.toString();
        //textView.setText(displayText.toString());
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
                    //to be commented

                    //comment this

                    URL url = new URL(apiUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    urlConnection.setRequestProperty("Accept", "application/vnd.BNM.API.v1+json");

                    try {
                        //int responseCode = urlConnection.getResponseCode();

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
                            //TextView tvResponse = findViewById(R.id.tvHeatMapResponse);
                            //displayDataWithIcons(tvResponse, result);
                            //tvResponse.setText(result);

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