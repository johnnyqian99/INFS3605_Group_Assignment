package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.infs3605_group_assignment.Image.MyImages;
import com.example.infs3605_group_assignment.News.NewsActivity;
import com.example.infs3605_group_assignment.Text.MyTexts;
import com.example.infs3605_group_assignment.Video.MyVideos;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Declare variables
    private TextView mWelcome;
    private ImageButton mOpinion;
    private ImageButton mPhoto;
    private ImageButton mVideo;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove action bar
        getSupportActionBar().hide();

        // Assign variables
        mWelcome = findViewById(R.id.main_title);
        mOpinion = findViewById(R.id.btn_opinion);
        mPhoto = findViewById(R.id.btn_photos);
        mVideo = findViewById(R.id.btn_videos);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set up the profile Token
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProfRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("Name");
        DatabaseReference myStarRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("stars");
        DatabaseReference mTextCount = database.getReference(FirebaseAuth.getInstance().getUid()).child("textCount");
        DatabaseReference mImageCount = database.getReference(FirebaseAuth.getInstance().getUid()).child("imageCount");
        DatabaseReference mVideoCount = database.getReference(FirebaseAuth.getInstance().getUid()).child("videoCount");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        myProfRef.setValue(user.getDisplayName());
        mWelcome.setText("Welcome, " + user.getDisplayName());

        // sets the default stars to 0 if the user has just joined
        myStarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    myStarRef.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mTextCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    mTextCount.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mImageCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    mImageCount.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mVideoCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    mVideoCount.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // ***NAVIGATION BAR

        // Set current selected item
        bottomNavigationView.setSelectedItemId(R.id.post);
        // Set up select listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), PermissionsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(), NewsDemoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.post:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.donate:
                        startActivity(new Intent(getApplicationContext(), RewardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        // NAVIGATION BAR***

        // Navigate to MyTexts
        mOpinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyTexts.class);
                startActivity(intent);
            }
        });

        // Navigate to MyImages
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyImages.class);
                startActivity(intent);
            }
        });

        // Navigate to MyVideos
        mVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyVideos.class);
                startActivity(intent);
            }
        });

    }
}