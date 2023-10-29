package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

public class StickItToEm extends AppCompatActivity {
    List<String> stickerIdentifiers = Arrays.asList("captainamerica_sticker", "drstrange_sticker", "spiderman_sticker",
            "thor_sticker");

    private RecyclerView stickerRecyclerView;

    private RecyclerView.LayoutManager recycleLayoutManager;
    private StickerAdapter stickerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        recycleLayoutManager = new LinearLayoutManager(this);
        stickerRecyclerView = findViewById(R.id.recycleViewStickers);
        stickerRecyclerView.setHasFixedSize(true);
        stickerAdapter = new StickerAdapter(this, stickerIdentifiers);
        stickerRecyclerView.setAdapter(stickerAdapter);
        stickerRecyclerView.setLayoutManager(recycleLayoutManager);

    }
}