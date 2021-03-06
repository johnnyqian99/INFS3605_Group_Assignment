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

import com.example.infs3605_group_assignment.Image.ImageUpload;
import com.example.infs3605_group_assignment.Image.MyImages;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ImageFavourites extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<ImageUpload> options;
    FirebaseRecyclerAdapter<ImageUpload, ImageFavouritesAdapter> adapter;
    DatabaseReference dataRef;
    private ImageButton mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_favourites);

        // Remove action bar
        getSupportActionBar().hide();

        //Assign variables
        mBackBtn = findViewById(R.id.back_btn11);

        // This will get the userID for like function
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        // this may be wrong***
        dataRef = FirebaseDatabase.getInstance().getReference().child("Favourites/ImageList").child(currentUserId);

        recyclerView = findViewById(R.id.recyclerView_image_favourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        LoadData();

        // Navigate to MainActivity
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageFavourites.this, MyImages.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void LoadData() {

        options = new FirebaseRecyclerOptions.Builder<ImageUpload>().setQuery(dataRef, ImageUpload.class).build();
        adapter = new FirebaseRecyclerAdapter<ImageUpload, ImageFavouritesAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ImageFavouritesAdapter holder, int position, @NonNull ImageUpload model) {

                holder.mTitle.setText(model.getmTitle());
                holder.mLocation.setText(model.getmLocation());
                holder.mNotes.setText(model.getmNotes());
                holder.mDate.setText(model.getmDate());
                Picasso.get().load(model.getmImageUrl()).into(holder.imageView);
            }

            @NonNull
            @Override
            public ImageFavouritesAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
                return new ImageFavouritesAdapter(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}