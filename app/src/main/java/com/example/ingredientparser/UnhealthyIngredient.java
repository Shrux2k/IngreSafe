package com.example.ingredientparser;

public class UnhealthyIngredient {
    private String name;
    private String description;

    public UnhealthyIngredient(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
