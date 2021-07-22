package com.example.infs3605_group_assignment.Text;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.infs3605_group_assignment.R;

public class TextDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_detail);

        // Remove action bar
        getSupportActionBar().hide();

        getIncomingIntent();
    }

    // Retrieve post data when item clicked in Recyclerview
    private void getIncomingIntent() {
        // Ensure that the previous intent has all the "getExtra" required
        if (getIntent().hasExtra("text_title") && getIntent().hasExtra("text_location")
                && getIntent().hasExtra("text_notes") && getIntent().hasExtra("text_date")) {
            String textTitle = getIntent().getStringExtra("text_title");
            String textLocation = getIntent().getStringExtra("text_location");
            String textNotes = getIntent().getStringExtra("text_notes");
            String textDate = getIntent().getStringExtra("text_date");

            setText(textTitle, textLocation, textNotes, textDate);
        }
    }

    // Set XML variables
    private void setText(String textTitle, String textLocation, String textNotes, String textDate) {

        TextView title = findViewById(R.id.text_title);
        title.setText(textTitle);
        TextView location = findViewById(R.id.text_location);
        location.setText(textLocation);
        TextView notes = findViewById(R.id.text_notes);
        notes.setText(textNotes);
        TextView date = findViewById(R.id.text_date);
        date.setText(textDate);

    }
}