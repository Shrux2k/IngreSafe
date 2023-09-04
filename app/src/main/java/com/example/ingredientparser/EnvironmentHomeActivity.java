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

public class EnvironmentHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_home);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navColorFix));



        CardView cardView = findViewById(R.id.SCformEnv);
        Switch plastic = findViewById(R.id.switchMicroplastics);
        Switch palm = findViewById(R.id.switchPalmOil);

        SharedPreferences preferences = getSharedPreferences("SwitchPreferences", MODE_PRIVATE);


        boolean plasticSwitch = preferences.getBoolean("plastic", false);
        plastic.setChecked(plasticSwitch);

        plastic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("plastic", isChecked).apply();
        });

        boolean palmSwitch = preferences.getBoolean("palm", false);
        palm.setChecked(palmSwitch);

        palm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("palm", isChecked).apply();
        });



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