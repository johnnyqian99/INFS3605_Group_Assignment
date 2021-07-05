package com.example.infs3605_group_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewPostActivity extends AppCompatActivity {

    // Variables
    private Button mBtnText;
    private Button mBtnImage;
    private Button mBtnVideo;
    private Button mBtnNews;
    private Button mBtnOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // Direct TEXT button
        mBtnText = findViewById(R.id.postTextBtn);
        mBtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostActivity.this, TextPostActivity.class);
                startActivity(intent);
            }
        });

        // Direct IMAGE button
        mBtnImage = findViewById(R.id.postImageBtn);
        mBtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostActivity.this, ImagePostActivity.class);
                startActivity(intent);
            }
        });

        // Direct VIDEO button
        mBtnVideo = findViewById(R.id.postVideoBtn);
        mBtnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostActivity.this, VideoPostActivity.class);
                startActivity(intent);
            }
        });

        // Direct OTHER POSTS button
        mBtnOther = findViewById(R.id.otherPostsBtn);
        mBtnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostActivity.this, OtherPostsActivity.class);
                startActivity(intent);
            }
        });

        // Direct NEWS FEED button
        mBtnNews = findViewById(R.id.newsFeedBtn);
        mBtnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostActivity.this, NewsFeedActivity.class);
                startActivity(intent);
            }
        });
    }
}