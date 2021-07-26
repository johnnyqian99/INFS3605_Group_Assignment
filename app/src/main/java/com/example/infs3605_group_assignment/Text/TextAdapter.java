package com.example.infs3605_group_assignment.Text;

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

import com.example.infs3605_group_assignment.Image.ImageAdapter;
import com.example.infs3605_group_assignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TextAdapter extends RecyclerView.ViewHolder {

    private TextAdapter.ClickListener mClickListener;
    TextView mTitle;
    TextView mLocation;
    TextView mNotes;
    TextView mDate;
    ImageButton likeButton;
    TextView likesDisplay;
    int likesCount;
    DatabaseReference likesRef;

    public TextAdapter(@NonNull View itemView) {
        super(itemView);

        mTitle = itemView.findViewById(R.id.tv_title2);
        mLocation = itemView.findViewById(R.id.tv_location2);
        mNotes = itemView.findViewById(R.id.tv_notes2);
        mDate = itemView.findViewById(R.id.tv_date);

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

    // Below two methods are for on item click

    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(TextAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    // For like button
    public void setLikesButtonStatus(final String postKey) {
        likeButton = itemView.findViewById(R.id.like_btn);
        likesDisplay = itemView.findViewById(R.id.likes_textView);
//        commentButton = itemView.findViewById(R.id.comment_activity_open);
        likesRef = FirebaseDatabase.getInstance().getReference("Likes/Text");
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

//    private Context mContext;
//    private List<TextUpload> mUploads;
//    private OnItemClickListener mListener;
//
//    // Constructor
//    public TextAdapter(Context context, List<TextUpload> uploads) {
//        mContext = context;
//        mUploads = uploads;
//    }
//
//    // Below 3 methods for adapter
//
//    @Override
//    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(mContext).inflate(R.layout.text_item, parent, false);
//        return new TextViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
//
//        TextUpload uploadCurrent = mUploads.get(position);
//        holder.mTitle.setText(uploadCurrent.getmTitle());
//        holder.mLocation.setText(uploadCurrent.getmLocation());
//        holder.mNotes.setText(uploadCurrent.getmNotes());
//        holder.mDate.setText(uploadCurrent.getmDate());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mUploads.size();
//    }
//
//    // Display content into Cardview
//    public class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
//            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
//
//        public TextView mTitle;
//        public TextView mLocation;
//        public TextView mNotes;
//        public TextView mDate;
//        public ImageView mImageView;
//        CardView textLayout; // dont need this?
//
//        public TextViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            mTitle = itemView.findViewById(R.id.tv_title2);
//            mLocation = itemView.findViewById(R.id.tv_location2);
//            mNotes = itemView.findViewById(R.id.tv_notes2);
//            mDate = itemView.findViewById(R.id.tv_date);
//            mImageView = itemView.findViewById(R.id.iv_image);
//            textLayout = itemView.findViewById(R.id.text_layout); // dont need this?
//
//            itemView.setOnClickListener(this);
//            itemView.setOnCreateContextMenuListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (mListener != null) {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    mListener.onItemClick(position);
//                }
//            }
//        }
//
//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.setHeaderTitle("Select Action");
//            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");
//
//            delete.setOnMenuItemClickListener(this);
//        }
//
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
//
//    // Set activity as the listener for the interface above
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = listener;
//    }

}
