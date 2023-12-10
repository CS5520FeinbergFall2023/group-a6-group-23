package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CHANNEL_ID = "channel_id";
    private static final String PREFS_NAME = "app_prefs";
    private static final String LAST_OPENED_KEY = "last_opened";

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
        Button project = findViewById(R.id.start_project);
        project.setOnClickListener(this);

        Button aboutMe = findViewById(R.id.btnAbout);
        aboutMe.setOnClickListener(this);
        createNotificationChannel();
        scheduleAlarm();
    }

    private void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        // Repeat the alarm every minute
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                12*60*60*1000, pIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
        if(view.getId() == R.id.start_project) {
            Intent intent = new Intent(this, ProjectStarterPageActivity.class);
            startActivity(intent);
        }
        if(view.getId() == R.id.btnAbout) {
            Intent intent = new Intent(this, AboutMe.class);
            startActivity(intent);
        }
    }
}