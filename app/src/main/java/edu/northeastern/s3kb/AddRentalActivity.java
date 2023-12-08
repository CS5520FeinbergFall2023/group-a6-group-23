package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Map;

public class AddRentalActivity extends AppCompatActivity {

    private Spinner propertyTypeSpinner;

    private DatabaseReference databaseReference;

    private EditText addressEditText;
    private EditText usernameEditText;
    private EditText aptUnitNumberEditText;
    private EditText stateEditText;
    private EditText zipCodeEditText;
    private EditText cityEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rental);

        nextButton = findViewById(R.id.nxtButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("owner");

        propertyTypeSpinner = findViewById(R.id.propertySpinner);
        String[] propertyTypeItems = {"Apartment", "House/Villa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, propertyTypeItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyTypeSpinner.setAdapter(adapter);

        addressEditText = findViewById(R.id.addressEt);
        aptUnitNumberEditText = findViewById(R.id.aptEt);
        usernameEditText = findViewById(R.id.usernameEt);
        stateEditText = findViewById(R.id.stateEt);
        cityEditText = findViewById(R.id.cityEt);
        zipCodeEditText = findViewById(R.id.zipcodeEt);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = addressEditText.getText().toString();
                String aptUnitNumber = aptUnitNumberEditText.getText().toString();
                String propertyTpe = propertyTypeSpinner.getSelectedItem().toString();
                String username = usernameEditText.getText().toString();
                String city = cityEditText.getText().toString();
                String state = stateEditText.getText().toString();
                int zipcode = Integer.parseInt(zipCodeEditText.getText().toString());
                databaseReference.child(username).get().addOnCompleteListener((node)->{
                    Map<String, Object> userMap = (Map<String, Object>) node.getResult().getValue();
                    Owner owner = null;
                    owner = new Owner(username, address, aptUnitNumber, propertyTpe, city, state, zipcode);
                    databaseReference.child(username).setValue(owner);

//                    Intent clickIntent = new Intent(LoginA8Activity.this, StickItToEm.class);
//                    clickIntent.putExtra("currentUserName", userName);
//                    clickIntent.putExtra("lastVisited", date.toString());
//                    startActivity(clickIntent);
                });

            }
        });


    }

}