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

import com.example.infs3605_group_assignment.Text.MyTexts;
import com.example.infs3605_group_assignment.Text.TextUpload;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TextFavourites extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<TextUpload> options;
    FirebaseRecyclerAdapter<TextUpload, TextFavouritesAdapter> adapter;
    DatabaseReference dataRef;
    private ImageButton mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_favourites);

        // Remove action bar
        getSupportActionBar().hide();

        //Assign variables
        mBackBtn = findViewById(R.id.back_btn11);

        // This will get the userID for like function
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        // this may be wrong***
        dataRef = FirebaseDatabase.getInstance().getReference().child("Favourites/TextList").child(currentUserId);

        recyclerView = findViewById(R.id.recyclerView_text_favourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        LoadData();

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TextFavourites.this, MyTexts.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void LoadData() {

        options = new FirebaseRecyclerOptions.Builder<TextUpload>().setQuery(dataRef, TextUpload.class).build();
        adapter = new FirebaseRecyclerAdapter<TextUpload, TextFavouritesAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TextFavouritesAdapter holder, int position, @NonNull TextUpload model) {

                holder.mTitle.setText(model.getmTitle());
                holder.mLocation.setText(model.getmLocation());
                holder.mNotes.setText(model.getmNotes());
                holder.mDate.setText(model.getmDate());
            }

            @NonNull
            @Override
            public TextFavouritesAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_text_item, parent, false);
                return new TextFavouritesAdapter(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}