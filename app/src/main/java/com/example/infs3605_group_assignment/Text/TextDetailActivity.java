package com.example.infs3605_group_assignment.Text;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infs3605_group_assignment.R;
import com.squareup.picasso.Picasso;

public class TextDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_detail);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("text_name")) {
            String textName = getIntent().getStringExtra("text_name");

            setText(textName);
        }
    }

    private void setText(String textName) {

        TextView name = findViewById(R.id.text_title);
        name.setText(textName);

    }
}