package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.infs3605_group_assignment.Image.MyImages;
import com.example.infs3605_group_assignment.News.NewsActivity;
import com.example.infs3605_group_assignment.Text.MyTexts;
import com.example.infs3605_group_assignment.Video.MyVideos;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageButton mOpinion;
    private ImageButton mPhoto;
    private ImageButton mVideo;
    private Button mUpload;
    private ImageButton mNews;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        // Assign variable
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(), NewPostActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.reward:
                        startActivity(new Intent(getApplicationContext(), RewardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        mOpinion = findViewById(R.id.btn_opinion);
        mPhoto = findViewById(R.id.btn_photos);
        mVideo = findViewById(R.id.btn_videos);
//        mUpload = findViewById(R.id.btn_add_post);
        mNews = findViewById(R.id.btn_news);

        mOpinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyTexts.class);
                startActivity(intent);
            }
        });

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyImages.class);
                startActivity(intent);
            }
        });

        mVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyVideos.class);
                startActivity(intent);
            }
        });

//        mUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
//                startActivity(intent);
//            }
//        });

        mNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
    }
}