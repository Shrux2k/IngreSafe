package com.example.ingredientparser;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;

import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.emoji.text.EmojiCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NoteActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    TextView textView;

    String recognizedText;



    //Button analyse = findViewById(R.id.analyse);

    private List<String> allergensList;
    private List<String> veganList;

    private boolean isIntentReceived = false;

    private List<String> allergensDisplay;

    private List<String> unhealthy = new ArrayList<>();


    Button button;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_note);


        ProgressBar progressBar = findViewById(R.id.progressBarNote);
        progressBar.setVisibility(View.INVISIBLE);

        allergensList = new ArrayList<>();
        allergensList.add("Milk");
        allergensList.add("Egg");
        allergensList.add("Peanuts");
        allergensList.add("Tree Nuts");
        allergensList.add("Fish");
        allergensList.add("Wheat");
        allergensList.add("Soy");
        allergensList.add("Sesame");
        allergensList.add("Mustard");
        allergensList.add("Crème Fraîche Milk");
        allergensList.add("Soya Bean");
        allergensList.add("Pasteurised Egg Yolk");
        allergensList.add("Barley Malt Extract");
        allergensList.add("Mustard Seed");
        allergensList.add("Barley");


        veganList = new ArrayList<>();
        veganList.add("Nothing");


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

        //Button button = findViewById(R.id.button);

        // Set an OnClickListener for the button
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This code will execute when the button is clicked
                // You can add your logic here
                analysebutton(); // Call the method you specified in android:onClick
            }
        });*/

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isVeganSwitchActivated = preferences.getBoolean("veganSwitch", false);

