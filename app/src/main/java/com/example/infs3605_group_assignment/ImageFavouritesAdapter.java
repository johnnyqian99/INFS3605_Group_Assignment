package com.example.infs3605_group_assignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageFavouritesAdapter extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView mTitle, mLocation, mNotes, mDate;

    public ImageFavouritesAdapter(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.iv_image);
        mTitle = itemView.findViewById(R.id.tv_title);
        mLocation = itemView.findViewById(R.id.tv_location);
        mNotes = itemView.findViewById(R.id.tv_notes);
        mDate = itemView.findViewById(R.id.tv_date);
    }
}
