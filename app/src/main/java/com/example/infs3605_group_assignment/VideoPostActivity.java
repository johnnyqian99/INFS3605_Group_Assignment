package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class VideoPostActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;

    private VideoView mVideoView;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private EditText mDate;
    private Button mChooseFile;
    private Button mUpload;
    private ProgressBar mProgressBar;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private VideoUpload videoUpload;
    private UploadTask uploadTask;
    private Uri videoUri;

    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);

        mVideoView = findViewById(R.id.video_view);
        mTitle = findViewById(R.id.et_title);
        mLocation = findViewById(R.id.et_location);
        mNotes = findViewById(R.id.et_notes);
        mDate = findViewById(R.id.et_date);
        mChooseFile = findViewById(R.id.btn_choose_file);
        mUpload = findViewById(R.id.btn_upload);
        mProgressBar = findViewById(R.id.progress_bar);

        mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(mVideoView);
        mVideoView.start();

        storageReference = FirebaseStorage.getInstance().getReference("Uploads/Video");
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Video");
        videoUpload = new VideoUpload();

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
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(VideoPostActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    // Opens file to select an image
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    // This retrieves and displays video
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            videoUri = data.getData();
            mVideoView.setVideoURI(videoUri);
        }
    }

    // Return extension of the file chosen (e.g. mp4)
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        String title = mTitle.getText().toString();
        String location = mLocation.getText().toString();
        String notes = mNotes.getText().toString();
        String date = mDate.getText().toString();

        if (videoUri != null && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(location)
                && !TextUtils.isEmpty(notes) && !TextUtils.isEmpty(date)) {

            mProgressBar.setVisibility(View.VISIBLE);
            // This allocates a unique identifier for a file
            // This also adds video to storage
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() +
                    "." + getFileExtension(videoUri));
            uploadTask = fileReference.putFile(videoUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(VideoPostActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

                                videoUpload.setmTitle(title);
                                videoUpload.setmLocation(location);
                                videoUpload.setmNotes(notes);
                                videoUpload.setmDate(date);
                                videoUpload.setmVideoUrl(downloadUrl.toString());
                                String i = databaseReference.push().getKey();
                                databaseReference.child(i).setValue(videoUpload);
                            } else {
                                Toast.makeText(VideoPostActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }
    }

}

