package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class  NewsDemoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String titleArray[], descArray[];
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_demo);

        // Remove action bar
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        titleArray = getResources().getStringArray(R.array.newsTitles);
        descArray = getResources().getStringArray(R.array.newsDesc);

        NewsDemoAdapter adapter = new NewsDemoAdapter(this, titleArray, descArray);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ***NAVIGATION BAR

        // Set current selected item
        bottomNavigationView.setSelectedItemId(R.id.news);
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
                        return true;
                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
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
    }
}