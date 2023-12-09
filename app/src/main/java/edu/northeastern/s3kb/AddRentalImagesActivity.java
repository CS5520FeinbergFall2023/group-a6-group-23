package edu.northeastern.s3kb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddRentalImagesActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private EditText bedroomsEt, bathroomsEt, houseDescriptionEt, rentEt;

    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rental_images);

        submitBtn = findViewById(R.id.submitBtn);
        bedroomsEt = findViewById(R.id.bedroomsEt);
        bathroomsEt = findViewById(R.id.bathroomsEt);
        houseDescriptionEt = findViewById(R.id.houseDescriptionEt);
        rentEt = findViewById(R.id.rentEt);
        String country = getIntent().getExtras().getString("country");
        String state = getIntent().getExtras().getString("state");
        String city = getIntent().getExtras().getString("city");
        String address = getIntent().getExtras().getString("address");
        String type = getIntent().getExtras().getString("type");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String houseId = Long.toString(System.currentTimeMillis());
                String noOfRoom = bedroomsEt.getText().toString();
                String rentPerRoom = rentEt.getText().toString();
                String houseDescription = houseDescriptionEt.getText().toString();
                String baths = bathroomsEt.getText().toString();

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                assert firebaseUser != null;
                String userId = firebaseUser.getUid();

                Log.v("KAUSHIK", userId);
                databaseReference = FirebaseDatabase.getInstance().getReference().child("houses").child(userId);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("houseId", houseId);
                hashMap.put("noOfRoom", noOfRoom);
                hashMap.put("houseDescription", houseDescription);
                hashMap.put("houseLocation", city);
                hashMap.put("rentPerRoom", rentPerRoom);
                hashMap.put("userId", userId);
                hashMap.put("country",country);
                hashMap.put("state",state);
                hashMap.put("type",type);
                hashMap.put("baths",baths);
                hashMap.put("address",address);
                Log.v("KAUSHIK", hashMap+"");

                // Check if address already exists in the database
                databaseReference.orderByChild("address").equalTo(address).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Address already exists in the database
                            Toast.makeText(AddRentalImagesActivity.this, "Address already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // Address does not exist in the database, so we can add it
                            databaseReference.child(houseId).setValue(hashMap);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
            }
        });
    }

}