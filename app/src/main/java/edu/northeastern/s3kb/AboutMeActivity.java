package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {

    private TextView aboutMeDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        aboutMeDetails = findViewById(R.id.tvAboutMeDetails);
        aboutMeDetails.setText("TEAM NAME : S3KB \n TEAM MEMBERS: \n KAUSHIK BOORA \n SAITEJA KURA \n SAHITH KOTA " +
                "\n SRI SAI SUSHMITHA KURUMADDALI");

    }
}