package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Map;

public class AddRentalActivity extends AppCompatActivity {

    private Spinner propertyTypeSpinner;

    private DatabaseReference databaseReference;

    private EditText addressEditText;
    private EditText stateEditText;
    private EditText countryEditText;
    private EditText cityEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rental);

        nextButton = findViewById(R.id.nxtButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("owner");

        propertyTypeSpinner = findViewById(R.id.propertySpinner);
        String[] propertyTypeItems = {"Apartment", "House/Villa", "Condo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, propertyTypeItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyTypeSpinner.setAdapter(adapter);

        addressEditText = findViewById(R.id.addressEt);
        stateEditText = findViewById(R.id.stateEt);
        cityEditText = findViewById(R.id.cityEt);
        countryEditText = findViewById(R.id.countryEt);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = addressEditText.getText().toString();
                String propertyType = propertyTypeSpinner.getSelectedItem().toString();
                String city = cityEditText.getText().toString();
                String state = stateEditText.getText().toString();
                String country = countryEditText.getText().toString();
                if (address.length() == 0 || propertyType.length() == 0 || city.length() == 0 ||
                    state.length() == 0 || country.length() == 0) {
                    Toast.makeText(AddRentalActivity.this,
                            "Please enter all the required details", Toast.LENGTH_SHORT).show();
                    nextButton.setError("Please enter all the required details");
                } else {
                    Intent i = new Intent(AddRentalActivity.this, AddRentalImagesActivity.class);
                    i.putExtra("country", country);
                    i.putExtra("address", address);
                    i.putExtra("state", state);
                    i.putExtra("city", city);
                    i.putExtra("type", propertyType);
                    startActivity(i);
                    finish();
                }

            }
        });


    }

}