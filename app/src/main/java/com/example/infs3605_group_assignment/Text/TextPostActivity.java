package com.example.infs3605_group_assignment.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.infs3605_group_assignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TextPostActivity extends AppCompatActivity {

    // Declare variables
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private EditText mDate;
    private Button mUpload;
    private DatabaseReference databaseReference;
    long value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post);

        // Remove action bar
        getSupportActionBar().hide();

        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Text");

        // Assign variables
        mTitle = findViewById(R.id.et_title);
        mLocation = findViewById(R.id.et_location);
        mNotes = findViewById(R.id.et_notes);
        mDate = findViewById(R.id.et_date);
        mUpload = findViewById(R.id.btn_upload);

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
    }

    // Upload file into firebase database and storage
    private void uploadFile() {

        // Retrieve text in the EditText fields
        String title = mTitle.getText().toString().trim();
        String location = mLocation.getText().toString().trim();
        String notes = mNotes.getText().toString().trim();
        String date = mDate.getText().toString().trim();

        // Ensure that no entry is blank
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(location) &&
                !TextUtils.isEmpty(notes) && !TextUtils.isEmpty(date)) {

            Toast.makeText(TextPostActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

            // This creates a new image upload object
            TextUpload textUpload = new TextUpload(title, location, notes, date);

            // This will create a new entry in the database with a unique ID
            String uploadId = databaseReference.push().getKey();

            // Take the unique ID and set its data to 'imageUpload'
            databaseReference.child(uploadId).setValue(textUpload);

            //adds points
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("Name");
            DatabaseReference myStarRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("stars");

            myStarRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    value = (long) snapshot.getValue();
                    value = value + 20;
                    snapshot.getRef().setValue(value);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // changes screen
            Intent intent = new Intent(TextPostActivity.this, MyTexts.class);
            startActivity(intent);

        } else {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}