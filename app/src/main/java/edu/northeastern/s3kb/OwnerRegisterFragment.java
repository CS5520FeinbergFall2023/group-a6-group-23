package edu.northeastern.s3kb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OwnerRegisterFragment#} factory method to
 * create an instance of this fragment.
 */
public class OwnerRegisterFragment extends Fragment {

    public static final String OWNER = "Owner";
    public static final String HOUSES = "houses";

    TextView login;
    Button register;
    EditText email, password, confirmPassword, username,phoneNumber;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    ProgressDialog progressDialog;

    public OwnerRegisterFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_register, container, false);
        progressDialog = new ProgressDialog(requireContext());
        username = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        register = view.findViewById(R.id.register);
        phoneNumber = view.findViewById(R.id.phoneNo);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });
        return view;
    }

    private void validateInput() {
        String emailStr = this.email.getText().toString();
        String passwordStr = this.password.getText().toString();
        String confirmPasswordStr = this.confirmPassword.getText().toString();
        String username = this.username.getText().toString();
        String phone = this.phoneNumber.getText().toString();

        if (emailStr.isEmpty()) {
            this.email.setError("Please Enter Email");
        } else if (!pattern.matcher(emailStr).matches()) {
            this.email.setError("Please Enter a valid Email");
        }  else if(phone.length() != 10){
            this.phoneNumber.setError("Please Enter a 10 digit phone number");
        }else if (passwordStr.isEmpty()) {
            this.password.setError("Please Enter Password");
        }
        else if (passwordStr.length() < 8) {
            this.password.setError("Password should be more than eight characters");
        } else if (!confirmPasswordStr.equals(passwordStr)) {
            this.confirmPassword.setError("Password doesn't matches");
        } else {
            progressDialog.setTitle("Registering");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String userId = firebaseUser.getUid();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child(OwnerRegisterFragment.OWNER).child(userId);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("email",emailStr);
                        hashMap.put("phoneNumber",phone);
                        hashMap.put("id", userId);
                        hashMap.put("username", username);
                        hashMap.put("imageUrl", "default");
                        hashMap.put("search", username.toLowerCase());

                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    navigateOwnerToHomePage();
                                }
                            }
                        });
                        Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch(FirebaseAuthUserCollisionException e) {

                            Toast.makeText(requireContext(), "Enter a different email", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void navigateOwnerToHomePage() {
        Intent intent = new Intent(requireContext(), AddRentalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}