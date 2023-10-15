package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AtYourServiceActivity extends AppCompatActivity {

    private String[] dropDownContent = new String[]{"Islamic Interbank Money Market(IIMM)", "Kura's_choice",
            "Bond Info Hub(BIH)", "Kota's Choice"};
    private ConstraintLayout constraintLayout;

    private String selectedItem;

    private Executor executor = Executors.newSingleThreadExecutor();

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private String url;
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
                selectedItem = adapterView.getItemAtPosition(i).toString();
                if("Islamic Interbank Money Market(IIMM)".equals(selectedItem)) {
                    Log.v("Kaushik", "Here");
                    addIIMMButtons();
                }

                if("Bond Info Hub(BIH)".equals(selectedItem)) {
                    addButtonsBondInfoHub();
                }
            }
        });

    }

    private void addIIMMButtons() {
        Button button1 = new Button(this);
        button1.setId(View.generateViewId());
        button1.setText("Button 1");
        button1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        button1.setPadding(50, 50, 50, 50);
        button1.setBackgroundColor(0xFF673AB7);
        button1.setTextColor(Color.WHITE);

        Button button2 = new Button(this);
        button2.setId(View.generateViewId());
        button2.setText("Button 2");
        button2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        button2.setPadding(50, 50, 50, 50);
        button2.setBackgroundColor(0xFF673AB7);
        button2.setTextColor(Color.WHITE);

        constraintLayout.addView(button1);


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(button1.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(button1.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(button1.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        constraintSet.applyTo(constraintLayout);
        constraintLayout.addView(button2);
        constraintSet.clone(constraintLayout);
        constraintSet.connect(button1.getId(), ConstraintSet.BOTTOM, button2.getId(), ConstraintSet.TOP);
        constraintSet.applyTo(constraintLayout);
        constraintSet.connect(button2.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(button2.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(button2.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        constraintSet.connect(button2.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);

    }

    private void addButtonsBondInfoHub() {
        Button btnHeatMap = new Button(this);
        btnHeatMap.setId(View.generateViewId());
        btnHeatMap.setText("HeatMap");

        btnHeatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://api.bnm.gov.my/public/bih/heatmap";
                Intent intent = new Intent(AtYourServiceActivity.this, BihHeatMapActivity.class);
                intent.putExtra("heatmap", url);
                startActivity(intent);
            }

        });

        Button btnTradingActivities = new Button(this);
        btnTradingActivities.setId(View.generateViewId());
        btnTradingActivities.setText("Trading Activities");

        btnTradingActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://api.bnm.gov.my/public/bih/trading-activities";
                Intent intent = new Intent(AtYourServiceActivity.this, BihHeatMapActivity.class);
                intent.putExtra("heatmap", url);
                startActivity(intent);
            }

        });

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        constraintLayout.addView(btnHeatMap);


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(btnHeatMap.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(btnHeatMap.getId(), ConstraintSet.TOP, R.id.textInputLayout2, ConstraintSet.BOTTOM);

        constraintLayout.addView(btnTradingActivities);
        constraintSet.clone(constraintLayout);
        constraintSet.connect(btnTradingActivities.getId(), ConstraintSet.TOP, R.id.textInputLayout2, ConstraintSet.BOTTOM);
        constraintSet.connect(btnTradingActivities.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.applyTo(constraintLayout);

    }
}