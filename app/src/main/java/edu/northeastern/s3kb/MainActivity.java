package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton = findViewById(R.id.a6_at_your_service);
        mButton.setOnClickListener(this);
        // firebase_realtime_database Button
        Button fButton = findViewById(R.id.firebase_realtime_database);
        fButton.setOnClickListener(this);
        FirebaseApp.initializeApp(this);

        Button aboutMe = findViewById(R.id.btnAbout);
        aboutMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.a6_at_your_service) {
            Intent intent = new Intent(this, AtYourServiceActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.firebase_realtime_database) {
            Intent intent = new Intent(this, LoginA8Activity.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.btnAbout){
            Intent intent = new Intent(this, AboutMeActivity.class);
            startActivity(intent);
        }

    }
}