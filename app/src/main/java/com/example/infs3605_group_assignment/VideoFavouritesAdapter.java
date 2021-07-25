package com.example.infs3605_group_assignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.PlayerView;

public class VideoFavouritesAdapter extends RecyclerView.ViewHolder {

    PlayerView playerView;
    TextView mTitle, mLocation, mNotes, mDate;

    public VideoFavouritesAdapter(@NonNull View itemView) {
        super(itemView);

        playerView = itemView.findViewById(R.id.exoplayer_video);
        mTitle = itemView.findViewById(R.id.tv_title);
        mLocation = itemView.findViewById(R.id.tv_location);
        mNotes = itemView.findViewById(R.id.tv_notes);
        mDate = itemView.findViewById(R.id.tv_date);
    }
}
