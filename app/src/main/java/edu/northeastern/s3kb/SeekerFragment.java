package edu.northeastern.s3kb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SeekerFragment extends Fragment {
    // creating a variable for Firebase Database.
    private FirebaseDatabase firebaseDatabase;

    // creating a variable for reference for Firebase.
    private DatabaseReference databaseReference;
    private Button login;
    private EditText userName;
    private SeekerUser user;
    private String userKey;
    public SeekerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seeker, container, false);

        // instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for the database.
        databaseReference = firebaseDatabase.getReference("");
        this.userName = view.findViewById(R.id.project_seeker_login_username);
        this.login = view.findViewById(R.id.project_seeker_login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // below line is for checking whether the
                // edittext fields are empty or not.
                if (userName.getText().toString().length() == 0) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(requireContext(), "Please enter user name!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    user = new SeekerUser(userName.getText().toString(), "seekers");
                    addDataToFirebase(user);
                }

            }
        });

        return view;
    }


    private void addDataToFirebase(SeekerUser user) {
        //flag to check if user was created.
        boolean[] created = {true};
        boolean[] exists = {false};

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.child(user.getUserType()).getChildren()) {
                    if (user.getUserName().equalsIgnoreCase(data.child("userName").getValue().toString())) {
                        //user name exists
                        userKey = data.getKey();
                        exists[0] = true;
                        break;
                    }
                }

                if (!exists[0]) {
                    //user name does not exists, create new
                    // data base reference will sends data to firebase.
                    DatabaseReference db = databaseReference.child(user.getUserType()).push();
                    userKey = db.getKey();
                    db.setValue(user).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            created[0] = false;
                            Toast.makeText(requireContext(), "Could not Add User, Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    created[0] = false;
                }
                if (created[0]) {
                    Toast.makeText(requireContext(), "Added new Property Seeker!", Toast.LENGTH_SHORT).show();

                    if("seekers".equalsIgnoreCase(user.getUserType())){
                        Intent clickIntent = new Intent(requireContext(), BasicQuestionsActivity.class);
                        clickIntent.putExtra("userKey", userKey);
                        startActivity(clickIntent);
                    }

                } else {
                    Toast.makeText(requireContext(), "logged in successfully!", Toast.LENGTH_SHORT).show();

                    if("seekers".equalsIgnoreCase(user.getUserType())){
                        Intent clickIntent = new Intent(requireContext(), PropertySeekerActivity.class);
                        clickIntent.putExtra("userKey", userKey);
                        startActivity(clickIntent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("KAUSHIK", "The read failed: " + error.getMessage());
                System.out.println(error.getMessage());
            }
        });

    }
}