package com.example.infs3605_group_assignment.Comment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.infs3605_group_assignment.Image.MyImages;
import com.example.infs3605_group_assignment.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentsActivityImages extends AppCompatActivity {

    // Declare variables
    private RecyclerView recyclerView_comments;
    private Button post_comments;
    private ImageButton mBackBtn;
    private EditText editText_comment_input;
    DatabaseReference databaseReference, postref;
    private String post_key;
    Comments comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_images);

        // Remove action bar
        getSupportActionBar().hide();

        post_key = getIntent().getExtras().getString("postkey2"); // retrieve the image reference
        comments = new Comments();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getDisplayName();

        // WE NEED TO ASSIGN DATABASE REFERENCE HERE OR ELSE ERROR
        databaseReference = FirebaseDatabase.getInstance().getReference(); // --> change this

        // This is where comments will be saved
        // This will be saved under child ("Uploads/Video") > postkey (video id) > then "Comments"
        postref = FirebaseDatabase.getInstance().getReference().child("Uploads/Image").child(post_key).child("Comments");

        recyclerView_comments = findViewById(R.id.recyclerView_comments);
        recyclerView_comments.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView_comments.setLayoutManager(linearLayoutManager);

        mBackBtn = findViewById(R.id.back_btn11);
        post_comments = findViewById(R.id.comment_btn_post);
        editText_comment_input = findViewById(R.id.comment_et);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentsActivityImages.this, MyImages.class);
                startActivity(intent);
                finish();
            }
        });

        post_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // Check if username exists --> with this if statement, it doesnt post of some reason
//                        if (snapshot.exists()) {
//                            String username = snapshot.child("name").getValue().toString();

                        String username = userId; // change this
                        commentFeature(username);

                        editText_comment_input.setText("");
//                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            private void commentFeature(String username) {

                String comment_post = editText_comment_input.getText().toString();
                if (comment_post.isEmpty()) {
                    Toast.makeText(CommentsActivityImages.this, "Please write a comment", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar callForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                    final String saveCurrentDate = currentDate.format(callForDate.getTime());

                    Calendar callForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                    final String saveCurrentTime = currentTime.format(callForTime.getTime());

                    final String randomKey = userId + saveCurrentDate + saveCurrentTime;

                    // Create Hashmap for saving comment
                    HashMap commentMap = new HashMap();
                    commentMap.put("uid", userId);
                    commentMap.put("comment", " " + comment_post);
                    commentMap.put("date", saveCurrentDate);
                    commentMap.put("time", saveCurrentTime);
                    commentMap.put("username", username);

                    postref.child(randomKey).updateChildren(commentMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(CommentsActivityImages.this, "Commented added!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CommentsActivityImages.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(postref, Comments.class)
                        .build();

        FirebaseRecyclerAdapter<Comments, CommentsAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Comments, CommentsAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CommentsAdapter holder, int position, @NonNull Comments model) {

                        holder.Comments(getApplication(), model.getComment(), model.getDate(), model.getTime(), model.getUsername());
                    }

                    @NonNull
                    @Override
                    public CommentsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.comment_item2, parent, false);

                        return new CommentsAdapter(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView_comments.setAdapter(firebaseRecyclerAdapter);
    }
}