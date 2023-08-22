
package com.example.ingredientparser;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private Switch veganSwitch;
    private SharedPreferences preferences;

    private ImageView badgeImageView;
    private int scanCount;

    int remScans;

    int totalCount = 0;


    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        TextView scanCountView = findViewById(R.id.scanCountView);
        TextView remScansView = findViewById(R.id.remainingScans);


        veganSwitch = findViewById(R.id.veganSwitch);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        boolean isVeganSwitchActivated = preferences.getBoolean("veganSwitch", false);
        veganSwitch.setChecked(isVeganSwitchActivated);

        veganSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //editor.putBoolean("veganSwitch", isChecked);
            //editor.apply();
            preferences.edit().putBoolean("veganSwitch", isChecked).apply();
        });

        badgeImageView = findViewById(R.id.badgeImageView);
        loadScanCount(); // Load the scan count from SharedPreferences


        updateBadge(); // Update the badge based on the loaded scan count
        String scan = "Total Scans : " + scanCount+"/"+totalCount;
        scanCountView.setText(scan);
        remScans = totalCount-scanCount;
        remScansView.setText(remScans+" scans left to unlock the next badge");

        ProgressBar progressBar = findViewById(R.id.progressBar);
        ObjectAnimator.ofInt(progressBar, "progress", 0, scanCount)
                .setDuration(1000) // Animation duration in milliseconds
                .start();


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.add);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;

                } else if (itemId == R.id.add) {
                    startActivity(new Intent(getApplicationContext(), AddActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;

                } else if (itemId == R.id.info) {
                    startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;


            }
        });
    }

    private void loadScanCount()
    {
        SharedPreferences preferences = getSharedPreferences("CounterPrefs", MODE_PRIVATE);
        scanCount = preferences.getInt("counter", 0);

    }

    private void updateBadge() {
        if (scanCount >= 300) {
            badgeImageView.setImageResource(R.drawable.expert_foodie);
            totalCount = 10000;
        } else if (scanCount >= 100) {
            badgeImageView.setImageResource(R.drawable.beginner_foodie);
            totalCount = 300;
        } else {
            badgeImageView.setImageResource(R.drawable.novice_foodie);
            totalCount = 100;
        }
    }
}




