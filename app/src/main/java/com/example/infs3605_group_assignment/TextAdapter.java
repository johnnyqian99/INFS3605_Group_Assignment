package com.example.infs3605_group_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> {

    private Context mContext;
    private List<TextUpload> mUploads;

    public TextAdapter(Context context, List<TextUpload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.text_item, parent, false);
        return new TextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {

        TextUpload uploadCurrent = mUploads.get(position);
        holder.mTitle.setText(uploadCurrent.getmTitle());
        holder.mLocation.setText(uploadCurrent.getmLocation());
        holder.mNotes.setText(uploadCurrent.getmNotes());
        holder.mDate.setText(uploadCurrent.getmDate());
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mLocation;
        public TextView mNotes;
        public TextView mDate;
        public ImageView mImageView;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_title2);
            mLocation = itemView.findViewById(R.id.tv_location2);
            mNotes = itemView.findViewById(R.id.tv_notes2);
            mDate = itemView.findViewById(R.id.tv_date);
            mImageView = itemView.findViewById(R.id.iv_image);
        }
    }

}
