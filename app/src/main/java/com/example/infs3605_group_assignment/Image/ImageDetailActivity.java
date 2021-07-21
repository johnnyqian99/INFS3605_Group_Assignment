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

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("image_url") && getIntent().hasExtra("image_name")) {
            String imageUrl = getIntent().getStringExtra("image_url");
            String imageName = getIntent().getStringExtra("image_name");

            setImage(imageUrl, imageName);
        }
    }

    private void setImage(String imageUrl, String imageName) {

        TextView name = findViewById(R.id.image_title);
        name.setText(imageName);

        ImageView imageView = findViewById(R.id.image);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_image)
                .fit()
                .centerCrop()
                .into(imageView);
    }
}