package edu.northeastern.s3kb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickItToEm extends AppCompatActivity {


    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";
    private Spinner usernameSelector;
    private String myUsername;
    private LocalDateTime lastVisited;
    private Map<String, Map<String, String>> usersMap = new HashMap<>();
    private DatabaseReference databaseReference;
    List<String> stickerIdentifiers = Arrays.asList("captainamerica_sticker", "drstrange_sticker", "spiderman_sticker",
            "thor_sticker");

    private RecyclerView stickerRecyclerView;

    private RecyclerView.LayoutManager recycleLayoutManager;
    private StickerAdapter stickerAdapter;

    private Button btnHistory;

    private User userToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");

        recycleLayoutManager = new LinearLayoutManager(this);
        stickerRecyclerView = findViewById(R.id.recycleViewStickers);
        stickerRecyclerView.setHasFixedSize(true);
        stickerAdapter = new StickerAdapter(this, stickerIdentifiers, userName);
        stickerRecyclerView.setAdapter(stickerAdapter);
        stickerRecyclerView.setLayoutManager(recycleLayoutManager);
        btnHistory = findViewById(R.id.btnHistory);

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clickIntent = new Intent(StickItToEm.this, StickersHistory.class);
                clickIntent.putExtra("user", userName);
                startActivity(clickIntent);
            }
        });
        usernameSelector = findViewById(R.id.usernameList);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").get().addOnCompleteListener((task) -> {
            usersMap = (Map) task.getResult().getValue();
            List<String> userData = new ArrayList<>();
            userData.add("Recipient's Username for sticker");
            userData.addAll(usersMap.keySet());
            if(usersMap.containsKey(myUsername)) {
                userData.remove(userData.indexOf(myUsername));
            }
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, userData);
            usernameSelector.setAdapter(adapter);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                stickerAdapter.notifyItemChanged(position);

                String to = usernameSelector.getSelectedItem().toString();
                if("Recipient's Username for sticker".equals(to)){
                    Toast.makeText(StickItToEm.this, "Select Username of recipient", Toast.LENGTH_SHORT).show();
                } else {
                    Sticker sticker = new Sticker(position, myUsername, to, LocalDateTime.now().toString());
                    String id = String.valueOf(LocalDateTime.parse(sticker.getSendTime()).atZone(ZoneId.systemDefault()).toEpochSecond());
                    Thread wt = new Thread(new WorkThread(id, sticker));
                    wt.start();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(stickerRecyclerView);
        // attach a listener to monitor a new sticker creation
        DatabaseReference stickersRef = FirebaseDatabase.getInstance().getReference("stickers");
        stickersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // load current logged in username from global data store
                myUsername = intent.getStringExtra("currentUserName");
                lastVisited = LocalDateTime.parse(intent.getStringExtra("lastVisited"));
                Log.v("Kaushik",lastVisited+"");

                for (DataSnapshot stickerSnapshot : dataSnapshot.getChildren()) {
                    Sticker sticker = stickerSnapshot.getValue(Sticker.class);
                    if (sticker.getTo().equals(myUsername) && LocalDateTime.parse(sticker.getSendTime()).compareTo(lastVisited) > 0) {
//                        sendNotification(sticker);
                    }
                }
                databaseReference.child("users").child(myUsername).setValue(new User(myUsername, LocalDateTime.now().toString()));
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void sendNotification(Sticker sticker) {
        Intent intent = new Intent(this, StickItToEm.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1410, intent, PendingIntent.FLAG_MUTABLE);

        int[] data = {R.drawable.captainamerica_sticker, R.drawable.drstrange_sticker, R.drawable.spiderman_sticker,
                R.drawable.thor_sticker};
        // Create the notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),
                        sticker.getImageId() < 4 ? data[sticker.getImageId()] : R.drawable.captainamerica_sticker)).bigLargeIcon(BitmapFactory.decodeResource(getResources(),
                        sticker.getImageId() < 4 ? data[sticker.getImageId()] : R.drawable.captainamerica_sticker)))
                .setContentTitle("You have a new sticker from " + sticker.getFrom() + "!")
                .setContentText("imageId=" + sticker.getImageId())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        // Set the notification channel and build the notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(0, builder.build());

    }

    class WorkThread implements Runnable {
        private final String id;

        private final Sticker sticker;

        public WorkThread(String id, Sticker sticker) {
            this.id = id;
            this.sticker = sticker;
        }
        @Override
        public void run() {
            databaseReference.child("stickers").child(id).setValue(sticker).addOnSuccessListener(
                    (task) -> {
                        Toast toast = Toast.makeText(StickItToEm.this, "Sticker sent successfully", Toast.LENGTH_SHORT);
                        toast.show();
                    });

        }
    }


}