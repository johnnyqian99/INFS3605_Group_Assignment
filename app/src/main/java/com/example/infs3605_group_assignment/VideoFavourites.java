package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.infs3605_group_assignment.Video.MyVideos;
import com.example.infs3605_group_assignment.Video.VideoUpload;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VideoFavourites extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    private ImageButton mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_favourites);

        // Remove action bar
        getSupportActionBar().hide();

        //Assign variables
        mBackBtn = findViewById(R.id.back_btn11);

        // This will get the userID for like function
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Favourites/VideoList").child(currentUserId);

        recyclerView = findViewById(R.id.recyclerView_video_favourites);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Navigate to MainActivity
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoFavourites.this, MyVideos.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<VideoUpload> options =
                new FirebaseRecyclerOptions.Builder<VideoUpload>()
                .setQuery(databaseReference, VideoUpload.class)
                .build();

        FirebaseRecyclerAdapter<VideoUpload, VideoFavouritesAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<VideoUpload, VideoFavouritesAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VideoFavouritesAdapter holder, int position, @NonNull VideoUpload model) {

                        holder.setExoplayer(getApplication(), model.getmTitle(), model.getmLocation(), model.getmNotes(), model.getmDate(), model.getmVideoUrl());

                    }

                    @NonNull
                    @Override
                    public VideoFavouritesAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_video_item, parent, false);
                        return new VideoFavouritesAdapter(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}