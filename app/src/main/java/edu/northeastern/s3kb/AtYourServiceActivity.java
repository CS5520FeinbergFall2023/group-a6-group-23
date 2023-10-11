package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class AtYourServiceActivity extends AppCompatActivity {

    private String[] dropDownContent = new String[]{"Islamic Interbank Money Market(IIMM)", "Kura's_choice", "Kurrumaddali's_choice", "Kota's Choice"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, this.dropDownContent);
        AutoCompleteTextView text = findViewById(R.id.autoCompleteTextView);
        text.setAdapter(arrayAdapter);
    }
}