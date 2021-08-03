package com.example.infs3605_group_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsDemoAdapter extends RecyclerView.Adapter<NewsDemoAdapter.MyViewHolder> {

    String titleArray[], descArray[];
    Context mContext;

    // constructor
    public NewsDemoAdapter (Context context, String title[], String desc[]) {
        mContext = context;
        titleArray = title;
        descArray = desc;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.newsdemo_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDemoAdapter.MyViewHolder holder, int position) {

        holder.title.setText(titleArray[position]);
        holder.desc.setText(descArray[position]);
    }

    @Override
    public int getItemCount() {
        return titleArray.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            desc = itemView.findViewById(R.id.tv_desc);
        }
    }
}
