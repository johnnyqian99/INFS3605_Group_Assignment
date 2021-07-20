package com.example.infs3605_group_assignment.Text;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.infs3605_group_assignment.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TextPostActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private EditText mDate;
    private Button mUpload;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post);

        mTitle = findViewById(R.id.et_title);
        mLocation = findViewById(R.id.et_location);
        mNotes = findViewById(R.id.et_notes);
        mDate = findViewById(R.id.et_date);
        mUpload = findViewById(R.id.btn_upload);

        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Text");

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
    }

    private void uploadFile() {

        String title = mTitle.getText().toString().trim();
        String location = mLocation.getText().toString().trim();
        String notes = mNotes.getText().toString().trim();
        String date = mDate.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(location) &&
                !TextUtils.isEmpty(notes) && !TextUtils.isEmpty(date)) {

            Toast.makeText(TextPostActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

            // This creates a new image upload object
            TextUpload textUpload = new TextUpload(title, location, notes, date);

            // This will create a new entry in the database with a unique ID
            String uploadId = databaseReference.push().getKey();

            // Take the unique ID and set its data to 'imageUpload'
            databaseReference.child(uploadId).setValue(textUpload);

            Intent intent = new Intent(TextPostActivity.this, MyTexts.class);
            startActivity(intent);

        } else {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

}