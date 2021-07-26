package com.example.infs3605_group_assignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TextFavouritesAdapter extends RecyclerView.ViewHolder {

    TextView mTitle, mLocation, mNotes, mDate;

    public TextFavouritesAdapter(@NonNull View itemView) {
        super(itemView);

        mTitle = itemView.findViewById(R.id.tv_title);
        mLocation = itemView.findViewById(R.id.tv_location);
        mNotes = itemView.findViewById(R.id.tv_notes);
        mDate = itemView.findViewById(R.id.tv_date);
    }
}