// Use isVeganSwitchActivated to determine whether to apply colors and messages
        if (isVeganSwitchActivated) {
            veganList = new ArrayList<>();
            veganList.add("Chicken Breast");
            veganList.add("Egg");
            veganList.add("Beef");
            veganList.add("Pork");
            veganList.add("Chicken");
            veganList.add("Lamb");
            //veganList.add("Milk");
            veganList.add("Cheese");
            veganList.add("Butter");
            veganList.add("Yogurt");
            //veganList.add("Eggs");
            veganList.add("Honey");
            veganList.add("Gelatin");
            veganList.add("Fish");
            veganList.add("Seafood");
            veganList.add("Cochineal");
            veganList.add("Casein");
            veganList.add("Lard");
            veganList.add("Tallow");
            veganList.add("Rennet");
            veganList.add("Whey");
            veganList.add("Animal-derived Ingredients");
        }


        Intent intent = getIntent();
        List<Ingredient> ingredientsList = (List<Ingredient>) intent.getSerializableExtra("INGREDIENTS_LIST");
        recognizedText = intent.getStringExtra("RECOGNIZED_TEXT");
        ArrayList<String> unhealthyIngredients = intent.getStringArrayListExtra("UNHEALTHY");
        String count =intent.getStringExtra("COUNT");
        System.out.println(count);


        allergensDisplay = new ArrayList<>();
        if (ingredientsList != null && !ingredientsList.isEmpty()) {
            // Ingredients received via Intent, display the expandable RecyclerView
            List<IngredientGroup> ingredientGroups = new ArrayList<>();
            for (Ingredient ingredient : ingredientsList) {
                IngredientGroup group = new IngredientGroup(ingredient.getName(), Arrays.asList(ingredient));
                ingredientGroups.add(group);


                System.out.println("Group: " + group.getTitle());
                for (Ingredient child : group.getItems()) {
                    if (allergensList.contains(child.getName())) {
                        System.out.println("New fn ------ " + child.getName());
                        allergensDisplay.add(group.getTitle());
                    }
                }
            }


            if (!allergensDisplay.isEmpty()) {
                // Allergens are available, show the bottom sheet
                showBottomSheet();
            } else {
                // No allergens found, show a message
                showNoAllergensMessage();
            }


            // Allergens list

            button = findViewById(R.id.button);

            // Set an OnClickListener for the button
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = NoteActivity.this;
                    BottomSheetDialog dialog = new BottomSheetDialog(context);

                    View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheetlayout, null);
                    FlowLayout flowLayout = dialogView.findViewById(R.id.flowLayoutAllergens);

                    if (!allergensDisplay.isEmpty()) {


                        for (String allergen : allergensDisplay) {
                            TextView textView = new TextView(context);
                            textView.setText(allergen);
                            textView.setTextColor(Color.BLACK);
                            textView.setPadding(16, 8, 16, 8);
                            textView.setBackgroundResource(R.drawable.rounded_allergen_bubble); // Set background resource
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(0, 0, 16, 0); // Add margin between bubbles
                            textView.setLayoutParams(layoutParams);
                            flowLayout.addView(textView);
                        }

                        dialog.setContentView(dialogView);
                        dialog.show();
                    } else {
                        textView.setText("Hurray! No Allergens Found");
                        dialog.setContentView(dialogView);
                        dialog.show();
                    }
                }
            });


            Button button2 = findViewById(R.id.button2);

            // Set an OnClickListener for the button
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = NoteActivity.this;
                    BottomSheetDialog dialog = new BottomSheetDialog(context);

                    View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheetlayoutuh, null);
                    FlowLayout flowLayout = dialogView.findViewById(R.id.flowLayoutUnhealthy);

                    if (!unhealthyIngredients.isEmpty()) {


                        for (String ingredient: unhealthyIngredients) {
                            TextView textView = new TextView(context);
                            textView.setText(ingredient);
                            textView.setTextColor(Color.BLACK);
                            textView.setPadding(16, 8, 16, 8);
                            textView.setBackgroundResource(R.drawable.rounded_allergen_bubble); // Set background resource
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(0, 0, 16, 0); // Add margin between bubbles
                            textView.setLayoutParams(layoutParams);
                            flowLayout.addView(textView);
                        }

                        dialog.setContentView(dialogView);
                        dialog.show();
                    } else {
                        Context context2 = NoteActivity.this;
                        BottomSheetDialog dialog2 = new BottomSheetDialog(context);
                        View dialogView2 = LayoutInflater.from(context2).inflate(R.layout.bottomsheetlayout, null);
                        textView.setText("Hurray! No Unhealthy ingredients found");
                        dialog.setContentView(dialogView2);
                        dialog2.show();
                    }
                }
            });


            RecyclerView recyclerView = findViewById(R.id.expandableRecyclerView);
            IngredientAdapter adapter = new IngredientAdapter(ingredientGroups, allergensList, veganList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);


        } else {
            // No Ingredients received, show a toast message "You need to scan first."
            Toast.makeText(getApplicationContext(), "No Ingredients found, please scan.", Toast.LENGTH_LONG).show();
        }


    }
    private void showBottomSheet() {
        Context context = NoteActivity.this;
        BottomSheetDialog dialog = new BottomSheetDialog(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheetlayout, null);
        FlowLayout flowLayout = dialogView.findViewById(R.id.flowLayoutAllergens);

        for (String allergen : allergensDisplay) {
            TextView textView = new TextView(context);
            textView.setText(allergen);
            textView.setTextColor(Color.BLACK);
            textView.setPadding(16, 8, 16, 8);
            textView.setBackgroundResource(R.drawable.rounded_allergen_bubble);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 16, 0);
            textView.setLayoutParams(layoutParams);
            flowLayout.addView(textView);
        }

        dialog.setContentView(dialogView);
        dialog.show();
    }

    private void showNoAllergensMessage() {
        // Show a message when no allergens are found
        // You can display this message in a TextView or another view in your layout

        Context context = NoteActivity.this;
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheetlayout, null);
        TextView textView = new TextView(context);
        textView.setText("Hurray! No Allergens Found");
        dialog.setContentView(dialogView);
        dialog.show();
    }


}

