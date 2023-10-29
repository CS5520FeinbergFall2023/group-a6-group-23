package edu.northeastern.s3kb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginA8Activity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    private EditText userNameEdt;
     private Button loginBtn;
    private static String TAG = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_a8);

        userNameEdt = findViewById(R.id.loginEditText);

        loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = userNameEdt.getText().toString();
                Intent clickIntent = new Intent(LoginA8Activity.this, StickItToEm.class);
                clickIntent.putExtra("userName", userName);
                startActivity(clickIntent);
            }

        });
    }
}