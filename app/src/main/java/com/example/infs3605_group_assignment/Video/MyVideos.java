package com.example.infs3605_group_assignment.Video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.infs3605_group_assignment.Image.MyImages;
import com.example.infs3605_group_assignment.MainActivity;
import com.example.infs3605_group_assignment.NewPostActivity;
import com.example.infs3605_group_assignment.R;
import com.example.infs3605_group_assignment.Text.MyTexts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyVideos extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Declare variables
    private ImageButton backBtn;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressCircle;
    private Spinner mSpinner;
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference, likesReference;
    Boolean likeChecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_videos);

        // Remove action bar
        getSupportActionBar().hide();

        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Video");
        likesReference = FirebaseDatabase.getInstance().getReference("Uploads/Likes");

        // Assign variables
        backBtn = findViewById(R.id.back_btn);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        progressCircle = findViewById(R.id.progress_circle);
        mSpinner = findViewById(R.id.spinner);

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.videoActivityTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        // Recyclerview
        mRecyclerView = findViewById(R.id.rv_video);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Navigate to MainActivity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyVideos.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Navigate to NewPostActivity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyVideos.this, NewPostActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Populate Recyclerview
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<VideoUpload> options =
                new FirebaseRecyclerOptions.Builder<VideoUpload>()
                        .setQuery(databaseReference, VideoUpload.class)
                        .build();

        FirebaseRecyclerAdapter<VideoUpload, VideoAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<VideoUpload, VideoAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VideoAdapter holder, int position, @NonNull VideoUpload model) {

                        // This will get the userID for like function
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserId = user.getUid();
                        final String postKey = getRef(position).getKey();

                        holder.setExoplayer(getApplication(), model.getmTitle(), model.getmLocation(), model.getmNotes(),
                                model.getmDate(), model.getmVideoUrl());

                        holder.setLikesButtonStatus(postKey);
                        holder.likeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                likeChecker = true;

                                likesReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        // Check if user already liked video or not
                                        if (likeChecker.equals(true)) {
                                            if (snapshot.child(postKey).hasChild(currentUserId)) {
                                                likesReference.child(postKey).child(currentUserId).removeValue();
                                                likeChecker = false;
                                            } else {
                                                likesReference.child(postKey).child(currentUserId).setValue(true);
                                                likeChecker = false;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Can show a Toast message
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public VideoAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.video_item, parent, false);
                        progressCircle.setVisibility(View.INVISIBLE);
                        return new VideoAdapter(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    // Below two methods are for item selected on spinner

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("Text")) {
            Intent intent = new Intent(MyVideos.this, MyTexts.class);
            startActivity(intent);
        }
        if (text.equals("Image")) {
            Intent intent = new Intent(MyVideos.this, MyImages.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
