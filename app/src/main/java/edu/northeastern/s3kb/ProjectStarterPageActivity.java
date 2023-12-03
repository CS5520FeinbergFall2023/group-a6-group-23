package edu.northeastern.s3kb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProjectStarterPageActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_starter_page);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        if(savedInstanceState == null) {
            bottomNavigationView
                    .setOnNavigationItemSelectedListener(this);
            bottomNavigationView.setSelectedItemId(R.id.nav_restaurant);
        }
    }
    LocationFragment locationFragment = new LocationFragment();
    SeekerFragment seeker = new SeekerFragment();
    OwnerRegisterFragment ownerRegisterFragment = new OwnerRegisterFragment();
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("Rotated", true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.nav_restaurant) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, locationFragment)
                    .commit();
            return true;
        }
        if(item.getItemId() == R.id.nav_seeker) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, seeker)
                    .commit();
            return true;
        }
        if(item.getItemId() == R.id.nav_owner) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, ownerRegisterFragment)
                    .commit();
            return true;
        }
        return false;
    }
}