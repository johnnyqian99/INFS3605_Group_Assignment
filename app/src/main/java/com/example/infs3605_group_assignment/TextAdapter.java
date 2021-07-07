package com.example.infs3605_group_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> {

    private Context mContext;
    private List<TextUpload> mUploads;

    public TextAdapter(Context context, List<TextUpload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.text_item, parent, false);
        return new TextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {

        TextUpload uploadCurrent = mUploads.get(position);
        holder.textViewTitle.setText(uploadCurrent.getmTitle());
        holder.textViewNotes.setText(uploadCurrent.getmNotes());
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTitle;
        public TextView textViewNotes;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewNotes = itemView.findViewById(R.id.tv_notes);
        }
    }

}
