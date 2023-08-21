package com.example.ingredientparser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ingredientparser.UnhealthyIngredient;

import java.util.List;

public class UnhealthyIngredientAdapter extends RecyclerView.Adapter<UnhealthyIngredientAdapter.ViewHolder> {

    private List<UnhealthyIngredient> unhealthyIngredients;

    public UnhealthyIngredientAdapter(List<UnhealthyIngredient> unhealthyIngredients) {
        this.unhealthyIngredients = unhealthyIngredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_unhealthy_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UnhealthyIngredient ingredient = unhealthyIngredients.get(position);
        holder.textName.setText(ingredient.getName());
        holder.textDescription.setText(ingredient.getDescription());
    }

    @Override
    public int getItemCount() {
        return unhealthyIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDescription = itemView.findViewById(R.id.textDescription);
        }
    }
}
