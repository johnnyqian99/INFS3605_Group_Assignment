package com.example.infs3605_group_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FourthInfoActivity extends AppCompatActivity {
    private Button nextBtn;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_info);

        getSupportActionBar().hide();

        nextBtn = findViewById(R.id.fourth_next);
        backBtn = findViewById(R.id.fourth_back);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourthInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourthInfoActivity.this, ThirdInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}