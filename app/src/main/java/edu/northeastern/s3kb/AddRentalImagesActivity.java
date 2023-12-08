package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                databaseReference.child(houseId).setValue(hashMap);
            }
        });
    }
}