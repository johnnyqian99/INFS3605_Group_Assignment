package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TextPostActivity extends AppCompatActivity {

    // Variables for XML fields
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private EditText mDate;
    private ProgressBar mProgressBar;
    private Button mUpload;

    // Database reference
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post);

        mTitle = findViewById(R.id.edit_text_title);
        mLocation = findViewById(R.id.edit_text_location);
        mNotes = findViewById(R.id.edit_text_notes);
        mDate = findViewById(R.id.edit_text_date);
        mProgressBar = findViewById(R.id.progress_bar);
        mUpload = findViewById(R.id.button_upload);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads/Text");

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This helps prevent accidentally clicking upload button multiple times
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(TextPostActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    private void uploadFile() {

        Toast.makeText(TextPostActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

        mProgressBar.setProgress(100);

        // This creates a new image upload object
        TextUpload textUpload = new TextUpload(mTitle.getText().toString().trim(),
                mLocation.getText().toString().trim(),mNotes.getText().toString().trim(),
                mDate.getText().toString().trim());

        // This will create a new entry in the database with a unique ID
        String uploadId = mDatabaseRef.push().getKey();
        // Take the unique ID and set its data to 'imageUpload'
        mDatabaseRef.child(uploadId).setValue(textUpload);
    }

}