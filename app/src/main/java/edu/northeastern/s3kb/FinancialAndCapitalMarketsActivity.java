package edu.northeastern.s3kb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import edu.northeastern.s3kb.R;

public class FinancialAndCapitalMarketsActivity extends AppCompatActivity {

    private Map<String, String> dataMap;

    private Executor executor = Executors.newSingleThreadExecutor();

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private RecyclerView recyclerView;
    private List<String> itemList;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.financial_and_capital_markets_activity);

        recyclerView = findViewById(R.id.recyclerView);
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        initializedataMap();

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
        dataMap.put("pub_sec_suk_gov_hou_suk", "📈");

        // Rest of the dataMap initialization...

    }

    private void fetchData(String apiUrl) {
        executor.execute(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.BNM.API.v1+json");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String formattedJson = formatJson(response.toString());

                    mainHandler.post(() -> {
                        TextView msbResponse = findViewById(R.id.msbResponse);
                        msbResponse.setText(formattedJson);
                    });
                } else {
                    mainHandler.post(() -> Toast.makeText(FinancialAndCapitalMarketsActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> Toast.makeText(FinancialAndCapitalMarketsActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private String formatJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            StringBuilder formattedJson = new StringBuilder();

            Iterator<String> keys = data.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = data.get(key).toString();
                if (dataMap.containsKey(key)) {
                    String icon = dataMap.get(key);
                    formattedJson.append(key).append(icon).append(": ").append(value).append("\n");
                } else {
                    formattedJson.append(key).append(": ").append(value).append("\n");
                }
            }

            return formattedJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        private List<String> itemList;

        public ItemAdapter(List<String> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String item = itemList.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
            }

            public void bind(String item) {
                textView.setText(item);
            }
        }
    }
}