package com.example.infs3605_group_assignment.Image;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605_group_assignment.R;
import com.example.infs3605_group_assignment.Video.VideoAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        favouriteRef = database.getReference("Uploads/FavouritesImage");
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

//    private Context mContext;
//    private List<ImageUpload> mUploads;
//    private OnItemClickListener mListener;

//    // Constructor
//    public ImageAdapter(Context context, List<ImageUpload> uploads) {
//        mContext = context;
//        mUploads = uploads;
//    }
//
//    // Below 3 methods for adapter
//
//    @Override
//    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
//        return new ImageViewHolder(v);
//    }

//    @Override
//    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
//
//        ImageUpload uploadCurrent = mUploads.get(position);
//        holder.mTitle.setText(uploadCurrent.getmTitle());
//        holder.mLocation.setText(uploadCurrent.getmLocation());
//        holder.mNotes.setText(uploadCurrent.getmNotes());
//        holder.mDate.setText(uploadCurrent.getmDate());
//        Picasso.get()
//                .load(uploadCurrent.getmImageUrl())
//                .placeholder(R.drawable.ic_image)
//                .fit()
//                .centerCrop()
//                .into(holder.mImageView);
//    }

//    @Override
//    public int getItemCount() {
//        return mUploads.size();
//    }
//
//    // Display content into Cardview
//    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
//            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

//        public TextView mTitle;
//        public TextView mLocation;
//        public TextView mNotes;
//        public TextView mDate;
//        public ImageView mImageView;
//        CardView imageLayout;

//        public ImageViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            mTitle = itemView.findViewById(R.id.tv_title2);
//            mLocation = itemView.findViewById(R.id.tv_location2);
//            mNotes = itemView.findViewById(R.id.tv_notes2);
//            mDate = itemView.findViewById(R.id.tv_date);
//            mImageView = itemView.findViewById(R.id.iv_image);
//            imageLayout = itemView.findViewById(R.id.image_layout);
//
//            itemView.setOnClickListener(this);
//            itemView.setOnCreateContextMenuListener(this);
//        }

//        @Override
//        public void onClick(View v) {
//            if (mListener != null) {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    mListener.onItemClick(position);
//                }
//            }
//        }

//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.setHeaderTitle("Select Action");
//            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");
//
//            delete.setOnMenuItemClickListener(this);
//        }

//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            if (mListener != null) {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    if (item.getItemId() == 1) {
//                        mListener.onDeleteClick(position);
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//    }

//    public interface OnItemClickListener {
//        // Default click
//        void onItemClick(int position);
//
//        // Delete item from firebase
//        void onDeleteClick(int position);
//    }

    // Set activity as the listener for the interface above
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = listener;
//    }

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
        likesRef = FirebaseDatabase.getInstance().getReference("Uploads/LikesImage");
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