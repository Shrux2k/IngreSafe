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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;

import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NoteActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    TextView textView;

    String recognizedText;

    //Button analyse = findViewById(R.id.analyse);

    private List<String> allergensList;

    private boolean isIntentReceived = false;


    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_note);

        allergensList = new ArrayList<>();
        allergensList.add("Milk");
        allergensList.add("Eggs");
        allergensList.add("Peanuts");
        allergensList.add("Tree Nuts");
        allergensList.add("Fish");
        allergensList.add("Wheat");
        allergensList.add("Soy");
        allergensList.add("Sesame");
        allergensList.add("Mustard");
        allergensList.add("Crème Fraîche Milk");
        allergensList.add("Soya Bean");






        //textView = findViewById(R.id.textdata);

        //view = findViewById(R.id.ViewButton);
        button = findViewById(R.id.button);




        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //bottomNavigationView.setSelectedItemId(R.id.note);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                } else if (itemId == R.id.add) {
                    startActivity(new Intent(getApplicationContext(),AddActivity.class));
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

        Button button = findViewById(R.id.button);

        // Set an OnClickListener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This code will execute when the button is clicked
                // You can add your logic here
                analysebutton(); // Call the method you specified in android:onClick
            }
        });

        Intent intent = getIntent();
        List<Ingredient> ingredientsList = (List<Ingredient>) intent.getSerializableExtra("INGREDIENTS_LIST");
        recognizedText = intent.getStringExtra("RECOGNIZED_TEXT");


        if (ingredientsList != null && !ingredientsList.isEmpty()) {
            // Ingredients received via Intent, display the expandable RecyclerView
            List<IngredientGroup> ingredientGroups = new ArrayList<>();
            for (Ingredient ingredient : ingredientsList) {
                IngredientGroup group = new IngredientGroup(ingredient.getName(), Arrays.asList(ingredient));
                ingredientGroups.add(group);

                System.out.println("Group: " + group.getTitle());
                for (Ingredient child : group.getItems()) {
                    System.out.println("Child: " + child.getName() + " - " + child.getDescription());
                }
            }

            RecyclerView recyclerView = findViewById(R.id.expandableRecyclerView);
            IngredientAdapter adapter = new IngredientAdapter(ingredientGroups,allergensList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);


        } else {
            // No Ingredients received, show a toast message "You need to scan first."
            Toast.makeText(getApplicationContext(), "No Ingredients found, please scan.", Toast.LENGTH_LONG).show();
        }


    }

    private void analysebutton() {
        Intent intent = new Intent(NoteActivity.this, HealthActivity.class);
        intent.putExtra("RECOGNIZED_TEXT", recognizedText);
        startActivity(intent);
    }


}
