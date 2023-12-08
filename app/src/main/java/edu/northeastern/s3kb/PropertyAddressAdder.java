package edu.northeastern.s3kb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PropertyAddressAdder extends AppCompatActivity {
    EditText editTextCity;
    Button buttonNext;
    EditText editTextState;
    EditText editTextCountry;
    EditText editTextFullAddress;
    String selectedPropertyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_address);
        editTextCity = findViewById(R.id.et_Location);
        buttonNext = findViewById(R.id.passInfo);
        editTextState = findViewById(R.id.et_state);
        editTextCountry = findViewById(R.id.et_country);
        editTextFullAddress = findViewById(R.id.et_address);
        Spinner spinnerPropertyType = findViewById(R.id.spinner_languages);
        ArrayAdapter<CharSequence> adapterPropertyType = ArrayAdapter.createFromResource(this, R.array.propertyType, android.R.layout.simple_spinner_item);

        adapterPropertyType.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerPropertyType.setAdapter(adapterPropertyType);

        spinnerPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedPropertyType = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processNextButtonClick();
            }
        });
    }

    private void processNextButtonClick() {
        String city = editTextCity.getText().toString();
        String state = editTextState.getText().toString();
        String country = editTextCountry.getText().toString();
        String fullAddress = editTextFullAddress.getText().toString();

        if (isInputValid(city, state, country, fullAddress)) {
            Intent intentToAddProperty = new Intent(PropertyAddressAdder.this, PropertyAdder.class);
            intentToAddProperty.putExtra("location", city);
            intentToAddProperty.putExtra("address", fullAddress);
            intentToAddProperty.putExtra("state", state);
            intentToAddProperty.putExtra("country", country);
            intentToAddProperty.putExtra("type", selectedPropertyType);
            startActivity(intentToAddProperty);
            finish();
        }
    }

    private boolean isInputValid(String city, String state, String country, String fullAddress) {
        if (fullAddress.isEmpty()) {
            editTextFullAddress.setError("Please Enter the Address");
            return false;
        }
        if (city.isEmpty()) {
            editTextCity.setError("Please Enter the City");
            return false;
        }
        if (state.isEmpty()) {
            editTextState.setError("Please Enter the State");
            return false;
        }
        if (country.isEmpty()) {
            editTextCountry.setError("Please Enter the Country");
            return false;
        }
        return true;
    }
}
