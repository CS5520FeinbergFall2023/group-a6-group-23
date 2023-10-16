package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
        setContentView(R.layout.activity_bih_trading_activities);

        Intent intent = getIntent();
        String receivedValue = intent.getStringExtra("trading");
        TextView tvResponse = findViewById(R.id.tvBihTradingActivities);
        fetchData(receivedValue);

    }

    private void initializeiconMappingping() {
        iconMapping = new HashMap<>();
        iconMapping.put("trd_date", "📅");
        iconMapping.put("trd_time", "⌚");
        iconMapping.put("instrument", "🎻");
        iconMapping.put("stock_code", "🔢");
        iconMapping.put("stock_desc", "📝");
        iconMapping.put("stock_iss", "🏛️");
        iconMapping.put("price", "💰");
        iconMapping.put("yield", "📈");
        iconMapping.put("stock_sname", "📚");
        iconMapping.put("issue_date", "📅");
        iconMapping.put("mat_date", "📅");
        iconMapping.put("rem_tenure", "⏳");
        iconMapping.put("coup_rate", "📈");
        iconMapping.put("amount", "💲");
        iconMapping.put("discount", "📉");
        iconMapping.put("val_date", "📅");
    }

    private void fetchData(String apiUrl) {
        runnableThread = new RunnableThread(apiUrl);
        new Thread(runnableThread).start();
    }

    private void displayDataWithIcons(TextView textView, String data) {
        data = "{\"data\": [\n        {\n            \"trd_date\": \"2023-10-13\",\n            \"trd_time\": \"17:40:31\",\n            \"instrument\": \"MGS\",\n            \"stock_code\": \"MS120002\",\n            \"stock_desc\": \"MGS 2/2012 3.892% 15.03.2027\",\n            \"stock_iss\": \"GOVERNMENT\",\n            \"price\": \"100.49\",\n            \"yield\": \"3.73\",\n            \"stock_sname\": \"MS03/27\",\n            \"issue_date\": \"2012-03-15\",\n            \"mat_date\": \"2027-03-15\",\n            \"rem_tenure\": 1249,\n            \"coup_rate\": \"3.89\",\n            \"amount\": \"0.03\",\n            \"discount\": null,\n            \"val_date\": \"2023-10-17\"\n        },\n        {\n            \"trd_date\": \"2023-10-13\",\n            \"trd_time\": \"17:39:52\",\n            \"instrument\": \"IMTN\",\n            \"stock_code\": \"VI210052\",\n            \"stock_desc\": \"EWCSB IMTN 5.850% 24.03.2026 - Series 1 Tranche 1\",\n            \"stock_iss\": \"EWCSB\",\n            \"price\": \"100.97\",\n            \"yield\": \"5.42\",\n            \"stock_sname\": \"VI210052\",\n            \"issue_date\": \"2021-03-24\",\n            \"mat_date\": \"2026-03-24\",\n            \"rem_tenure\": 893,\n            \"coup_rate\": \"5.85\",\n            \"amount\": \"0.60\",\n            \"discount\": null,\n            \"val_date\": \"2023-10-17\"\n        }\n]}";

        initializeiconMappingping();
        StringBuilder displayText = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            int j = dataArray.length();
            textView.setText("pqrs");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                displayText = new StringBuilder();

                for (Map.Entry<String, String> entry : iconMapping.entrySet()) {
                    String key = entry.getKey();
                    String icon = entry.getValue();

                    if (dataObject.has(key)) {
                        String value = "";
                        value = dataObject.getString(key);
                        displayText.append("<br>").append("<b>").append(icon).append(" ").append(key).append(": </b>").append(value).append("<br>").append("<br>").append("<br>");
                    }
                }
            }
            textView.setText(Html.fromHtml(displayText.toString(), Html.FROM_HTML_MODE_COMPACT));
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
                    //to be commented
                    mainHandler.post(() -> {
                       TextView tvResponse = findViewById(R.id.tvBihTradingActivities);
                        //tvResponse.setText("hello");
                        displayDataWithIcons( tvResponse,"");

                    });
                    //comment this

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
                            //TextView tvResponse = findViewById(R.id.tvBihTradingActivities);
                            //displayDataWithIcons(tvResponse, result);

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