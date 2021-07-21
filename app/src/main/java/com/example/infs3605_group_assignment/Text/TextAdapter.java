package com.example.infs3605_group_assignment.Text;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605_group_assignment.Image.ImageDetailActivity;
import com.example.infs3605_group_assignment.R;

import java.util.ArrayList;
import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> implements Filterable {

    private Context mContext;
    private List<TextUpload> mUploads;
    private List<TextUpload> mUploadsFull;

    public TextAdapter(Context context, List<TextUpload> uploads) {
        mContext = context;
        mUploads = uploads;
        mUploadsFull = new ArrayList<>(uploads);
    }

    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.text_item, parent, false);
        return new TextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {

        TextUpload uploadCurrent = mUploads.get(position);
        holder.mTitle.setText(uploadCurrent.getmTitle());
        holder.mLocation.setText(uploadCurrent.getmLocation());
        holder.mNotes.setText(uploadCurrent.getmNotes());
        holder.mDate.setText(uploadCurrent.getmDate());

        holder.textLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Click position: " + mUploads.get(position).getmTitle(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, TextDetailActivity.class);
                intent.putExtra("text_name", mUploads.get(position).getmTitle());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mLocation;
        public TextView mNotes;
        public TextView mDate;
        public ImageView mImageView;
        CardView textLayout;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_title2);
            mLocation = itemView.findViewById(R.id.tv_location2);
            mNotes = itemView.findViewById(R.id.tv_notes2);
            mDate = itemView.findViewById(R.id.tv_date);
            mImageView = itemView.findViewById(R.id.iv_image);
            textLayout = itemView.findViewById(R.id.text_layout);
        }
    }

    @Override
    public Filter getFilter() {
        return uploadsFilter;
    }

    private Filter uploadsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TextUpload> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mUploadsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (TextUpload item : mUploadsFull) {
                    if (item.getmTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mUploads.clear();
            mUploads.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
