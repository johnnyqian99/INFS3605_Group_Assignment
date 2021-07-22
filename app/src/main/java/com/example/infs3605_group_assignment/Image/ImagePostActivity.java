package com.example.infs3605_group_assignment.Image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.infs3605_group_assignment.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ImagePostActivity extends AppCompatActivity {

    // Declare variables
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView mImageView;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private EditText mDate;
    private ProgressBar mProgressBar;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);

        // Remove action bar
        getSupportActionBar().hide();

        storageReference = FirebaseStorage.getInstance().getReference("Uploads/Image");
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Image");

        // Assign variables
        mImageView = findViewById(R.id.image_view);
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTitle = findViewById(R.id.et_title);
        mLocation = findViewById(R.id.et_location);
        mNotes = findViewById(R.id.et_notes);
        mDate = findViewById(R.id.et_date);
        mProgressBar = findViewById(R.id.progress_bar);

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This helps prevent accidentally clicking upload button multiple times
                if (storageTask != null && storageTask.isInProgress()) {
                    Toast.makeText(ImagePostActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }

    // Opens file to select an image
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // This retrieves and displays image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    // Return extension of the file chosen (e.g. jpg)
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // Upload file to firebase database and storage
    private void uploadFile() {

        String title = mTitle.getText().toString().trim();
        String location = mLocation.getText().toString().trim();
        String notes = mNotes.getText().toString().trim();
        String date = mDate.getText().toString().trim();

        if (mImageUri != null && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(location) &&
                !TextUtils.isEmpty(notes) && !TextUtils.isEmpty(date)) {

            mProgressBar.setVisibility(View.VISIBLE);
            // This allocates a unique identifier for a file
            // This also adds image to storage
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            storageTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {

                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ImagePostActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> uriTask = snapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadUri = uriTask.getResult();

                            ImageUpload imageUpload = new ImageUpload(title, location, notes, date, downloadUri.toString());

                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(imageUpload);

                            Intent intent = new Intent(ImagePostActivity.this, MyImages.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImagePostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }
    }

}