package com.example.infs3605_group_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondInfoActivity extends AppCompatActivity {
    private Button nextBtn;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_info);

        getSupportActionBar().hide();

        nextBtn = findViewById(R.id.second_next);
        backBtn = findViewById(R.id.second_back);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondInfoActivity.this, ThirdInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondInfoActivity.this, FirstInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}