package com.example.infs3605_group_assignment.Image;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infs3605_group_assignment.R;
import com.squareup.picasso.Picasso;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // Remove action bar
        getSupportActionBar().hide();

        getIncomingIntent();
    }

    // Retrieve post data when item clicked in Recyclerview
    private void getIncomingIntent() {
        // Ensure that the previous intent has all the "getExtra" required
        if (getIntent().hasExtra("image_url") && getIntent().hasExtra("image_title") &&
                getIntent().hasExtra("image_location") && getIntent().hasExtra("image_notes") &&
                getIntent().hasExtra("image_date")) {

            String imageUrl = getIntent().getStringExtra("image_url");
            String imageTitle = getIntent().getStringExtra("image_title");
            String imageLocation = getIntent().getStringExtra("image_location");
            String imageNotes = getIntent().getStringExtra("image_notes");
            String imageDate = getIntent().getStringExtra("image_date");

            // Set data
            setImage(imageUrl, imageTitle, imageLocation, imageNotes, imageDate);
        }
    }

    // Set XML variables
    private void setImage(String imageUrl, String imageTitle, String imageLocation,
                          String imageNotes, String imageDate) {

        ImageView imageView = findViewById(R.id.image);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_image)
                .fit()
                .centerCrop()
                .into(imageView);

        TextView title = findViewById(R.id.image_title);
        title.setText(imageTitle);
        TextView location = findViewById(R.id.image_location);
        location.setText(imageLocation);
        TextView notes = findViewById(R.id.image_notes);
        notes.setText(imageNotes);
        TextView date = findViewById(R.id.image_date);
        date.setText(imageDate);
    }
}