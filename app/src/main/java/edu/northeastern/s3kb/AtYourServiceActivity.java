package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class AtYourServiceActivity extends AppCompatActivity {

    private String[] dropDownContent = new String[]{"Islamic Interbank Money Market(IIMM)", "Kura's_choice", "Kurrumaddali's_choice", "Kota's Choice"};
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, this.dropDownContent);
        AutoCompleteTextView text = findViewById(R.id.autoCompleteTextView);
        text.setAdapter(arrayAdapter);

        constraintLayout = findViewById(R.id.constraintLayout);

        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if("Islamic Interbank Money Market(IIMM)".equals(selectedItem)) {
                    Log.v("Kaushik", "Here");
                    addButtons();
                }
            }
        });
    }

    private void addButtons() {
        Button button1 = new Button(this);
        button1.setId(View.generateViewId());
        button1.setText("Button 1");

        Button button2 = new Button(this);
        button2.setId(View.generateViewId());
        button2.setText("Button 2");

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        //button1.setLayoutParams(layoutParams);
        //button2.setLayoutParams(layoutParams);

        constraintLayout.addView(button1);


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
//
        //constraintSet.connect(button2.getId(), ConstraintSet.TOP, R.id.textInputLayout2, ConstraintSet.BOTTOM);
        constraintSet.connect(button1.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(button1.getId(), ConstraintSet.TOP, R.id.textInputLayout2, ConstraintSet.BOTTOM);
        //constraintSet.connect(button2.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
//
        constraintLayout.addView(button2);
        constraintSet.clone(constraintLayout);
        constraintSet.connect(button2.getId(), ConstraintSet.TOP, R.id.textInputLayout2, ConstraintSet.BOTTOM);
        constraintSet.connect(button2.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.applyTo(constraintLayout);
    }
}