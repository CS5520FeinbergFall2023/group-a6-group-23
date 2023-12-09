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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OwnerLoginFragment#} factory method to
 * create an instance of this fragment.
 */
public class OwnerLoginFragment extends Fragment {

    TextView register;
    Button login;
    EditText email, password;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    ProgressDialog progressDialog;

    public OwnerLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_login, container, false);
        email = view.findViewById(R.id.et_email);
        password = view.findViewById(R.id.et_password);
        login = view.findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(requireContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        return view;
    }
    private void login() {
        String emailStr = this.email.getText().toString();
        String passwordStr = this.password.getText().toString();

        if (emailStr.isEmpty()) {
            this.email.setError("Please Enter Email");
        } else if (!pattern.matcher(emailStr).matches()) {
            this.email.setError("Please Enter a valid Email");
        } else if (passwordStr.isEmpty()) {
            this.password.setError("Please Enter Password");
        }
        //TODO
        else if (passwordStr.length() < 6) {
            this.password.setError("Password should be more than six characters");
        } else {
            progressDialog.setTitle("Logging In");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        navigateOwnerToHomePage();
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void navigateOwnerToHomePage() {
        startActivity(new Intent(requireContext(), LocationFragment.class));

    }
}