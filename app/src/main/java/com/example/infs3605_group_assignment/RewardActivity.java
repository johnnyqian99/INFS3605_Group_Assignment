package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.infs3605_group_assignment.News.NewsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RewardActivity extends AppCompatActivity implements RewardsAdapter.OnNoteListener {

    private TextView mStars;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private List<Rewards> rewardsList;
    private RewardsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        // Remove action bar
        getSupportActionBar().hide();

        // Assign variables
        mStars = findViewById(R.id.stars);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        rewardsList = Rewards.getCategories();

        recyclerView = findViewById(R.id.rewards);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("stars");

        // logic to see if points exist and need to be shown
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    int total = Integer.parseInt(snapshot.getValue().toString());
                    mStars.setText(String.format("%d", total));
                } else {
                    mStars.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setAdapter();

        // ***NAVIGATION BAR

        // Set current selected item
        bottomNavigationView.setSelectedItemId(R.id.reward);
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
                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(), NewPostActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.reward:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });
        // NAVIGATION BAR***
    }

    private void setAdapter() {
        adapter = new RewardsAdapter(rewardsList, this);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(int position) {
        rewardsList.get(position);
//        Intent intent = new Intent(HomePageActivity.this, ModeSelectActivity.class);
//        intent.putExtra("Category",categoryList.get(position).getCategory());
//        intent.putExtra("Description", categoryList.get(position).getDescription());
//        startActivity(intent);
    }
}