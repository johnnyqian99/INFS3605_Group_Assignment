package com.example.infs3605_group_assignment.Image;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605_group_assignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ImageAdapter extends RecyclerView.ViewHolder {

    private ImageAdapter.ClickListener mClickListener;
    ImageView imageView;
    TextView title;
    TextView location;
    TextView notes;
    TextView date;
    ImageButton likeButton, commentButton, favouriteButton;
    TextView likesDisplay;
    int likesCount;
    DatabaseReference likesRef, favouriteRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    public ImageAdapter(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.iv_image);
        title = itemView.findViewById(R.id.tv_title2);
        location = itemView.findViewById(R.id.tv_location2);
        notes = itemView.findViewById(R.id.tv_notes2);
        date = itemView.findViewById(R.id.tv_date);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mClickListener.onItemLongClick(v, getAdapterPosition());
                return false;
            }
        });
    }

    public void favouriteChecker(String postKey) {

        favouriteButton = itemView.findViewById(R.id.favourite_item);
        favouriteRef = database.getReference("Favourites/Image");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        favouriteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postKey).hasChild(uid)) {
                    favouriteButton.setImageResource(R.drawable.ic_turned_in);
                } else {
                    favouriteButton.setImageResource(R.drawable.ic_turned_in_not);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Below two methods are for on item click

    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ImageAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    // For like button
    public void setLikesButtonStatus(final String postKey) {
        likeButton = itemView.findViewById(R.id.like_btn);
        likesDisplay = itemView.findViewById(R.id.likes_textView);
        commentButton = itemView.findViewById(R.id.comment_activity_open);
        likesRef = FirebaseDatabase.getInstance().getReference("Likes/Image");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String likes = "likes";

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postKey).hasChild(userId)) {
                    likesCount = (int) snapshot.child(postKey).getChildrenCount();
                    likeButton.setImageResource(R.drawable.ic_like);
                    likesDisplay.setText(Integer.toString(likesCount) + " " + likes);
                } else {
                    likesCount = (int) snapshot.child(postKey).getChildrenCount();
                    likeButton.setImageResource(R.drawable.ic_dislike);
                    likesDisplay.setText(Integer.toString(likesCount) + " " + likes);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Can show a Toast message
            }
        });
    }

}