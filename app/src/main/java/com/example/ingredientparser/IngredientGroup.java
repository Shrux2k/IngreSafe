
package com.example.ingredientparser;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class IngredientGroup extends ExpandableGroup<Ingredient> {
    public IngredientGroup(String title, List<Ingredient> items) {
        super(title, items);
    }
}

