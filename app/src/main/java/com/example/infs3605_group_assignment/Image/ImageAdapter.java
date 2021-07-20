package com.example.infs3605_group_assignment.Image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605_group_assignment.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> implements Filterable {

    private Context mContext;
    private List<ImageUpload> mUploads;
    private List<ImageUpload> mUploadsFull;

    public ImageAdapter(Context context, List<ImageUpload> uploads) {
        mContext = context;
        mUploads = uploads;
        mUploadsFull = new ArrayList<>(uploads);
    }

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

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mLocation;
        public TextView mNotes;
        public TextView mDate;
        public ImageView mImageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_title2);
            mLocation = itemView.findViewById(R.id.tv_location2);
            mNotes = itemView.findViewById(R.id.tv_notes2);
            mDate = itemView.findViewById(R.id.tv_date);
            mImageView = itemView.findViewById(R.id.iv_image);
        }
    }

    @Override
    public Filter getFilter() {
        return uploadsFilter;
    }

    private Filter uploadsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ImageUpload> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mUploadsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ImageUpload item : mUploadsFull) {
                    if (item.getmTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mUploads.clear();
            mUploads.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
