package com.example.ingredientparser;

import com.example.ingredientparser.IngredientGroup;
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

    private IngredientGroup expandedGroup = null; // Variable to keep track of the currently expanded group

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
        holder.setIngredientName(group);
        holder.setExpanded(group == expandedGroup); // Set the expansion state of the group
    }

    @Override
    public void onBindChildViewHolder(DescriptionViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Ingredient ingredient = (Ingredient) group.getItems().get(childIndex);
        holder.setIngredientDescription(ingredient.getDescription());
    }

    static class IngredientViewHolder extends GroupViewHolder {
        private TextView groupNameTextView;
        private View arrow;

        IngredientViewHolder(View itemView) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
            arrow = itemView.findViewById(R.id.arrow);
        }

        void setIngredientName(ExpandableGroup group) {
            groupNameTextView.setText(group.getTitle());
        }

        void setExpanded(boolean isExpanded) {
            arrow.setRotation(isExpanded ? 180 : 0); // Rotate the arrow icon based on the expansion state
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

    // Method to set the currently expanded group
    public void setExpandedGroup(IngredientGroup group) {
        expandedGroup = group;
    }
}
