package edu.northeastern.s3kb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SummaryTransactionActivity extends AppCompatActivity implements DataFetcherTask.DataFetchListener {
    private ArrayList<ItemCard> itemList = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();

    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_transaction);
        init(savedInstanceState);
    }


    // Handling Orientation Changes on Android
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {


        int size = itemList == null ? 0 : itemList.size();
        outState.putInt(NUMBER_OF_ITEMS, size);
        for (int i = 0; i < size; i++) {
            // put image information id into instance
            outState.putInt(KEY_OF_INSTANCE + i + "0", itemList.get(i).getImageSource());
            // put itemName information into instance
            outState.putString(KEY_OF_INSTANCE + i + "1", itemList.get(i).getItemName());
            // put itemDesc information into instance
            outState.putString(KEY_OF_INSTANCE + i + "2", itemList.get(i).getItemDesc());
        }
        super.onSaveInstanceState(outState);

    }

    private void init(Bundle savedInstanceState) {

        initialItemData(savedInstanceState);
        createRecyclerView();
        if(itemList.size() <= 0){
            Intent intent = getIntent();
            String receivedValue = intent.getStringExtra("summary-transaction");
            DataFetcherTask dataFetcherTask = new DataFetcherTask(receivedValue, this);
            dataFetcherTask.execute();
        }
    }

    @Override
    public void onDataFetched(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            int[] months = {R.drawable.jan, R.drawable.feb,R.drawable.mar,R.drawable.apr,
                    R.drawable.may, R.drawable.jun,R.drawable.jul,
                    R.drawable.aug, R.drawable.sep};
            String[] monthText = {"January", "February", "March", "April", "May",
                    "June", "July", "August", "September", "October", "November", "December"};

            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                int effYear = dataObject.getInt("eff_year");
                int effMonth = dataObject.getInt("eff_month");
                String totalTrans = dataObject.getString("total_trans");
                String percentage = dataObject.getString("percentage");
                String arg2 = String.format("Total : %s ( %s %% )", totalTrans, percentage);
                String arg1 = String.format("%s, %d", monthText[effMonth - 1], effYear);

                itemList.add(i, new ItemCard(effMonth == 0 ? R.drawable.jan: months[effMonth - 1], arg1, arg2));
                rviewAdapter.notifyItemInserted(i);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initialItemData(Bundle savedInstanceState) {

        // Not the first time to open this Activity
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (itemList == null || itemList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {
                    Integer imgId = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");

                    ItemCard itemCard = new ItemCard(imgId, itemName, itemDesc);

                    itemList.add(itemCard);
                }
            }
        }

    }

    private void createRecyclerView() {
        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new RviewAdapter(itemList);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

}