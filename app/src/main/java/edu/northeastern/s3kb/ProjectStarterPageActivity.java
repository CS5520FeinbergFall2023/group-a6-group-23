package edu.northeastern.s3kb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProjectStarterPageActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    LocationFragment locationFragment = new LocationFragment();
    SeekerFragment seeker = new SeekerFragment();
    OwnerRegisterFragment ownerRegisterFragment = new OwnerRegisterFragment();
    OwnerLoginFragment ownerLoginFragment = new OwnerLoginFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_starter_page);

        initializeBottomNavigationView();

        if(savedInstanceState == null) {
            setNavigationItemSelected(R.id.nav_restaurant);
        }
    }

    private void initializeBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("Rotated", true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return setNavigationItemSelected(item.getItemId());
    }

    private boolean setNavigationItemSelected(int itemId) {
        if (itemId == R.id.nav_restaurant) {
            replaceFragment(locationFragment);
            return true;
        } else if (itemId == R.id.nav_seeker) {
            replaceFragment(seeker);
            return true;
        } else if (itemId == R.id.nav_owner) {
            replaceFragment(ownerRegisterFragment);
            return true;
        } else if (itemId == R.id.nav_login) {
            replaceFragment(ownerLoginFragment);
            return true;
        } else {
            return false;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}