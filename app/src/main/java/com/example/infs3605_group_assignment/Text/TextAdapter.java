package com.example.infs3605_group_assignment.Text;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605_group_assignment.R;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> {

    private Context mContext;
    private List<TextUpload> mUploads;

    // Constructor
    public TextAdapter(Context context, List<TextUpload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    // Below 3 methods for adapter

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

        // Click listener for items in Recyclerview
        holder.textLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send extras for detail
                Intent intent = new Intent(mContext, TextDetailActivity.class);
                intent.putExtra("text_title", mUploads.get(position).getmTitle());
                intent.putExtra("text_location", mUploads.get(position).getmLocation());
                intent.putExtra("text_notes", mUploads.get(position).getmNotes());
                intent.putExtra("text_date", mUploads.get(position).getmDate());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    // Display content into Cardview
    public class TextViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mLocation;
        public TextView mNotes;
        public TextView mDate;
        public ImageView mImageView;
        CardView textLayout;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_title2);
            mLocation = itemView.findViewById(R.id.tv_location2);
            mNotes = itemView.findViewById(R.id.tv_notes2);
            mDate = itemView.findViewById(R.id.tv_date);
            mImageView = itemView.findViewById(R.id.iv_image);
            textLayout = itemView.findViewById(R.id.text_layout);
        }
    }

}
