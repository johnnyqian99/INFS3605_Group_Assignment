package com.example.infs3605_group_assignment.Video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.infs3605_group_assignment.R;
import com.example.infs3605_group_assignment.Text.MyTexts;
import com.example.infs3605_group_assignment.Text.TextPostActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoPostActivity extends AppCompatActivity {

    // Declare variables
    private static final int PICK_VIDEO_REQUEST = 1;
    private VideoView mVideoView;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mNotes;
    private TextView mDate;
    private Button mChooseFile;
    private Button mUpload;
    private ImageButton backBtn;
    private ProgressBar mProgressBar;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private VideoUpload videoUpload;
    private UploadTask uploadTask;
    private Uri videoUri;
    MediaController mediaController;
    long value;
    long videoCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);

        // Remove action bar
        getSupportActionBar().hide();

        storageReference = FirebaseStorage.getInstance().getReference("Uploads/Video");
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Video");

        // Assign variables
        mVideoView = findViewById(R.id.video_view);
        mTitle = findViewById(R.id.et_title);
        mLocation = findViewById(R.id.et_location);
        mNotes = findViewById(R.id.et_notes);
        mDate = findViewById(R.id.tv_date);
        mChooseFile = findViewById(R.id.btn_choose_file);
        mUpload = findViewById(R.id.btn_upload);
        mProgressBar = findViewById(R.id.progress_bar);
        backBtn = findViewById(R.id.back_btn);

        mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(mVideoView);
        mVideoView.start();

        videoUpload = new VideoUpload();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpWindow();
            }
        });

        // Open file to choose media from
        mChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Upload data
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

        //Set up autodate
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String autodate = date.format(new Date());
        mDate.setText(autodate);
    }

    private void popUpWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPostActivity.this);
        builder.setTitle("Unsaved changes");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(VideoPostActivity.this, MyVideos.class);
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

            // check if there is a file selected when posting
            try {
                videoUri = data.getData();
                mVideoView.setVideoURI(videoUri);
            } catch (Exception e) {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // Return extension of the file chosen (e.g. mp4)
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // Upload data into firebase database and storage
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

                                Intent intent = new Intent(VideoPostActivity.this, MyVideos.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(VideoPostActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            //adds points
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("Name");
            DatabaseReference myStarRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("stars");
            DatabaseReference mVideoCount = database.getReference(FirebaseAuth.getInstance().getUid()).child("videoCount");

            myStarRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    value = (long) snapshot.getValue();
                    value = value + 30;
                    snapshot.getRef().setValue(value);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mVideoCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    videoCounter = (long) snapshot.getValue();
                    videoCounter = videoCounter + 1;
                    snapshot.getRef().setValue(videoCounter);
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

