package com.example.infs3605_group_assignment.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.infs3605_group_assignment.Image.MyImages;
import com.example.infs3605_group_assignment.MainActivity;
import com.example.infs3605_group_assignment.NewPostActivity;
import com.example.infs3605_group_assignment.Video.MyVideos;
import com.example.infs3605_group_assignment.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyTexts extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextAdapter.OnItemClickListener {

    // Declare variables
    private ImageButton backBtn;
    private FloatingActionButton floatingActionButton;
    private Spinner mSpinner;
    private RecyclerView mRecyclerView;
    private TextAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<TextUpload> mUploads;
    private ProgressBar progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_texts);

        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Text");

        // Remove action bar
        getSupportActionBar().hide();

        // Assign variables
        backBtn = findViewById(R.id.back_btn);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        progressCircle = findViewById(R.id.progress_circle);
        mSpinner = findViewById(R.id.spinner);
        mRecyclerView = findViewById(R.id.rv_text);

        // Navigate to MainActivity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTexts.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.textActivityTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        // Recyclerview
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();

        // Navigate to NewPostActivity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTexts.this, NewPostActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Load data into Recyclerview
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    TextUpload textUpload = postSnapshot.getValue(TextUpload.class);
                    mUploads.add(textUpload);
                }

                mAdapter = new TextAdapter(MyTexts.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(MyTexts.this);
                progressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyTexts.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    // Below two methods are for item selected on spinner

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("Image")) {
            Intent intent = new Intent(MyTexts.this, MyImages.class);
            startActivity(intent);
        }
        if (text.equals("Video")) {
            Intent intent = new Intent(MyTexts.this, MyVideos.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(int position) {
//        Toast.makeText(this, "Detail click at position: " + position, Toast.LENGTH_SHORT).show(); // for testing
        // Send extras for detail
        Intent intent = new Intent(MyTexts.this, TextDetailActivity.class);
        intent.putExtra("text_title", mUploads.get(position).getmTitle());
        intent.putExtra("text_location", mUploads.get(position).getmLocation());
        intent.putExtra("text_notes", mUploads.get(position).getmNotes());
        intent.putExtra("text_date", mUploads.get(position).getmDate());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
//        Toast.makeText(this, "Delete click at position: " + position, Toast.LENGTH_SHORT).show(); // for testing
    }
}