package com.example.ingredientparser;

public class Document {
    private String name;
    private boolean isSelected;

    public Document(String name) {
        this.name = name;
        isSelected = false; // Initially not selected
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
