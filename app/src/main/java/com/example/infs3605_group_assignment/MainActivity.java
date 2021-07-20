package com.example.infs3605_group_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.infs3605_group_assignment.Image.MyImages;
import com.example.infs3605_group_assignment.News.NewsActivity;
import com.example.infs3605_group_assignment.Text.MyTexts;
import com.example.infs3605_group_assignment.Video.MyVideos;

public class MainActivity extends AppCompatActivity {

    private ImageButton mOpinion;
    private ImageButton mPhoto;
    private ImageButton mVideo;
    private Button mUpload;
    private ImageButton mNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

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