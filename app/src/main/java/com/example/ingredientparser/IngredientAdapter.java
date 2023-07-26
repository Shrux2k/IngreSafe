package com.example.ingredientparser;

import com.example.ingredientparser.Ingredient;
import com.example.ingredientparser.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class IngredientAdapter extends ExpandableRecyclerViewAdapter<IngredientAdapter.IngredientViewHolder, IngredientAdapter.DescriptionViewHolder> {

    public IngredientAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public IngredientViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public DescriptionViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
        return new DescriptionViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(IngredientViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setIngredientName(group.getTitle());
    }

    @Override
    public void onBindChildViewHolder(DescriptionViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Ingredient ingredient = (Ingredient) group.getItems().get(childIndex);
        holder.setIngredientDescription(ingredient.getDescription());
    }

    static class IngredientViewHolder extends GroupViewHolder {
        private TextView groupNameTextView;

        IngredientViewHolder(View itemView) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
        }

        void setIngredientName(String ingredientName) {
            groupNameTextView.setText(ingredientName);
        }
    }

    static class DescriptionViewHolder extends ChildViewHolder {
        private TextView descriptionTextView;

        DescriptionViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }

        void setIngredientDescription(String ingredientDescription) {
            descriptionTextView.setText(ingredientDescription);
        }
    }
}
