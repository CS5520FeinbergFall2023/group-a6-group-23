// References : https://developer.android.com/develop/ui/views/animations/transitions/start-activity
//https://www.youtube.com/watch?v=DkSlk03-opA
//https://mikescamell.com/shared-element-transitions-part-4-recyclerview/
//https://stackoverflow.com/questions/27438145/out-of-memory-error-java-heap-memory-on-android-studio
//https://stackoverflow.com/questions/11188398/how-to-change-the-spinner-background-in-android
//https://projectgurukul.org/android-project-house-rental-management-app/
// https://www.youtube.com/watch?v=eAooM-F1X9c&list=PLlxmoA0rQ-Lzd9-NUrP5Wi18OMi4R-zs_&index=28

package edu.northeastern.s3kb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Listings extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PropertyListAdapterOwner adapter;
    private ArrayList<Property> listings = new ArrayList<>();
    DatabaseReference reference;
    FloatingActionButton add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Listings.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        add = findViewById(R.id.floatingAdd);
        getAllProperties();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Listings.this, AddRentalActivity.class));
                overridePendingTransition( android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

    }

    private void getAllProperties() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("houses").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listings.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Property article = dataSnapshot.getValue(Property.class);
                    if(article.getHouseImage() == null) {
                        article.setHouseImage("https://firebasestorage.googleapis.com/v0/b/s3kb-b07f0.appspot.com/o/Uploads%2Fhouse.jpeg?alt=media&token=2377f3e6-b312-4adc-9b5d-3f18b6443827");
                    }
                    listings.add(article);
                    Collections.reverse(listings);
                }
                adapter = new PropertyListAdapterOwner(Listings.this, listings);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}