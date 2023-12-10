package edu.northeastern.s3kb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class AddRentalImagesActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private StorageReference storageReference;

    private EditText bedroomsEt, bathroomsEt, houseDescriptionEt, rentEt;

    private static final int PICK_IMAGE_REQUEST = 1;

    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    private Button submitBtn;

    private Uri imageUri;

    private Button uploadBtn;

    private ImageView houseImageView;

    private String imageStringValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rental_images);
        uploadBtn = findViewById(R.id.uploadBtn);
        houseImageView = findViewById(R.id.addPicsImageView);
        submitBtn = findViewById(R.id.submitBtn);
        bedroomsEt = findViewById(R.id.bedroomsEt);
        bathroomsEt = findViewById(R.id.bathroomsEt);
        houseDescriptionEt = findViewById(R.id.houseDescriptionEt);
        rentEt = findViewById(R.id.rentEt);
        String country = getIntent().getExtras().getString("country");
        String state = getIntent().getExtras().getString("state");
        String city = getIntent().getExtras().getString("city");
        String address = getIntent().getExtras().getString("address");
        String type = getIntent().getExtras().getString("type");

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String houseId = Long.toString(System.currentTimeMillis());
                String noOfRoom = bedroomsEt.getText().toString();
                String rentPerRoom = rentEt.getText().toString();
                String houseDescription = houseDescriptionEt.getText().toString();
                String baths = bathroomsEt.getText().toString();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                String userId = firebaseUser.getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("houses").child(userId);
                House house = new House(
                        houseId, noOfRoom, houseDescription,
                        city, rentPerRoom, userId,
                        country, state, type,
                        baths, address, imageStringValue);
                databaseReference.child(houseId).setValue(house);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri tempUri = data.getData();
            imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(tempUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                houseImageView.setImageBitmap(bitmap);
                houseImageView.setVisibility(View.VISIBLE);
                try {
                    uploadImage();
                } catch (Exception e){
                    Toast.makeText(AddRentalImagesActivity.this, "Upload unsuccessful", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            ContentResolver contentResolver = AddRentalImagesActivity.this.getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
            storageReference = FirebaseStorage.getInstance().getReference().child("Uploads");
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + extension);
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    handleSuccessfulUpload(task);
                } else {
                    handleFailedUpload();
                }
            }).addOnFailureListener(e -> {
                handleFailure(e);
            });
        } else {
            Toast.makeText(AddRentalImagesActivity.this, "Image Not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSuccessfulUpload(Task<Uri> task) {
        try {
            imageStringValue = task.getResult().toString();
        } catch (Exception e) {
            Toast.makeText(AddRentalImagesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFailedUpload() {
        Toast.makeText(AddRentalImagesActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
    }

    private void handleFailure(Exception e) {
        Toast.makeText(AddRentalImagesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}