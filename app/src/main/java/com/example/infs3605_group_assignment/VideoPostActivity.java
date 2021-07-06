package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class VideoPostActivity extends AppCompatActivity {

    // This identifier is used for video request
    private static final int PICK_VIDEO_REQUEST = 1;

    // Variables for XML fields
    private VideoView mVideoView;
    private Button mChooseFile;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private EditText mDate;
    private ProgressBar mProgressBar;
    private Button mUpload;

    // This will point to the video
    private Uri mVideoUri;

    // Storage and database references
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);

        mVideoView = findViewById(R.id.video_view);
        mChooseFile = findViewById(R.id.button_choose_file);
        mTitle = findViewById(R.id.edit_text_title);
        mLocation = findViewById(R.id.edit_text_location);
        mNotes = findViewById(R.id.edit_text_notes);
        mDate = findViewById(R.id.edit_text_date);
        mProgressBar = findViewById(R.id.progress_bar);
        mUpload = findViewById(R.id.button_upload);

        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads/Video");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads/Video");

        mediaController = new MediaController(this);

        mVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(mVideoView);
        mVideoView.start();

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
                    Toast.makeText(VideoPostActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    // Opens file to select an image                                            --> FIX DEPRECATION
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    // This retrieves and displays image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mVideoUri = data.getData();

            mVideoView.setVideoURI(mVideoUri);
        }
    }

    // Return extension of the file chosen (e.g. mp4)
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        // This means we picked a video
        if (mVideoUri != null) {
            // This allocates a unique identifier for a file
            // This also adds video to storage
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mVideoUri));

            mUploadTask = fileReference.putFile(mVideoUri)
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

                            Toast.makeText(VideoPostActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            // This creates a new video upload object
                            VideoUpload videoUpload = new VideoUpload(mTitle.getText().toString().trim(),
                                    mLocation.getText().toString().trim(),mNotes.getText().toString().trim(),
                                    mDate.getText().toString().trim(), snapshot.getUploadSessionUri().toString());

                            // This will create a new entry in the database with a unique ID
                            String uploadId = mDatabaseRef.push().getKey();
                            // Take the unique ID and set its data to 'videoUpload'
                            mDatabaseRef.child(uploadId).setValue(videoUpload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VideoPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

