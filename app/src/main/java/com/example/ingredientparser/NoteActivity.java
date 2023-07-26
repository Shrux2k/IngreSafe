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
import java.util.Arrays;
import java.util.List;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NoteActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    TextView textView;

    private boolean isIntentReceived = false;


    Button view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        //textView = findViewById(R.id.textdata);

        //view = findViewById(R.id.ViewButton);



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

        Intent intent = getIntent();
        List<Ingredient> ingredientsList = (List<Ingredient>) intent.getSerializableExtra("INGREDIENTS_LIST");




        if (ingredientsList != null && !ingredientsList.isEmpty()) {
            // Ingredients received via Intent, display the expandable RecyclerView
            List<IngredientGroup> ingredientGroups = new ArrayList<>();
            for (Ingredient ingredient : ingredientsList) {
                IngredientGroup group = new IngredientGroup(ingredient.getName(), Arrays.asList(ingredient));
                ingredientGroups.add(group);
            }

            RecyclerView recyclerView = findViewById(R.id.expandableRecyclerView);
            IngredientAdapter adapter = new IngredientAdapter(ingredientGroups);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            // No Ingredients received, show a toast message "You need to scan first."
            Toast.makeText(getApplicationContext(), "You need to scan first.", Toast.LENGTH_LONG).show();
        }

    }

    private void setupRecyclerView(List<Ingredient> ingredientsList) {
        RecyclerView recyclerView = findViewById(R.id.expandableRecyclerView);
        List<ExpandableGroup> ingredientGroups = new ArrayList<>();

        // Assuming you have a list of Ingredient objects called 'ingredientsList'
        IngredientGroup group = new IngredientGroup("Ingredients", ingredientsList);
        ingredientGroups.add(group);

        IngredientAdapter adapter = new IngredientAdapter(ingredientGroups);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}