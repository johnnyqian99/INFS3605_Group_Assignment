package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TextPostActivity extends AppCompatActivity {

    // This will be used for LOG messages
    private static final String TAG = "TextPostActivity";

    // When we save values in Firebase database we also need a KEY for them because all fields in
    // a document are key-value pairs
    private static final String KEY_TITLE = "title";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_DATE = "date";

    // Variables for XML fields
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private EditText mDate;
    private ProgressBar mProgressBar;
    private Button mUpload;

    // Reference to Firestore Database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString();
                String location = mLocation.getText().toString();
                String notes = mNotes.getText().toString();
                String date = mDate.getText().toString();

                // To save these values together with the KEYs in the Firestore Database, we have
                // to put them in some sort of container, can't just pass them directly.
                // A MAP can do this. Map<key type, value type>
                Map<String, Object> upload = new HashMap<>();
                // The order of PUT is the order displayed in database
                upload.put(KEY_TITLE, title);
                upload.put(KEY_LOCATION, location);
                upload.put(KEY_NOTES, notes);
                upload.put(KEY_DATE, date);

                // This creates a new collection, adds a new document and sets the values above
                db.collection("Text Uploads").document().set(upload)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(TextPostActivity.this, "Upload saved", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TextPostActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
            }
        });

    }

}