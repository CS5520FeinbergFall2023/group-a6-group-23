package edu.northeastern.s3kb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeekerFavoriteActivity extends AppCompatActivity {

    private FavoritesAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<favProperty> favoriteProperties;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference db;

    private Bundle bundle = null;
    private String userKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_favorite);

        recyclerView = findViewById(R.id.favRecycler);

        userKey = getIntent().getStringExtra("userKey");

        favoriteProperties = new ArrayList<>();
        adapter = new FavoritesAdapter(SeekerFavoriteActivity.this, favoriteProperties, userKey);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");

        firebaseDatabase = FirebaseDatabase.getInstance();
        db = firebaseDatabase.getReference("");

        init();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_favorites);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.page_home) {
                    Intent intent = new Intent(getApplicationContext(),PropertySeekerActivity.class);
                    intent.putExtra("userKey", userKey);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                }

                if(item.getItemId() == R.id.page_favorites) {
                    return true;
                }

                if(item.getItemId() == R.id.page_profile) {
                    Intent clickIntent1 = new Intent(SeekerFavoriteActivity.this, SeekerProfileActivity.class);
                    clickIntent1.putExtra("userKey", userKey);
                    startActivity(clickIntent1);
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

    }

    private void init() {

        db = FirebaseDatabase.getInstance().getReference("seekers");
        db.child(userKey).child("favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favoriteProperties.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Log.v("KAUSHIK", ds.child("owner").getValue().toString());
                            favProperty prop = new favProperty();
                            prop.setHouseID(ds.child("propID").getValue().toString());
                            prop.setUserID(ds.child("owner").getValue().toString());
                            favoriteProperties.add(prop);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent clickIntent1 = new Intent(SeekerFavoriteActivity.this, PropertySeekerActivity.class);
        clickIntent1.putExtra("userKey", userKey);
        startActivity(clickIntent1);
        finish();
    }
}