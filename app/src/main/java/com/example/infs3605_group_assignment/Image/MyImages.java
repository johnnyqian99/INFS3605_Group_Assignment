package com.example.infs3605_group_assignment.Image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.infs3605_group_assignment.MainActivity;
import com.example.infs3605_group_assignment.NewPostActivity;
import com.example.infs3605_group_assignment.Video.MyVideos;
import com.example.infs3605_group_assignment.R;
import com.example.infs3605_group_assignment.Text.MyTexts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyImages extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ImageAdapter.OnItemClickListener {

    // Declare variables
    private ImageButton backBtn;
    private FloatingActionButton floatingActionButton;
    private Spinner mSpinner;
    private ProgressBar mProgressCircle;
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<ImageUpload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_images);

        // Remove action bar
        getSupportActionBar().hide();

        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Image");

        // Assign variables
        backBtn = findViewById(R.id.back_btn);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        mSpinner = findViewById(R.id.spinner);
        mRecyclerView = findViewById(R.id.rv_image);
        mProgressCircle = findViewById(R.id.progress_circle);

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.imageActivityTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        // Recyclerview
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();

        // Navigate to MainActivity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyImages.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Navigate to NewPostActivity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyImages.this, NewPostActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Load data into Recyclerview
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImageUpload imageUpload = postSnapshot.getValue(ImageUpload.class);
                    mUploads.add(imageUpload);
                }

                mAdapter = new ImageAdapter(MyImages.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(MyImages.this);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyImages.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    // Below two methods are for item selected on spinner

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("Text")) {
            Intent intent = new Intent(MyImages.this, MyTexts.class);
            startActivity(intent);
        }
        if (text.equals("Video")) {
            Intent intent = new Intent(MyImages.this, MyVideos.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // USE THIS IF YOU WANT TO DO SOMETHING WITH SINGLE CLICK
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show(); // for testing
    }

    @Override
    public void onDetailClick(int position) {
//        Toast.makeText(this, "Detail click at position: " + position, Toast.LENGTH_SHORT).show(); // for testing
        // Send extras for detail
        Intent intent = new Intent(MyImages.this, ImageDetailActivity.class);
        intent.putExtra("image_url", mUploads.get(position).getmImageUrl());
        intent.putExtra("image_title", mUploads.get(position).getmTitle());
        intent.putExtra("image_location", mUploads.get(position).getmLocation());
        intent.putExtra("image_notes", mUploads.get(position).getmNotes());
        intent.putExtra("image_date", mUploads.get(position).getmDate());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
//        Toast.makeText(this, "Delete click at position: " + position, Toast.LENGTH_SHORT).show(); // for testing
    }
}