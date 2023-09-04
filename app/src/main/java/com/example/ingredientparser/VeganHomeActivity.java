package com.example.ingredientparser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class VeganHomeActivity extends AppCompatActivity {

    Switch meat;
    Switch egg;
    Switch dairy;
    Switch fish;
    Switch general;
    Switch numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegan_home);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navColorFix));


        meat= findViewById(R.id.switchMeatV);
        egg= findViewById(R.id.switchChickenEggV);
        dairy= findViewById(R.id.switchDairyV);
        fish= findViewById(R.id.switchFishV);
        general= findViewById(R.id.switchGeneralV);
        numbers= findViewById(R.id.switchNumbersV);


        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //Celery
        boolean isMeatSwitchActivated = preferences.getBoolean("switchMeatV", false);
        meat.setChecked(isMeatSwitchActivated);

        meat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchMeatV", isChecked).apply();
        });

        //Chicken Egg
        boolean isEggSwitchActivated = preferences.getBoolean("switchChickenEggV", false);
        egg.setChecked(isEggSwitchActivated);

        egg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchChickenEggV", isChecked).apply();
        });

        //Dairy products
        boolean isDairySwitchActivated = preferences.getBoolean("switchDairyV", false);
        dairy.setChecked(isDairySwitchActivated);

        dairy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchDairyV", isChecked).apply();
        });

        //Fish and ShellFish
        boolean isFishSwitchActivated = preferences.getBoolean("switchFishV", false);
        fish.setChecked(isFishSwitchActivated);

        fish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchFishV", isChecked).apply();
        });

        //Gluten

        boolean isGeneralSwitchActivated = preferences.getBoolean("switchGeneralV", false);
        general.setChecked(isGeneralSwitchActivated);

        general.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchGeneralV", isChecked).apply();
        });

        //Peanut

        boolean isNumbersSwitchActivated = preferences.getBoolean("switchNumbersV", false);
        numbers.setChecked(isNumbersSwitchActivated);

        numbers.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchNumbersV", isChecked).apply();
        });

        CardView cardView = findViewById(R.id.SCformVegan);



        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the URL of your Google Form
                String googleFormUrl = "https://forms.gle/8x7y4Nyys6o1KAJ76";

                // Create an intent to open a web browser
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(googleFormUrl));

                // Start the web browser activity to open the Google Form
                startActivity(intent);
            }
        });

    }
}