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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyObservationsActivity extends AppCompatActivity {

    // Variables
    private ImageButton mUpload;

    // Images
    private RecyclerView imageRecyclerView;
    private ImageAdapter imageAdapter;
    private DatabaseReference imageDb;
    private List<ImageUpload> imageUploads;

    // Videos
    private RecyclerView videoRecyclerView;
    private DatabaseReference videoDb;

    // Texts
    private RecyclerView textRecyclerView;
    private TextAdapter textAdapter;
    private DatabaseReference textDb;
    private List<TextUpload> textUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_observations);

        mUpload = findViewById(R.id.button_add);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyObservationsActivity.this, NewPostActivity.class);
                startActivity(intent);
            }
        });

        // ***IMAGES
        imageDb = FirebaseDatabase.getInstance().getReference("Uploads/Image");
        imageRecyclerView = findViewById(R.id.recycler_view);
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(MyObservationsActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        imageUploads = new ArrayList<>();

        imageDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImageUpload imageUpload = postSnapshot.getValue(ImageUpload.class);
                    imageUploads.add(imageUpload);
                }

                imageAdapter = new ImageAdapter(MyObservationsActivity.this, imageUploads);

                imageRecyclerView.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyObservationsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // IMAGES***

        // VIDEOS
        videoDb = FirebaseDatabase.getInstance().getReference("Uploads/Video");
        videoRecyclerView = findViewById(R.id.recycler_view2);
        videoRecyclerView.setHasFixedSize(true);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(MyObservationsActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        // ***TEXTS
        textDb = FirebaseDatabase.getInstance().getReference("Uploads/Text");
        textRecyclerView = findViewById(R.id.recycler_view3);
        textRecyclerView.setHasFixedSize(true);
        textRecyclerView.setLayoutManager(new LinearLayoutManager(MyObservationsActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        textUploads = new ArrayList<>();

        textDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    TextUpload textUpload = postSnapshot.getValue(TextUpload.class);
                    textUploads.add(textUpload);
                }

                textAdapter = new TextAdapter(MyObservationsActivity.this, textUploads);

                textRecyclerView.setAdapter(textAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyObservationsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // TEXTS***
    }

    // FOR VIDEOS
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<VideoUpload> options =
                new FirebaseRecyclerOptions.Builder<VideoUpload>()
                .setQuery(videoDb, VideoUpload.class)
                .build();

        FirebaseRecyclerAdapter<VideoUpload, VideoAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<VideoUpload, VideoAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VideoAdapter holder, int position, @NonNull VideoUpload model) {

                        holder.setExoplayer(getApplication(), model.getmTitle(), model.getmVideoUrl());
                    }

                    @Override
                    public VideoAdapter onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.video_item, parent, false);

                        return new VideoAdapter(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        videoRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}