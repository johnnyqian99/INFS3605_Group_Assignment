package com.example.infs3605_group_assignment.Comment;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605_group_assignment.R;

public class CommentsAdapter extends RecyclerView.ViewHolder {

    TextView textView1, textView2;

    public CommentsAdapter(@NonNull View itemView) {
        super(itemView);
    }

    public void Comments(Application application, String comment, String date, String time, String username) {

        textView1 = itemView.findViewById(R.id.comment_time_date_item);
        textView2 = itemView.findViewById(R.id.comment_username_item);

        textView1.setText(date + "-" + time);
        textView2.setText(username + ":" + comment);

    }

}
