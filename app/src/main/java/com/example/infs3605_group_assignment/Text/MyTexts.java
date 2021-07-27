package com.example.infs3605_group_assignment.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.infs3605_group_assignment.Comment.CommentsActivityText;
import com.example.infs3605_group_assignment.Image.MyImages;
import com.example.infs3605_group_assignment.MainActivity;
import com.example.infs3605_group_assignment.TextFavourites;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyTexts extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Declare variables
    private Button favouritesListBtn;
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
    DatabaseReference dataRef, likesRef, favouriteRef, favouriteListRef;
    Boolean likeChecker = false, favouriteChecker = false;
    String mTitle, mLocation, mNotes, mDate;
    long value;
    long textCounter = 0;

    TextUpload textUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_texts);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

//        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Text");
        dataRef = FirebaseDatabase.getInstance().getReference("Uploads/Text");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes/Text");
        favouriteRef = FirebaseDatabase.getInstance().getReference("Favourites/Text"); // checking if image is saved
        favouriteListRef = FirebaseDatabase.getInstance().getReference("Favourites/TextList").child(currentUserId); // reference for saving images in new child
        textUpload = new TextUpload();

        // Remove action bar
        getSupportActionBar().hide();

        // Assign variables
        favouritesListBtn = findViewById(R.id.text_favourites_list_button);
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

        favouritesListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTexts.this, TextFavourites.class);
                startActivity(intent);
            }
        });

        // Navigate to NewPostActivity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTexts.this, TextPostActivity.class);
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

                // for favourites
                String title = getItem(position).getmTitle();
                String location = getItem(position).getmLocation();
                String notes = getItem(position).getmNotes();
                String date = getItem(position).getmDate();

                holder.favouriteChecker(postKey);
                holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        favouriteChecker = true;

                        favouriteRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (favouriteChecker.equals(true)) {
                                    if (snapshot.child(postKey).hasChild(currentUserId)) {
                                        favouriteRef.child(postKey).child(currentUserId).removeValue();
                                        delete(title);
                                        favouriteChecker = false;
                                    } else {
                                        favouriteRef.child(postKey).child(currentUserId).setValue(true);
                                        textUpload.setmTitle(title);
                                        textUpload.setmLocation(location);
                                        textUpload.setmNotes(notes);
                                        textUpload.setmDate(date);

                                        String id = favouriteListRef.push().getKey();
                                        favouriteListRef.child(id).setValue(textUpload);
                                        favouriteChecker = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                holder.setOnClickListener(new TextAdapter.ClickListener() {
                    // this does nothing
                    @Override
                    public void onItemClick(View view, int position) {

//                        mTitle = getItem(position).getmTitle();
//                        mLocation = getItem(position).getmLocation();
//                        mNotes = getItem(position).getmNotes();
//                        mDate = getItem(position).getmDate();
//                        Intent intent = new Intent(MyTexts.this, TextDetailActivity.class);
//                        intent.putExtra("text_title", mTitle);
//                        intent.putExtra("text_location", mLocation);
//                        intent.putExtra("text_notes", mNotes);
//                        intent.putExtra("text_date", mDate);
//                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        mTitle = getItem(position).getmTitle();
                        showDeleteDialog(mTitle);
                    }
                });

                // For like/comment feature
                holder.setLikesButtonStatus(postKey);

                holder.commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MyTexts.this, CommentsActivityText.class);
                        intent.putExtra("postkey2", postKey); // reference to a particular video
                        startActivity(intent);
                    }
                });

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
                progressCircle.setVisibility(View.INVISIBLE);
                return new TextAdapter(v);
            }
        };

        adapter.startListening();
        mRecyclerView.setAdapter(adapter);
    }

    void delete(String title) {

        Query query = favouriteListRef.orderByChild("mTitle").equalTo(title);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(MyTexts.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void showDeleteDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyTexts.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Query query = dataRef.orderByChild("mTitle").equalTo(title);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                            dataSnapshot1.getRef().removeValue();
                        }
                        Toast.makeText(MyTexts.this, "Text Deleted", Toast.LENGTH_SHORT).show();

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myStarRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("stars");
                        DatabaseReference mTextCount = database.getReference(FirebaseAuth.getInstance().getUid()).child("textCount");

                        myStarRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                value = (long) snapshot.getValue();
                                // number of posts cannot be negative
                                if (value >= 20 ) {
                                    value = value - 20;
                                    snapshot.getRef().setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        mTextCount.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                textCounter = (long) snapshot.getValue();
                                // number of posts cannot be negative
                                if (textCounter > 0) {
                                    textCounter = textCounter - 1;
                                    snapshot.getRef().setValue(textCounter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // DO WHATEVER YOU WANT
                    }
                });

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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