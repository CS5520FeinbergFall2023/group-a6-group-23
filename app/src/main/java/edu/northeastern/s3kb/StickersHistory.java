package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickersHistory extends AppCompatActivity {

    private RecyclerView stickerRecyclerView;

    private RecyclerView.LayoutManager recycleLayoutManager;
    private Map<String, Map<String, Object>> usersMap = new HashMap<>();
    private StickerAdapter stickerAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers_history);

        Intent intent = getIntent();
        String username = intent.getStringExtra("user");

        //can remove this used for testing
        List<String> sentStickers = new ArrayList<>();

        TextView tvCount = findViewById(R.id.tvCount);

        tvCount.setText("Count of Stickers: 0" );

        recycleLayoutManager = new LinearLayoutManager(this);
        stickerRecyclerView = findViewById(R.id.recycleViewStickersHistory);

        //can remove this used for testing
        String[] stickerData = {"captainamerica_sticker", "drstrange_sticker", "spiderman_sticker",
                "thor_sticker", "deadpool_sticker"};
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("stickers").get().addOnCompleteListener((task) -> {
            usersMap = (Map) task.getResult().getValue();
            if(username != null){
                for(String a : usersMap.keySet()) {
                    Map<String, Object> data = usersMap.get(a);
                    if(data.get("from").toString().equals(username)) {
                        int pos = sentStickers.size();
                        sentStickers.add(stickerData[Integer.valueOf(data.get("imageId").toString())]);
                        stickerAdapter = new StickerAdapter(this, sentStickers, username);
                        stickerRecyclerView.setAdapter(stickerAdapter);
                        stickerRecyclerView.setLayoutManager(recycleLayoutManager);

                        tvCount.setText("Count of Stickers: " + sentStickers.size());
                    }
                }
            }
        });



    }
}