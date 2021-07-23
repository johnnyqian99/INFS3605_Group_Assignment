package com.example.infs3605_group_assignment.Video;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.infs3605_group_assignment.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoDetailActivity extends AppCompatActivity {

    // Declare variables
    private SimpleExoPlayer player;
    private PlayerView playerView;
    TextView mTitle, mLocation, mNotes, mDate;
    private String mUrl;
    private Boolean playWhenReady = false;
    boolean fullscreen = false;
    ImageView fullScreenBtn;
    private int currentWindow = 0;
    private long playBackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        // Not using this
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Video Detail");

        // don't know why this is useful
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        // Assign variables
        playerView = findViewById(R.id.exoplayer_detail);
        mTitle = findViewById(R.id.video_title);
        mLocation = findViewById(R.id.video_location);
        mNotes = findViewById(R.id.video_notes);
        mDate = findViewById(R.id.video_date);
        fullScreenBtn = playerView.findViewById(R.id.exo_fullscreen_icon);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("video_title");
        String location = intent.getExtras().getString("video_location");
        String notes = intent.getExtras().getString("video_notes");
        String date = intent.getExtras().getString("video_date");
        mUrl = intent.getExtras().getString("video_url");

        mTitle.setText(title);
        mLocation.setText(location);
        mNotes.setText(notes);
        mDate.setText(date);

        fullScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // already in fullscreen layout
                if (fullscreen) {
                    fullScreenBtn.setImageDrawable(ContextCompat.getDrawable(VideoDetailActivity.this, R.drawable.ic_fullscreen_expand));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                } else {
                    fullScreenBtn.setImageDrawable(ContextCompat.getDrawable(VideoDetailActivity.this, R.drawable.ic_fullscreen_shrink));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                // Do I need to change the String here?
                new DefaultHttpDataSourceFactory("video");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this);
        playerView.setPlayer(player);
        Uri uri = Uri.parse(mUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playBackPosition);
        player.prepare(mediaSource, false, false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // check api level
        if (Util.SDK_INT >= Build.VERSION.SDK_INT) {
            initializePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check api level and if video is empty
        if (Util.SDK_INT >= Build.VERSION.SDK_INT || player == null) {
//            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // check api level
        if (Util.SDK_INT > Build.VERSION.SDK_INT) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // check api level
        if (Util.SDK_INT >= Build.VERSION.SDK_INT) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playBackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player = null;
        }
    }

    // stop the video when pressing the back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        player.stop();
        releasePlayer();

        final Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}