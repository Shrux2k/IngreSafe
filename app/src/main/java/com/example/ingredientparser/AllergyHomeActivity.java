package com.example.ingredientparser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;

public class AllergyHomeActivity extends AppCompatActivity {

    private Switch celery;

    private Switch egg;

    private Switch dairy;

    private Switch fish;

    private Switch gluten;

    private Switch peanut;

    private Switch soybean;

    private Switch treenuts;

    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy_home);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navColorFix));

        celery= findViewById(R.id.switchCelery);
        egg= findViewById(R.id.switchChickenEgg);
        dairy= findViewById(R.id.switchDairy);
        fish= findViewById(R.id.switchFish);
        gluten= findViewById(R.id.switchGluten);
        peanut= findViewById(R.id.switchPeanut);
        soybean= findViewById(R.id.switchSoybean);
        treenuts= findViewById(R.id.switchTreenuts);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //Celery
        boolean isCelerySwitchActivated = preferences.getBoolean("switchCelery", false);
        celery.setChecked(isCelerySwitchActivated);

        celery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchCelery", isChecked).apply();
        });

        //Chicken Egg
        boolean isEggSwitchActivated = preferences.getBoolean("switchChickenEgg", false);
        egg.setChecked(isEggSwitchActivated);

        egg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchChickenEgg", isChecked).apply();
        });

        //Dairy products
        boolean isDairySwitchActivated = preferences.getBoolean("switchDairy", false);
        dairy.setChecked(isDairySwitchActivated);

        dairy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchDairy", isChecked).apply();
        });

        //Fish and ShellFish
        boolean isFishSwitchActivated = preferences.getBoolean("switchFish", false);
        fish.setChecked(isFishSwitchActivated);

        fish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchFish", isChecked).apply();
        });

        //Gluten

        boolean isGlutenSwitchActivated = preferences.getBoolean("switchGluten", false);
        gluten.setChecked(isGlutenSwitchActivated);

        gluten.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchGluten", isChecked).apply();
        });

        //Peanut

        boolean isPeanutSwitchActivated = preferences.getBoolean("switchPeanut", false);
        peanut.setChecked(isPeanutSwitchActivated);

        peanut.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchPeanut", isChecked).apply();
        });

        //Soybean

        boolean isSoybeanSwitchActivated = preferences.getBoolean("switchSoybean", false);
        soybean.setChecked(isSoybeanSwitchActivated);

        soybean.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchSoybean", isChecked).apply();
        });

        //Tree nuts
        boolean isTreenutsSwitchActivated = preferences.getBoolean("switchTreenuts", false);
        treenuts.setChecked(isTreenutsSwitchActivated);

        treenuts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("switchTreenuts", isChecked).apply();
        });

        cardView = findViewById(R.id.SCformAllergen);



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