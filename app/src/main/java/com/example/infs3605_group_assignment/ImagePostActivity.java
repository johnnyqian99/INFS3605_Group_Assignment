package com.example.infs3605_group_assignment;

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

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView mImageView;
    private Button mChooseFile;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private EditText mDate;
    private ProgressBar mProgressBar;
    private Button mUpload;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);

        mImageView = findViewById(R.id.image_view);
        mChooseFile = findViewById(R.id.btn_choose_file);
        mTitle = findViewById(R.id.et_title);
        mLocation = findViewById(R.id.et_location);
        mNotes = findViewById(R.id.et_notes);
        mDate = findViewById(R.id.et_date);
        mProgressBar = findViewById(R.id.progress_bar);
        mUpload = findViewById(R.id.btn_upload);

        storageReference = FirebaseStorage.getInstance().getReference("Uploads/Image");
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Image");

        mChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mUpload.setOnClickListener(new View.OnClickListener() {
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

            uri = data.getData();
            Picasso.get().load(uri).into(mImageView);
        }
    }

    // Return extension of the file chosen (e.g. jpg)
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        String title = mTitle.getText().toString().trim();
        String location = mLocation.getText().toString().trim();
        String notes = mNotes.getText().toString().trim();
        String date = mDate.getText().toString().trim();

        if (uri != null || !TextUtils.isEmpty(title) || !TextUtils.isEmpty(location) ||
                !TextUtils.isEmpty(notes) || !TextUtils.isEmpty(date)) {

            mProgressBar.setVisibility(View.VISIBLE);
            // This allocates a unique identifier for a file
            // This also adds image to storage
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(uri));

            storageTask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {

                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ImagePostActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = snapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();

                            ImageUpload imageUpload = new ImageUpload(title, location, notes, date, downloadUrl.toString());

                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(imageUpload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImagePostActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }
    }

}