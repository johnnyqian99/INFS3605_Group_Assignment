package com.example.infs3605_group_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstInfoActivity extends AppCompatActivity {
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_info);

        getSupportActionBar().hide();

        nextBtn = findViewById(R.id.first_next);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstInfoActivity.this, SecondInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}