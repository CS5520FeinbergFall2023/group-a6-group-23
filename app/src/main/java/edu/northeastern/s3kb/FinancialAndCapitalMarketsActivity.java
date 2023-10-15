package edu.northeastern.s3kb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FinancialAndCapitalMarketsActivity extends AppCompatActivity {

    private Map<String, String> dataMap;

    private FinancialAndCapitalMarketsActivity.RunnableThread runnableThread;

    private Executor executor = Executors.newSingleThreadExecutor();

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.financial_and_capital_markets_activity);

        Intent intent = getIntent();
        String receivedValue = intent.getStringExtra("MSB");
        System.out.println("fiN aCTIVITY Created" + receivedValue);
        fetchData(receivedValue);

    }

    private void initializedataMap() {
        dataMap = new HashMap<>();
        dataMap.put("year_dt", "📅");
        dataMap.put("month_dt", "💻");
        dataMap.put("pub_sec_con_bnm_bill_mon", "📄");
        dataMap.put("pub_sec_con_mal_tre_bil", "🏛️");
        dataMap.put("pub_sec_con_mas_gov_sec", "⌛");
        dataMap.put("pub_sec_suk_bnm_neg_mon_not", "📈");
        dataMap.put("pub_sec_suk_mas_isl_re_bil", "📊");
        dataMap.put("pub_sec_suk_mas_gov_inv_iss", "📉");
        dataMap.put("pub_sec_suk_gov_hou_suk", "📈");
        dataMap.put("pri_sec_con_tot", "📊");
        dataMap.put("pri_sec_suk_tot", "📊");
        dataMap.put("tot", "📊");
    }

    private void fetchData(String apiUrl) {
        runnableThread = new FinancialAndCapitalMarketsActivity.RunnableThread(apiUrl);
        new Thread(runnableThread).start();
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
                System.out.println("Inside Fin Activity");
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    System.out.println("URL" + url);
                    urlConnection.setRequestProperty("Accept", "application/vnd.BNM.API.v1+json");

                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;

                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }

                        bufferedReader.close();
                        String result = stringBuilder.toString();
                        System.out.println(result);
                        mainHandler.post(() -> {
                            TextView tvResponse = findViewById(R.id.msbResponse);
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
