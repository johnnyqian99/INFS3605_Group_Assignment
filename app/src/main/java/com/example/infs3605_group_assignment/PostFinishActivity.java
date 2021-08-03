package com.example.infs3605_group_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.infs3605_group_assignment.Image.MyImages;
import com.example.infs3605_group_assignment.Text.MyTexts;
import com.example.infs3605_group_assignment.Video.MyVideos;

public class PostFinishActivity extends AppCompatActivity {

    // Declare variables
    private TextView mPoints;
    private Button mFinish;
    private String points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_finish);

        // Remove action bar
        getSupportActionBar().hide();

        // Assign variables
        mPoints = findViewById(R.id.tv_points);
        mFinish = findViewById(R.id.finish_btn);

        points = getIntent().getExtras().getString("points");
        mPoints.setText("+" + points + " stars");

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (points.equals("5")) {
                    Intent intent = new Intent(PostFinishActivity.this, MyTexts.class);
                    startActivity(intent);
                } else if (points.equals("10")) {
                    Intent intent = new Intent(PostFinishActivity.this, MyImages.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PostFinishActivity.this, MyVideos.class);
                    startActivity(intent);
                }
            }
        });
    }

}