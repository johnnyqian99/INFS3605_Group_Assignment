package com.example.infs3605_group_assignment.Video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.infs3605_group_assignment.Comment.CommentsActivity;
import com.example.infs3605_group_assignment.Image.ImageUpload;
import com.example.infs3605_group_assignment.Image.MyImages;
import com.example.infs3605_group_assignment.ImageFavourites;
import com.example.infs3605_group_assignment.MainActivity;
import com.example.infs3605_group_assignment.NewPostActivity;
import com.example.infs3605_group_assignment.R;
import com.example.infs3605_group_assignment.Text.MyTexts;
import com.example.infs3605_group_assignment.VideoFavourites;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyVideos extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Declare variables
    private Button favouritesListBtn;
    private ImageButton backBtn;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressCircle;
    private Spinner mSpinner;
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference, likesReference, favouriteRef, favouriteListRef;
    Boolean likeChecker = false, favouriteChecker = false;
    String mTitle, mLocation, mNotes, mDate, mUrl;

    VideoUpload videoUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_videos);

        // Remove action bar
        getSupportActionBar().hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Video");
        likesReference = FirebaseDatabase.getInstance().getReference("Likes/Video");
        favouriteRef = FirebaseDatabase.getInstance().getReference("Favourites/Video"); // checking if video is saved
        favouriteListRef = FirebaseDatabase.getInstance().getReference("Favourites/VideoList").child(currentUserId); // reference for saving images in new child
        videoUpload = new VideoUpload();

        // Assign variables
        favouritesListBtn = findViewById(R.id.video_favourites_list_button);
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

        // favourites list
        favouritesListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyVideos.this, VideoFavourites.class);
                startActivity(intent);
            }
        });

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

                        // for favourites
                        String title = getItem(position).getmTitle();
                        String location = getItem(position).getmLocation();
                        String notes = getItem(position).getmNotes();
                        String date = getItem(position).getmDate();
                        String videoUrl = getItem(position).getmVideoUrl();

                        holder.favouriteChecker(postKey);
                        holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                favouriteChecker = true;

                                favouriteRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (favouriteChecker.equals(true)) {
                                            if (snapshot.child(postKey).hasChild(currentUserId)) {
                                                favouriteRef.child(postKey).child(currentUserId).removeValue();
                                                delete(title);
                                                favouriteChecker = false;
                                            } else {
                                                favouriteRef.child(postKey).child(currentUserId).setValue(true);
                                                videoUpload.setmTitle(title);
                                                videoUpload.setmLocation(location);
                                                videoUpload.setmNotes(notes);
                                                videoUpload.setmDate(date);
                                                videoUpload.setmVideoUrl(videoUrl);

                                                String id = favouriteListRef.push().getKey();
                                                favouriteListRef.child(id).setValue(videoUpload);
                                                favouriteChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });

                        // For onclick feature
                        holder.setOnClickListener(new VideoAdapter.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                mTitle = getItem(position).getmTitle();
                                mLocation = getItem(position).getmLocation();
                                mNotes = getItem(position).getmNotes();
                                mDate = getItem(position).getmDate();
                                mUrl = getItem(position).getmVideoUrl();
                                Intent intent = new Intent(MyVideos.this, VideoDetailActivity.class);
                                intent.putExtra("video_title", mTitle);
                                intent.putExtra("video_location", mLocation);
                                intent.putExtra("video_notes", mNotes);
                                intent.putExtra("video_date", mDate);
                                intent.putExtra("video_url", mUrl);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                                mTitle = getItem(position).getmTitle();
                                showDeleteDialog(mTitle);
                            }
                        });

                        // For like/comment feature
                        holder.setLikesButtonStatus(postKey);

                        holder.commentButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(MyVideos.this, CommentsActivity.class);
                                intent.putExtra("postkey", postKey); // reference to a particular video
                                startActivity(intent);
                            }
                        });

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

    void delete(String title) {

        Query query = favouriteListRef.orderByChild("mTitle").equalTo(title);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(MyVideos.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    private void showDeleteDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyVideos.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Query query = databaseReference.orderByChild("mTitle").equalTo(title);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                            dataSnapshot1.getRef().removeValue();
                        }
                        Toast.makeText(MyVideos.this, "Video Deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // DO WHATEVER YOU WANT
                    }
                });

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

}
