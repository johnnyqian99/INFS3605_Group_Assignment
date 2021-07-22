package com.example.infs3605_group_assignment.Image;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605_group_assignment.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<ImageUpload> mUploads;
    private OnItemClickListener mListener;

    // Constructor
    public ImageAdapter(Context context, List<ImageUpload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    // Below 3 methods for adapter

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        ImageUpload uploadCurrent = mUploads.get(position);
        holder.mTitle.setText(uploadCurrent.getmTitle());
        holder.mLocation.setText(uploadCurrent.getmLocation());
        holder.mNotes.setText(uploadCurrent.getmNotes());
        holder.mDate.setText(uploadCurrent.getmDate());
        Picasso.get()
                .load(uploadCurrent.getmImageUrl())
                .placeholder(R.drawable.ic_image)
                .fit()
                .centerCrop()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    // Display content into Cardview
    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView mTitle;
        public TextView mLocation;
        public TextView mNotes;
        public TextView mDate;
        public ImageView mImageView;
        CardView imageLayout;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_title2);
            mLocation = itemView.findViewById(R.id.tv_location2);
            mNotes = itemView.findViewById(R.id.tv_notes2);
            mDate = itemView.findViewById(R.id.tv_date);
            mImageView = itemView.findViewById(R.id.iv_image);
            imageLayout = itemView.findViewById(R.id.image_layout);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem goDetail = menu.add(Menu.NONE, 1, 1, "View Details");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            goDetail.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDetailClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        // Default click
        void onItemClick(int position);

        // Navigate to detail screen
        void onDetailClick(int position);

        // Delete item from firebase
        void onDeleteClick(int position);
    }

    // Set activity as the listener for the interface above
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}