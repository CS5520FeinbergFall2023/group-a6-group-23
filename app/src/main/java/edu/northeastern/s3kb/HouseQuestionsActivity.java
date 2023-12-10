package edu.northeastern.s3kb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HouseQuestionsActivity extends AppCompatActivity {

    private EditText location;
    private ChipGroup chipGroup;
    private Button addLocation;
    private CheckBox apartment;
    private CheckBox townhouse;
    private CheckBox condo;
    private CheckBox duplex;
    private String totalBeds;
    private String totalBaths;
    private String minPrice;
    private String maxPrice;

    private List<String> locationPreference;
    private List<String> housePreference;

    Bundle bundle = null;
    String userKey;
    String avatarId;
    String seekerFullName;
    String seekerEmailId;
    String seekerPhone;
    String legalSex;
    String age;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_questions);

        // instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for the database.
        databaseReference = firebaseDatabase.getReference("");

        locationPreference = new ArrayList<>();
        housePreference = new ArrayList<>();

        //Get data from previous.
        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");
        avatarId = bundle.getString("avatarId");
        seekerFullName = bundle.getString("seekerFullName");
        seekerEmailId = bundle.getString("seekerEmailId");
        seekerPhone = bundle.getString("seekerPhone");
        legalSex = bundle.getString("legalSex");
        age = bundle.getString("age");
        if(age == null){
            age = "18";
        }

        location = (EditText) findViewById(R.id.editTextTextLocation);
        addLocation = (Button) findViewById(R.id.addLocationBtn);
        chipGroup = findViewById(R.id.locationChipGroup);

        apartment = (CheckBox)findViewById(R.id.apartment_chkbox);
        townhouse = (CheckBox)findViewById(R.id.townhouse_chkbox);
        condo = (CheckBox)findViewById(R.id.condo_chkbox);
        duplex = (CheckBox)findViewById(R.id.duplex_chkbox);

        RadioGroup numberOfBedsRG  = (RadioGroup) findViewById(R.id.bedroomRadioGroup);
        numberOfBedsRG.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            totalBeds = radioButton.getText().toString();
        });

        RadioGroup numberOfBathsRG = (RadioGroup) findViewById(R.id.bathroomRadioGroup);
        numberOfBathsRG.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            totalBaths = radioButton.getText().toString();
        });

        Spinner minimumPriceSpinner = (Spinner) findViewById(R.id.minimumPriceSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.min_price_array, android.R.layout.simple_spinner_item);
        minimumPriceSpinner.setAdapter(adapter1);
        minimumPriceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minPrice = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                minPrice = "0";
            }
        });

        Spinner maximumPriceSpinner = (Spinner) findViewById(R.id.maximumPriceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.max_price_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        maximumPriceSpinner.setAdapter(adapter2);
        maximumPriceSpinner.setSelection(9);
        maximumPriceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maxPrice = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                maxPrice = "5000";
            }
        });

    }

    /**
     * Method called on submitting the preference.
     * @param view current view.
     */
    public void submitPreferences(View view){

        //Determine the checkboxes checked for type of house
        if (apartment.isChecked()) housePreference.add("Apartment");
        if (townhouse.isChecked()) housePreference.add("Townhouse");
        if (condo.isChecked()) housePreference.add("Condo");
        if (duplex.isChecked()) housePreference.add("Duplex");

        //Create preference object.
        Preference myPref = new Preference(avatarId, seekerFullName, seekerEmailId, seekerPhone, legalSex,
                Integer.parseInt(age), locationPreference, housePreference,
                totalBeds, totalBaths, Integer.parseInt(minPrice.substring(1)), Integer.parseInt(maxPrice.substring(1)));

        //Update preference.
        databaseReference.child("seekers").child(userKey).child("myPreference").setValue(myPref).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HouseQuestionsActivity.this, "Preference update is unavailable. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        Intent clickIntent = new Intent(HouseQuestionsActivity.this, PropertySeekerActivity.class);
        clickIntent.putExtra("userKey", userKey);
        startActivity(clickIntent);

    }

    /**
     * Method that is invoked on click of add location button, adds chips to the chip group.
     * @param view current view.
     */
    public void addLocationGroup(View view){

        String loc = location.getText().toString();

        Chip chip = new Chip(this);
        chip.setText(loc);
        chip.setChipBackgroundColorResource(R.color.purple_200);
        chip.setCloseIconVisible(true);
        chip.setTextColor(getResources().getColor(R.color.white));

        //add chip into the chip group
        chipGroup.addView(chip);
        locationPreference.add(location.getText().toString());

        //on close of each chip
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationPreference.remove(loc);
                chipGroup.removeView(chip);
            }
        });

    }

}