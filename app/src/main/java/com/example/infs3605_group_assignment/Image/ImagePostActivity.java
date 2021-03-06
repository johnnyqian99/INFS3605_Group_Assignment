package com.example.infs3605_group_assignment.Image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infs3605_group_assignment.PostFinishActivity;
import com.example.infs3605_group_assignment.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagePostActivity extends AppCompatActivity {

    // Declare variables
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView mImageView;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private ImageButton backBtn;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private TextView mDate;
    private ProgressBar mProgressBar;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;
    private Uri mImageUri;
    long value;
    long imageCounter = 0;

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
        mDate = findViewById(R.id.tv_date);
        mProgressBar = findViewById(R.id.progress_bar);
        backBtn = findViewById(R.id.back_btn11);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpWindow();
            }
        });

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

        //Set up autodate
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String autodate = date.format(new Date());
        mDate.setText(autodate);

    }

    private void popUpWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ImagePostActivity.this);
        builder.setTitle("Unsaved changes");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(ImagePostActivity.this, MyImages.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

                            Intent intent = new Intent(ImagePostActivity.this, PostFinishActivity.class);
                            intent.putExtra("points", "10");
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImagePostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            //adds points
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("Name");
            DatabaseReference myStarRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("stars");
            DatabaseReference mImageCount = database.getReference(FirebaseAuth.getInstance().getUid()).child("imageCount");

            myStarRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    value = (long) snapshot.getValue();
                    value = value + 10;
                    snapshot.getRef().setValue(value);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mImageCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    imageCounter = (long) snapshot.getValue();
                    imageCounter = imageCounter + 1;
                    snapshot.getRef().setValue(imageCounter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Toast.makeText(this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }
    }

}