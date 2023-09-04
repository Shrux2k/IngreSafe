package com.example.ingredientparser;

public class Badge {
    private String name;
    private int imageResourceId;
    private int scansRequired;

    public Badge(String name, int imageResourceId, int scansRequired) {
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.scansRequired = scansRequired;
    }

    // Getter methods for the properties
    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getScansRequired() {
        return scansRequired;
    }
}
