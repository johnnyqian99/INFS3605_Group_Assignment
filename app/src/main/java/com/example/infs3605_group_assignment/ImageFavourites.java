package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infs3605_group_assignment.Image.ImageAdapter;
import com.example.infs3605_group_assignment.Image.ImageUpload;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_favourites);

        // Remove action bar
        getSupportActionBar().hide();

        // This will get the userID for like function
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        // this may be wrong***
        dataRef = FirebaseDatabase.getInstance().getReference().child("Favourites/ImageList").child(currentUserId);

        recyclerView = findViewById(R.id.recyclerView_image_favourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        LoadData();
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