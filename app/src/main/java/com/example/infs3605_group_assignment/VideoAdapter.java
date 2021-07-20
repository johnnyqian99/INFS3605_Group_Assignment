package com.example.infs3605_group_assignment;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VideoAdapter extends RecyclerView.ViewHolder {

    SimpleExoPlayer mExoplayer;
    PlayerView mPlayerView;
    ImageButton likeButton;
    TextView likesDisplay;
    int likesCount;
    DatabaseReference databaseReference;

    public VideoAdapter(@NonNull View itemView) {
        super(itemView);
    }

    public void setExoplayer(Application application, String title, String location, String notes, String date, String mVideoUrl) {

        TextView mTitle = itemView.findViewById(R.id.tv_title2);
        TextView mLocation = itemView.findViewById(R.id.tv_location2);
        TextView mNotes = itemView.findViewById(R.id.tv_notes2);
        TextView mDate = itemView.findViewById(R.id.tv_date);
        mPlayerView = itemView.findViewById(R.id.exoplayer_video);

        mTitle.setText(title);
        mLocation.setText(location);
        mNotes.setText(notes);
        mDate.setText(date);

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            mExoplayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application);
            Uri video = Uri.parse(mVideoUrl);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("Uploads/Video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
            mPlayerView.setPlayer(mExoplayer);
            mExoplayer.prepare(mediaSource);
            mExoplayer.setPlayWhenReady(false);
        } catch (Exception e) {
            Log.e("VideoAdapter", "exoplayer error" + e.toString());
        }
    }

    public void setLikesButtonStatus(final String postKey) {
        likeButton = itemView.findViewById(R.id.like_btn);
        likesDisplay = itemView.findViewById(R.id.likes_textView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads/Likes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String likes = "likes";

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postKey).hasChild(userId)) {
                    likesCount = (int) snapshot.child(postKey).getChildrenCount();
                    likeButton.setImageResource(R.drawable.ic_like);
                    likesDisplay.setText(Integer.toString(likesCount) + likes);
                } else {
                    likesCount = (int) snapshot.child(postKey).getChildrenCount();
                    likeButton.setImageResource(R.drawable.ic_dislike);
                    likesDisplay.setText(Integer.toString(likesCount) + likes);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Can show a toast message
            }
        });
    }

}
