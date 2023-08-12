package com.example.ingredientparser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class InfoActivity extends AppCompatActivity {

    private Switch veganSwitch;
    private SharedPreferences preferences;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        Switch veganSwitch = findViewById(R.id.veganSwitch);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        boolean isVeganSwitchActivated = preferences.getBoolean("veganSwitch", false);
        veganSwitch.setChecked(isVeganSwitchActivated);

        veganSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //editor.putBoolean("veganSwitch", isChecked);
            //editor.apply();
            preferences.edit().putBoolean("veganSwitch", isChecked).apply();
        });


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.add);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                } else if (itemId == R.id.add) {
                    startActivity(new Intent(getApplicationContext(), AddActivity.class));
                    overridePendingTransition(0, 0);
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
}
