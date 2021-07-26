package com.example.infs3605_group_assignment.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyTexts extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Declare variables
    private ImageButton backBtn;
    private FloatingActionButton floatingActionButton;
    private Spinner mSpinner;
    private RecyclerView mRecyclerView;
    private TextAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<TextUpload> mUploads;
    private ProgressBar progressCircle;
    FirebaseRecyclerOptions<TextUpload> options;
    FirebaseRecyclerAdapter<TextUpload, TextAdapter> adapter;
    DatabaseReference dataRef, likesRef;
    Boolean likeChecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_texts);

//        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Text");
        dataRef = FirebaseDatabase.getInstance().getReference("Uploads/Text");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes/Text");

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
//        mUploads = new ArrayList<>();

        // Navigate to NewPostActivity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTexts.this, NewPostActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LoadData();

        // Load data into Recyclerview
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    TextUpload textUpload = postSnapshot.getValue(TextUpload.class);
//                    mUploads.add(textUpload);
//                }
//
//                mAdapter = new TextAdapter(MyTexts.this, mUploads);
//                mRecyclerView.setAdapter(mAdapter);
//                mAdapter.setOnItemClickListener(MyTexts.this);
//                progressCircle.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MyTexts.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressCircle.setVisibility(View.INVISIBLE);
//            }
//        });

    }

    private void LoadData() {

        options = new FirebaseRecyclerOptions.Builder<TextUpload>().setQuery(dataRef, TextUpload.class).build();
        adapter = new FirebaseRecyclerAdapter<TextUpload, TextAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TextAdapter holder, int position, @NonNull TextUpload model) {

                // This will get the userID for like function
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserId = user.getUid();
                final String postKey = getRef(position).getKey();

                holder.mTitle.setText(model.getmTitle());
                holder.mLocation.setText(model.getmLocation());
                holder.mNotes.setText(model.getmNotes());
                holder.mDate.setText(model.getmDate());

                // For like/comment feature
                holder.setLikesButtonStatus(postKey);

                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        likeChecker = true;

                        likesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                // Check if user already liked video or not
                                if (likeChecker.equals(true)) {
                                    if (snapshot.child(postKey).hasChild(currentUserId)) {
                                        likesRef.child(postKey).child(currentUserId).removeValue();
                                        likeChecker = false;
                                    } else {
                                        likesRef.child(postKey).child(currentUserId).setValue(true);
                                        likeChecker = false;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Can show a Toast message
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public TextAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item, parent, false);
                return new TextAdapter(v);
            }
        };

        adapter.startListening();
        mRecyclerView.setAdapter(adapter);
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

//    @Override
//    public void onItemClick(int position) {
////        Toast.makeText(this, "Detail click at position: " + position, Toast.LENGTH_SHORT).show(); // for testing
//        // Send extras for detail
//        Intent intent = new Intent(MyTexts.this, TextDetailActivity.class);
//        intent.putExtra("text_title", mUploads.get(position).getmTitle());
//        intent.putExtra("text_location", mUploads.get(position).getmLocation());
//        intent.putExtra("text_notes", mUploads.get(position).getmNotes());
//        intent.putExtra("text_date", mUploads.get(position).getmDate());
//        startActivity(intent);
//    }

//    @Override
//    public void onDeleteClick(int position) {
////        Toast.makeText(this, "Delete click at position: " + position, Toast.LENGTH_SHORT).show(); // for testing
//    }
}