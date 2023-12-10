package edu.northeastern.s3kb;

import android.content.Intent;
import android.os.Bundle;
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

    private DatabaseReference db;

    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_favorite);

        retrieveUserKey();
        initializeRecyclerView();
        initializeDatabaseReference();
        populateFavoriteProperties();
        setupBottomNavigationView();
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.favRecycler);
        favoriteProperties = new ArrayList<>();
        adapter = new FavoritesAdapter(SeekerFavoriteActivity.this, favoriteProperties, userKey);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void retrieveUserKey() {
        userKey = getIntent().getStringExtra("userKey");
    }

    private void initializeDatabaseReference() {
        db = FirebaseDatabase.getInstance().getReference("seekers");
    }

    private void populateFavoriteProperties() {
        db.child(userKey).child("favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favoriteProperties.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
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

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_favorites);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.page_home) {
                    navigateTo(PropertySeekerActivity.class);
                    return true;
                }
                else if(item.getItemId() == R.id.page_favorites)
                        return true;
                else if(item.getItemId() == R.id.page_profile) {
                    navigateTo(SeekerProfileActivity.class);
                    return true;
                }
                else{
                        return false;
                }
            }
        });
    }

    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.putExtra("userKey", userKey);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    public void onBackPressed() {
        navigateTo(PropertySeekerActivity.class);
        finish();
    }
}