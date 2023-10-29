package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Map;

public class LoginA8Activity extends AppCompatActivity {

    DatabaseReference databaseReference;

    User user;

    private EditText userNameEdt;
    String userKey;

    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_a8);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        userNameEdt = findViewById(R.id.username);

        loginBtn = findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameEdt.getText().toString();
                databaseReference.child(userName).get().addOnCompleteListener((node)->{
                    Map<String, Object> userMap = (Map<String, Object>) node.getResult().getValue();
                    User user;
                    String date;
                    if (userMap == null) {
                        user = new User(userName, LocalDateTime.now().toString());
                        date = LocalDateTime.now().toString();
                    } else {
                        user = new User(userName, userMap.get("lastVisited").toString());
                        date = userMap.get("lastVisited").toString();
                    }
                    databaseReference.child(userName).setValue(user);
                    Intent clickIntent = new Intent(LoginA8Activity.this, StickItToEm.class);
                    clickIntent.putExtra("currentUserName", userName);
                    clickIntent.putExtra("lastVisited", date.toString());
                    startActivity(clickIntent);
                });

            }

        });
    }
}