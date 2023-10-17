package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AtYourServiceActivity extends AppCompatActivity {

    private String[] dropDownContent = new String[]{"Islamic Interbank Money Market(IIMM)", "Financial and Capital Markets",
            "Bond Info Hub(BIH)", "Financial Inclusion"};
    private ConstraintLayout constraintLayout;

    private String selectedItem;

    private Executor executor = Executors.newSingleThreadExecutor();

    private ProgressBar progressBar;

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

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = adapterView.getItemAtPosition(i).toString();
                if("Islamic Interbank Money Market(IIMM)".equals(selectedItem)) {
                    addIIMMButtons();
                }

                if("Bond Info Hub(BIH)".equals(selectedItem)) {
                    addButtonsBondInfoHub();
                }
                if("Financial and Capital Markets".equals(selectedItem)){
                    addFinancialCapitalMarkets();
                }
                if("Financial Inclusion".equals(selectedItem)){
                    addFinancialInclusion();
                }
            }
        });

    }

    private void addIIMMButtons() {
        constraintLayout.removeAllViews();
        Button button1 = new Button(this);
        button1.setId(View.generateViewId());
        button1.setText("Summary Transaction");
        button1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        button1.setPadding(50, 50, 50, 50);
        button1.setBackgroundColor(0xFF673AB7);
        button1.setTextColor(Color.WHITE);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://api.bnm.gov.my/public/iimm/summary-transaction";
                makeAPICallWithLoadingIndicator(url, SummaryTransactionActivity.class, "summary-transaction");
            }

        });

        Button button2 = new Button(this);
        button2.setId(View.generateViewId());
        button2.setText("Interbank Transactions");
        button2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        button2.setPadding(50, 50, 50, 50);
        button2.setBackgroundColor(0xFF673AB7);
        button2.setTextColor(Color.WHITE);



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://api.bnm.gov.my/public/iimm/interbank-transactions";
                makeAPICallWithLoadingIndicator(url, InterbankTransactionActivity.class, "summary-transaction");
            }

        });

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
        constraintLayout.removeAllViews();
        Button btnHeatMap = new Button(this);
        btnHeatMap.setId(View.generateViewId());
        btnHeatMap.setText("HeatMap");
        btnHeatMap.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        btnHeatMap.setPadding(50, 50, 50, 50);
        btnHeatMap.setBackgroundColor(0xFF673AB7);
        btnHeatMap.setTextColor(Color.WHITE);

        btnHeatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://api.bnm.gov.my/public/bih/heatmap";
                makeAPICallWithLoadingIndicator(url, BihHeatMapActivity.class, "heatmap");
            }

        });

        Button btnTradingActivities = new Button(this);
        btnTradingActivities.setId(View.generateViewId());
        btnTradingActivities.setText("Trading Activities");
        btnTradingActivities.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        btnTradingActivities.setPadding(50, 50, 50, 50);
        btnTradingActivities.setBackgroundColor(0xFF673AB7);
        btnTradingActivities.setTextColor(Color.WHITE);

        btnTradingActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://api.bnm.gov.my/public/bih/trading-activities";
                makeAPICallWithLoadingIndicator(url, BihTradingActivities.class, "trading");
            }

        });


        constraintLayout.addView(btnHeatMap);


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(btnHeatMap.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(btnHeatMap.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(btnHeatMap.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        constraintSet.applyTo(constraintLayout);
        constraintLayout.addView(btnTradingActivities);
        constraintSet.clone(constraintLayout);
        constraintSet.connect(btnHeatMap.getId(), ConstraintSet.BOTTOM, btnTradingActivities.getId(), ConstraintSet.TOP);
        constraintSet.applyTo(constraintLayout);
        constraintSet.connect(btnTradingActivities.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(btnTradingActivities.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(btnTradingActivities.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        constraintSet.connect(btnTradingActivities.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);

    }

    private void makeAPICallWithLoadingIndicator(String url, Class<?> targetActivity, String intentExtra) {
        progressBar.setVisibility(View.VISIBLE);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(AtYourServiceActivity.this, targetActivity);
                        intent.putExtra(intentExtra, url);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    private void addFinancialCapitalMarkets(){
        constraintLayout.removeAllViews();
        Button msb = new Button(this);
        msb.setId(View.generateViewId());
        msb.setText("MSB");
        msb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        msb.setPadding(50, 50, 50, 50);
        msb.setBackgroundColor(0xFF673AB7);
        msb.setTextColor(Color.WHITE);

        msb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://api.bnm.gov.my/public/msb/2.16";
                makeAPICallWithLoadingIndicator(url, FinancialAndCapitalMarketsActivity.class, "MSB");
            }
        });


        constraintLayout.addView(msb);


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(msb.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(msb.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(msb.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        constraintSet.connect(msb.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);
    }

    private void addFinancialInclusion(){
        constraintLayout.removeAllViews();
        Button finInc = new Button(this);
        finInc.setId(View.generateViewId());
        finInc.setText("FI");
        finInc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        finInc.setPadding(50, 50, 50, 50);
        finInc.setBackgroundColor(0xFF673AB7);
        finInc.setTextColor(Color.WHITE);

        finInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://api.bnm.gov.my/public/financial_inclusion/1.11";
                makeAPICallWithLoadingIndicator(url, FinancialInclusionActivity.class, "FI");
            }
        });
        constraintLayout.addView(finInc);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(finInc.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(finInc.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(finInc.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        constraintSet.connect(finInc.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);
    }
}