package com.example.ingredientparser;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private List<User> userList;
    private String currentUser;

    public LeaderboardAdapter(List<User> userList, String currentUser) {
        this.userList = userList;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userIconImageView.setImageResource(user.getIconDrawableId());
        holder.usernameTextView.setText(user.getUsername());
        holder.totalScansTextView.setText("Total Scans: " + user.getTotalScans());

        if (currentUser != null && currentUser.equals(user.getUsername())) {
            // Customize the background color for the current user's item
            holder.usernameTextView.setText(user.getUsername() + " (You)");
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.currentUserBackgroundColor));

        } else {
            // Reset the background color for other items
            holder.usernameTextView.setText(user.getUsername());
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);

        }
        holder.positionTextView.setText(String.valueOf(position + 1)); // Add 1 to start counting from 1

    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userIconImageView;
        TextView usernameTextView;
        TextView totalScansTextView;

        TextView positionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userIconImageView = itemView.findViewById(R.id.userIconImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            totalScansTextView = itemView.findViewById(R.id.totalScansTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView); // Add position TextView

        }
    }
}
