package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StickersHistory extends AppCompatActivity {

    private RecyclerView stickerRecyclerView;

    private RecyclerView.LayoutManager recycleLayoutManager;
    private StickerAdapter stickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers_history);

        Intent intent = getIntent();
        String username = intent.getStringExtra("user");

        //can remove this used for testing
        List<String> sentStickers = new ArrayList<>();
        sentStickers.add("drstrange_sticker");
        sentStickers.add("captainamerica_sticker");

        TextView tvCount = findViewById(R.id.tvCount);

        tvCount.setText("Count of Stickers: " + sentStickers.size());

        recycleLayoutManager = new LinearLayoutManager(this);
        stickerRecyclerView = findViewById(R.id.recycleViewStickersHistory);
        stickerRecyclerView.setHasFixedSize(true);
        stickerAdapter = new StickerAdapter(this, sentStickers, username);
        stickerRecyclerView.setAdapter(stickerAdapter);
        stickerRecyclerView.setLayoutManager(recycleLayoutManager);


    }
}