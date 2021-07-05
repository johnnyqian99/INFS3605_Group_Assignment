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

    }
}