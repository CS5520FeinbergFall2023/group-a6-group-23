package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

public class StickItToEm extends AppCompatActivity {
    List<String> stickerIdentifiers = Arrays.asList("captainamerica_sticker", "drstrange_sticker", "spiderman_sticker",
            "thor_sticker");

    private RecyclerView stickerRecyclerView;

    private RecyclerView.LayoutManager recycleLayoutManager;
    private StickerAdapter stickerAdapter;

    private Button btnHistory;

    private User userToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");

        recycleLayoutManager = new LinearLayoutManager(this);
        stickerRecyclerView = findViewById(R.id.recycleViewStickers);
        stickerRecyclerView.setHasFixedSize(true);
        stickerAdapter = new StickerAdapter(this, stickerIdentifiers, userName);
        stickerRecyclerView.setAdapter(stickerAdapter);
        stickerRecyclerView.setLayoutManager(recycleLayoutManager);

        btnHistory = findViewById(R.id.btnHistory);

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clickIntent = new Intent(StickItToEm.this, StickersHistory.class);
                clickIntent.putExtra("user", userName);
                startActivity(clickIntent);
            }
        });
    }
}