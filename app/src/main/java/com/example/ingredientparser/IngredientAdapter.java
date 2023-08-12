package com.example.ingredientparser;

import com.example.ingredientparser.IngredientGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class IngredientAdapter extends ExpandableRecyclerViewAdapter<IngredientAdapter.IngredientViewHolder, IngredientAdapter.DescriptionViewHolder> {

    private IngredientGroup expandedGroup = null; // Variable to keep track of the currently expanded group

    private List<String> allergensList;


    public IngredientAdapter(List<? extends ExpandableGroup> groups, List<String> allergensList) {
        super(groups);
        this.allergensList = allergensList;

    }

    @Override
    public IngredientViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new IngredientViewHolder(view,allergensList);
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

        boolean isAllergen = allergensList.contains(group.getTitle());
        System.out.println("Group Title: " + group.getTitle() + " | Is Allergen: " + isAllergen);
        holder.setIngredientNameColor(isAllergen);
    }

    @Override
    public void onBindChildViewHolder(DescriptionViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Ingredient ingredient = (Ingredient) group.getItems().get(childIndex);
        holder.setIngredientDescription(ingredient.getDescription());
    }

    static class IngredientViewHolder extends GroupViewHolder {
        private TextView groupNameTextView;
        private TextView potentialAllergenTextView;

        private View arrow;

        private List<String> allergensList;



        IngredientViewHolder(View itemView, List<String> allergensList) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
            potentialAllergenTextView = itemView.findViewById(R.id.potentialAllergenTextView);
            arrow = itemView.findViewById(R.id.arrow);
            this.allergensList = allergensList;
        }

        void setIngredientName(ExpandableGroup group) {
            groupNameTextView.setText(group.getTitle());
            boolean isAllergen = allergensList.contains(group.getTitle());
            System.out.println("Group Title: " + group.getTitle() + " | Is Allergen: " + isAllergen);

            potentialAllergenTextView.setVisibility(isAllergen ? View.VISIBLE : View.GONE);
        }

        void setExpanded(boolean isExpanded) {
            arrow.setRotation(isExpanded ? 180 : 0); // Rotate the arrow icon based on the expansion state
        }

        void setIngredientNameColor(boolean isAllergen) {
            // Set the text color of the ingredient name based on whether it's an allergen or not
            int color = isAllergen ? Color.RED : Color.BLACK;
            groupNameTextView.setTextColor(color);
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
