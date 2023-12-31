package com.example.ingredientparser;

import static android.content.Context.MODE_PRIVATE;

import com.example.ingredientparser.IngredientGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class IngredientAdapter extends ExpandableRecyclerViewAdapter<IngredientAdapter.IngredientViewHolder, IngredientAdapter.DescriptionViewHolder> {

    private IngredientGroup expandedGroup = null; // Variable to keep track of the currently expanded group

    private List<String> allergensList;
    private List<String> veganList;



    public IngredientAdapter(List<? extends ExpandableGroup> groups, List<String> allergensList,List<String> veganList) {
        super(groups);
        this.allergensList = allergensList;
        this.veganList = veganList;


    }

    @Override
    public IngredientViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new IngredientViewHolder(view,allergensList,veganList);
    }

    @Override
    public DescriptionViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
        return new DescriptionViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(IngredientViewHolder holder, int flatPosition, ExpandableGroup group) {
        //holder.setIngredientName(group);
        holder.setIngredientName(group, ((IngredientGroup) group).getItems().get(0).getEmoji()); // Extract emoji from the first child of the group
        holder.setExpanded(group == expandedGroup); // Set the expansion state of the group

        boolean isAllergen = allergensList.contains(group.getTitle());
        boolean isVegan = veganList.contains(group.getTitle());

        System.out.println("Group Title: " + group.getTitle() + " | Is Allergen: " + isAllergen);
        holder.setIngredientNameColor(isAllergen,isVegan);
    }

    @Override
    public void onBindChildViewHolder(DescriptionViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Ingredient ingredient = (Ingredient) group.getItems().get(childIndex);
        holder.setIngredientDescription(ingredient.getDescription());
    }

    static class IngredientViewHolder extends GroupViewHolder {
        private TextView groupNameTextView;
        private TextView potentialAllergenTextView;
        private TextView potentialVeganTextView; // Add the potentialVeganTextView

        private View arrow;

        private List<String> allergensList;
        private List<String> veganList;



        IngredientViewHolder(View itemView, List<String> allergensList,List<String> veganList) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
            potentialAllergenTextView = itemView.findViewById(R.id.potentialAllergenTextView);
            potentialVeganTextView = itemView.findViewById(R.id.potentialVeganTextView);
            arrow = itemView.findViewById(R.id.arrow);
            this.allergensList = allergensList;
            this.veganList = veganList; // Initialize veganList

        }

        void setIngredientName(ExpandableGroup group, String emoji) {
            groupNameTextView.setText(group.getTitle()+"   "+emoji);
            boolean isAllergen = allergensList.contains(group.getTitle());
            boolean isVegan = veganList.contains(group.getTitle());
            boolean isBothAllergenAndNonVegan = isAllergen && isVegan;


            System.out.println("Group Title: " + group.getTitle() + " | Is Allergen: " + isAllergen);
            //String tempEmoji =  "\uD83E\uDD6C";
            //emojiTextView.setText(emoji); // Set the emoji
            //potentialAllergenTextView.setVisibility(isAllergen ? View.VISIBLE : View.GONE);
            if (isBothAllergenAndNonVegan) {
                potentialAllergenTextView.setVisibility(View.VISIBLE);
                potentialAllergenTextView.setText("(Allergen) ");
                potentialVeganTextView.setVisibility(View.VISIBLE);
                potentialVeganTextView.setText("(Non-vegan) ");
            } else {
                // If it's not both an allergen and non-vegan, handle each case separately
                potentialAllergenTextView.setVisibility(isAllergen ? View.VISIBLE : View.GONE);
                potentialAllergenTextView.setText("(Allergen) ");
                potentialVeganTextView.setVisibility(isVegan ? View.VISIBLE : View.GONE);
                potentialVeganTextView.setText("(Non-vegan) ");
            }
        }

        void setExpanded(boolean isExpanded) {
            arrow.setRotation(isExpanded ? 180 : 0); // Rotate the arrow icon based on the expansion state
        }

        void setIngredientNameColor(boolean isAllergen, boolean isVegan) {
            // Set the text color of the ingredient name based on whether it's an allergen or not
            int color;
            if (isAllergen) {
                color = Color.RED; // Set color for allergens
                //potentialAllergenTextView.setVisibility(View.VISIBLE);
                //potentialVeganTextView.setVisibility(View.GONE); // Hide potentialVeganTextView
            } else if (isVegan) {
                color = Color.parseColor("#FF8800");
                //potentialVeganTextView.setVisibility(View.VISIBLE); // Show potentialVeganTextView
                //groupNameTextView.setText("Vegan ingredient: " + groupNameTextView.getText()); // Add label for vegan ingredients
                //potentialAllergenTextView.setVisibility(View.GONE); // Hide potentialAllergenTextView
            } else {
                color = Color.WHITE;
                //potentialAllergenTextView.setVisibility(View.GONE);
                //potentialVeganTextView.setVisibility(View.GONE); // Hide potentialVeganTextView
            }

            groupNameTextView.setTextColor(color);
        }
    }

    static class DescriptionViewHolder extends ChildViewHolder {
        private TextView descriptionTextView;



        DescriptionViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            descriptionTextView.setTextColor(Color.WHITE);

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

