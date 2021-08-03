package com.example.infs3605_group_assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.MyViewHolder> implements Filterable {
    private List<Rewards> categoryList;
    private List<Rewards> categoryListFull;
    long stars;

    private OnNoteListener mOnNoteListener;

    public RewardsAdapter(List<Rewards> categoryList, OnNoteListener onNoteListener) {
        this.categoryList = categoryList;
        this.mOnNoteListener = onNoteListener;
        categoryListFull = new ArrayList<>(categoryList);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button title;
        private ImageView img;

        OnNoteListener onNoteListener;

        public MyViewHolder(final View view, OnNoteListener onNoteListener) {
            super(view);
            title = view.findViewById(R.id.stars_cost);
            img = view.findViewById(R.id.charity);

            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RewardsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_grid_layout, parent, false);
        return new MyViewHolder(itemView, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsAdapter.MyViewHolder holder, int position) {
        String titleName = String.format("%d stars", categoryList.get(position).getCost());

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(true);

        holder.title.setText(titleName);
        holder.img.setImageResource(categoryList.get(position).getImg());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProfRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("Name");
        DatabaseReference myStarRef = database.getReference(FirebaseAuth.getInstance().getUid()).child("stars");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cost = categoryList.get(position).getCost();

                myStarRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        stars = (long) snapshot.getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if(stars>cost){
                    myStarRef.getRef().setValue(stars - cost);
                } else {
                    Toast.makeText(v.getContext(), "Not enough stars!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
