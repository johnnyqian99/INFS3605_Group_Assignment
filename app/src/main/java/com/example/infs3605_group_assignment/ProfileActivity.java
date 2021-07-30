package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.infs3605_group_assignment.News.NewsActivity;
import com.example.infs3605_group_assignment.Text.MyTexts;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements ValueEventListener {

    private BottomNavigationView bottomNavigationView;
//    private ImageButton backBtn;
    private Button termsBtn;
    private Button faqBtn;
    private Button mapBtn;
    private TextView textCount;
    private TextView imageCount;
    private TextView videoCount;
    private TextView mName;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mTextCount = database.getReference(FirebaseAuth.getInstance().getUid()).child("textCount");
    DatabaseReference mImageCount = database.getReference(FirebaseAuth.getInstance().getUid()).child("imageCount");
    DatabaseReference mVideoCount = database.getReference(FirebaseAuth.getInstance().getUid()).child("videoCount");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Remove action bar
        getSupportActionBar().hide();

        // Assign variables
        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        backBtn = findViewById(R.id.back_btn8);
        termsBtn = findViewById(R.id.terms_btn);
        faqBtn = findViewById(R.id.faq_btn);
        textCount = findViewById(R.id.text_post_count);
        imageCount = findViewById(R.id.image_post_count);
        videoCount = findViewById(R.id.video_post_count);
        mName = findViewById(R.id.tv_name);
        mapBtn = findViewById(R.id.bMap);

        // set name
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        mName.setText(user.getDisplayName());

        // set terms button
        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, TermsAndConditions.class);
                startActivity(intent);
            }
        });

        // set terms button
        faqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FAQActivity.class);
                startActivity(intent);
            }
        });

        // ***NAVIGATION BAR

        // Set current selected item
        bottomNavigationView.setSelectedItemId(R.id.profile);
        // Set up select listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.reward:
                        startActivity(new Intent(getApplicationContext(), RewardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        return true;
                }

                return false;
            }
        });

         //Map Button
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // NAVIGATION BAR***
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        if (snapshot.getValue(Integer.class) != null) {
            String key = snapshot.getKey();
            if (key.equals("textCount")) {
                int text = snapshot.getValue(Integer.class);

                textCount.setText("" + text);
            }

            if (key.equals("imageCount")) {
                int text = snapshot.getValue(Integer.class);

                imageCount.setText("" + text);
            }

            if (key.equals("videoCount")) {
                int text = snapshot.getValue(Integer.class);

                videoCount.setText("" + text);
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        mTextCount.addValueEventListener(this);
        mImageCount.addValueEventListener(this);
        mVideoCount.addValueEventListener(this);
    }
}