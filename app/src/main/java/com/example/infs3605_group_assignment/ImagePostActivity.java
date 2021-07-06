package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ImagePostActivity extends AppCompatActivity {

    // This identifier is used for image request
    private static final int PICK_IMAGE_REQUEST = 1;

    // Variables for XML fields
    private ImageView mImageView;
    private Button mChooseFile;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private EditText mDate;
    private ProgressBar mProgressBar;
    private Button mUpload;

    // This will point to the image
    private Uri mImageUri;

    // Storage and database references
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);

        mImageView = findViewById(R.id.image_view);
        mChooseFile = findViewById(R.id.button_choose_file);
        mTitle = findViewById(R.id.edit_text_title);
        mLocation = findViewById(R.id.edit_text_location);
        mNotes = findViewById(R.id.edit_text_notes);
        mDate = findViewById(R.id.edit_text_date);
        mProgressBar = findViewById(R.id.progress_bar);
        mUpload = findViewById(R.id.button_upload);

        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads/Image");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads/Image");

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
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(ImagePostActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }

    // Opens file to select an image                                            --> FIX DEPRECATION
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

    private void uploadFile() {
        // This means we picked an image
        if (mImageUri != null) {
            // This allocates a unique identifier for a file
            // This also adds image to storage
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
            + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {

                            // This will ensure the progress bar is seen when upload is successful
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(ImagePostActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            // This creates a new image upload object
                            ImageUpload imageUpload = new ImageUpload(mTitle.getText().toString().trim(),
                                    mLocation.getText().toString().trim(),mNotes.getText().toString().trim(),
                                    mDate.getText().toString().trim(), snapshot.getUploadSessionUri().toString());

                            // This will create a new entry in the database with a unique ID
                            String uploadId = mDatabaseRef.push().getKey();
                            // Take the unique ID and set its data to 'imageUpload'
                            mDatabaseRef.child(uploadId).setValue(imageUpload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImagePostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}