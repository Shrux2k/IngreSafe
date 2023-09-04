package com.example.ingredientparser;

public class User {
    private int iconDrawableId;
    private String username;
    private int totalScans;

    public User(int iconDrawableId, String username, int totalScans) {
        this.iconDrawableId = iconDrawableId;
        this.username = username;
        this.totalScans = totalScans;
    }

    // Getter methods for the properties
    public int getIconDrawableId() {
        return iconDrawableId;
    }

    public String getUsername() {
        return username;
    }

    public int getTotalScans() {
        return totalScans;
    }
}
