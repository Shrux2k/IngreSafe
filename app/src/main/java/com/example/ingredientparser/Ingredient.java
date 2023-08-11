package com.example.ingredientparser;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Ingredient implements Serializable, Parcelable {

        private String name;
        private String description;

       ;

        public Ingredient(String name, String description) {
            this.name = name;
            this.description = description;


        }


    protected Ingredient(Parcel in) {
        name = in.readString();
        description = in.readString();

    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
    }




}


