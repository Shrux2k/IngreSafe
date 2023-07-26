package com.example.ingredientparser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    TextView textView;

    Button view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        textView = findViewById(R.id.textdata);

        view = findViewById(R.id.ViewButton);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.note);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (itemId == R.id.note) {
                    startActivity(new Intent(getApplicationContext(),NoteActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (itemId == R.id.add) {
                    startActivity(new Intent(getApplicationContext(),AddActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (itemId==R.id.health) {
                    startActivity(new Intent(getApplicationContext(),HealthActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (itemId==R.id.info) {
                    startActivity(new Intent(getApplicationContext(),InfoActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;


            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                ArrayList<String> ingredientsList = intent.getStringArrayListExtra("INGREDIENTS_LIST");
                //textView.setText(ingredientsList.toString());

                if (ingredientsList != null && !ingredientsList.isEmpty()) {
                    // Display the content if the ingredientsList has data
                    textView.setText(ingredientsList.toString());
                } else {
                    // Show a toast message "scan first" if ingredientsList is empty
                    Toast.makeText(getApplicationContext(), "You need to scan first", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}