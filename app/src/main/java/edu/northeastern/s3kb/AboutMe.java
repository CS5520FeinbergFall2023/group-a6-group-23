package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutMe extends AppCompatActivity {
    private TextView aboutMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        aboutMe = findViewById(R.id.tvAboutMeDetails);
        aboutMe.setText("TEAM NAME : S3KB \n TEAM MEMBERS: \n SRI SAI SUSHMITHA KURUMADDALI \n KAUSHIK BOORA \n SAITEJA KURA \n SAHITH KOTA ");
    }
}