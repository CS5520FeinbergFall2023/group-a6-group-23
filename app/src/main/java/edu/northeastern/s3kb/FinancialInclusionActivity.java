package edu.northeastern.s3kb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class FinancialInclusionActivity extends AppCompatActivity {

    private Map<String, String> dataMap;

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private RecyclerView recyclerViewFI;
    private List<String> itemList;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_inclusion);

        recyclerViewFI = findViewById(R.id.recyclerViewFI);
        itemList = new ArrayList<>();
        itemAdapter = new FinancialInclusionActivity.ItemAdapter(itemList);

        recyclerViewFI.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFI.setAdapter(itemAdapter);

        initializedataMap();

        Intent intent = getIntent();
        String receivedValue = intent.getStringExtra("FI");
        System.out.println("financial inclusion Created" + receivedValue);
        fetchData(receivedValue);
    }
    private void initializedataMap() {
        dataMap = new HashMap<>();
        dataMap.put("year_dt", "📅");
        dataMap.put("month_dt", "📅");
        dataMap.put("sector", "🏛️");
        dataMap.put("pri_agr", "📄");
        dataMap.put("min_and_qua", "⌛");
        dataMap.put("man_inc_agr_bas", "📈");
        dataMap.put("ele_gas_and_wat_sup", "📊");
        dataMap.put("construction", "📉");
        dataMap.put("who_ret_tra_and_res_hot", "📈");
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
                    System.out.println("financial inclusion response" + response.toString());
                    String formattedJson = formatJson(response.toString());


                    mainHandler.post(() -> {
                        TextView fiResponse = findViewById(R.id.FiResponse);
                        fiResponse.setText(formattedJson);
                    });
                } else {
                    mainHandler.post(() -> Toast.makeText(FinancialInclusionActivity.this, "Failed to fetch FI data", Toast.LENGTH_SHORT).show());
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> Toast.makeText(FinancialInclusionActivity.this, "Failed to fetch the FI data", Toast.LENGTH_SHORT).show());
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


    class ItemAdapter extends RecyclerView.Adapter<FinancialInclusionActivity.ItemAdapter.ViewHolder> {

        private final List<String> itemList;

        public ItemAdapter(List<String> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public FinancialInclusionActivity.ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            return new FinancialInclusionActivity.ItemAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull FinancialInclusionActivity.ItemAdapter.ViewHolder holder, int position) {
            String item = itemList.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView textView;

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