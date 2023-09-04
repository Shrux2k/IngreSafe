package com.example.ingredientparser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.ViewHolder> {
    private List<Badge> badges;

    public BadgeAdapter(List<Badge> badges) {
        this.badges = badges;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Badge badge = badges.get(position);
        holder.badgeImageView.setImageResource(badge.getImageResourceId());
        holder.badgeNameTextView.setText(badge.getName());
        holder.badgeScansTextView.setText("Minimum Scans Required: " + badge.getScansRequired());
    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView badgeImageView;
        TextView badgeNameTextView;
        TextView badgeScansTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            badgeImageView = itemView.findViewById(R.id.badgeImageView);
            badgeNameTextView = itemView.findViewById(R.id.badgeNameTextView);
            badgeScansTextView = itemView.findViewById(R.id.badgeScansTextView);
        }
    }
}
