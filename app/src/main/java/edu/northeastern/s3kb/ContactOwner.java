//Reference : https://developer.android.com/training/basics/intents/sending
//https://stackoverflow.com/questions/2734749/opening-an-email-client-on-clicking-a-button
package edu.northeastern.s3kb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Objects;

public class ContactOwner extends AppCompatActivity {

    private TextView ownName;
    private TextView ownEmail;
    private TextView phoneNumber;
    private String houseId;
    private String houseDescription;
    private String location;
    private String address;
    private DatabaseReference databaseReference, databaseReference2;
    private FirebaseDatabase firebaseDatabase;
    private String user;
    private Button send;
    private String subject;
    private String body;
    private String userKey;
    private Preference currentUserPreference;
    private String name,gender,userNumber,email;
    private String x, y,i, j;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_owner);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("");
        databaseReference2 = firebaseDatabase.getReference("");

        Intent intent = getIntent();
        houseId = intent.getStringExtra("houseId");
        houseDescription = intent.getStringExtra("houseDescription");
        location = intent.getStringExtra("houseLocation");
        address = intent.getStringExtra("address");
        userKey = intent.getStringExtra("userKey");
        ownName = findViewById(R.id.ownerName);
        ownEmail = findViewById(R.id.ownerEmail);
        phoneNumber = findViewById(R.id.phoneNumber);
        send = findViewById(R.id.sendMail);

        databaseReference2.child("seekers").child(userKey).child("myPreference").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserPreference = snapshot.getValue(Preference.class);
                assert currentUserPreference != null;
                x = currentUserPreference.getFullName();
                if(x!= null){
                    name = x;
                }
                y = currentUserPreference.getPhoneNumber();
                if(y!= null){
                    userNumber = y;
                }
                i = currentUserPreference.getEmailID();
                if(i!= null){
                    email = i;
                }
                j = currentUserPreference.getLegalSex();
                if(j!= null){
                    gender = j;
                }
                body = String.format("Hey Next Rent Manager, %s I really loved your property at %s. I would like to submit an application for this property. %sThe following are my application details :%sFull Name : %s Gender : You can reach me at : %s Or email me at: %sThank you, Cheers", System.lineSeparator()+System.lineSeparator(), address, System.lineSeparator()+System.lineSeparator(), System.lineSeparator()+System.lineSeparator(),x +System.lineSeparator(), j+System.lineSeparator(), y+System.lineSeparator(), i+System.lineSeparator()+System.lineSeparator());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("houses").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {

                    Iterable<DataSnapshot> snapshotIterator2 = iterator.next().getChildren();
                    Iterator<DataSnapshot> iterator2 = snapshotIterator2.iterator();


                    while (iterator2.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator2.next();
                        user = String.valueOf(next.child("houseId").getValue());
                        if(Objects.equals(user, houseId)) {
                            String id = String.valueOf(next.child("userId").getValue());
                            DataSnapshot d  = dataSnapshot.child("Owner").child(id);
                            String username = String.valueOf(d.child("username").getValue());
                            ownName.setText(username);
                            String emailId = String.valueOf(d.child("email").getValue());
                            ownEmail.setText(emailId);
                            String phone = String.valueOf(d.child("phoneNumber").getValue());
                            phoneNumber.setText(phone);
                        }

                        Property article = new Property(String.valueOf(next.child("houseId").getValue()),
                                String.valueOf(next.child("noOfRoom").getValue()),
                                String.valueOf(next.child("rentPerRoom").getValue()),
                                String.valueOf(next.child("houseDescription").getValue()),
                                String.valueOf(next.child("houseLocation").getValue()),
                                String.valueOf(next.child("houseImage").getValue()),
                                String.valueOf(next.child("userId").getValue()),
                                String.valueOf(next.child("country").getValue()),
                                String.valueOf(next.child("state").getValue()),
                                String.valueOf(next.child("type").getValue()),String.valueOf(next.child("baths")),
                                String.valueOf(next.child("address")));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        subject = String.format("I am interested in your property at %s", address);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ownEmail != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:" + ownEmail.getText().toString() + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body));
                    intent.setData(data);
                    startActivity(intent);
                }
            }
        });

    }


}