package com.example.ingredientparser;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navColorFix));
        setContentView(R.layout.activity_note);



        ProgressBar progressBar = findViewById(R.id.progressBarNote);
        progressBar.setVisibility(View.INVISIBLE);

        allergensList = new ArrayList<>();

        allergensList.add("Sesame");
        allergensList.add("Mustard");
        allergensList.add("Mustard Seed");



        veganList = new ArrayList<>();
        veganList.add("Nothing");


        //textView = findViewById(R.id.textdata);

        //view = findViewById(R.id.ViewButton);
        button = findViewById(R.id.button);


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

                } else if (itemId == R.id.info) {
                    startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;


            }
        });
        //Allergen check
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isCelerySwitchActivated = preferences.getBoolean("switchCelery", false);

        if(isCelerySwitchActivated)
        {
            allergensList.add("Celery");
        }

        boolean isEggSwitchActivated = preferences.getBoolean("switchChickenEgg", false);

        if(isEggSwitchActivated)
        {
            allergensList.add("Egg");
        }

        boolean isDairySwitchActivated = preferences.getBoolean("switchDairy", false);

        if(isDairySwitchActivated)
        {
            allergensList.add("Milk");
        }

        boolean isFishSwitchActivated = preferences.getBoolean("switchFish", false);

        if(isFishSwitchActivated)
        {
            allergensList.add("Fish");
            allergensList.add("Shellfish");
        }

        boolean isGlutenSwitchActivated = preferences.getBoolean("switchGluten", false);

        if(isGlutenSwitchActivated)
        {
            allergensList.add("Wheat");
            allergensList.add("Barley");
            allergensList.add("Malt");
            allergensList.add("Barley Malt Extract");

        }
        boolean isPeanutSwitchActivated = preferences.getBoolean("switchPeanut", false);

        if(isPeanutSwitchActivated)
        {
            allergensList.add("Peanut");
        }

        boolean isSoybeanSwitchActivated = preferences.getBoolean("switchSoybean", false);

        if(isSoybeanSwitchActivated)
        {
            allergensList.add("Soybean");
            allergensList.add("Soy");
            allergensList.add("Soya Bean");
        }

        boolean isTreenutsSwitchActivated = preferences.getBoolean("switchTreenuts", false);

        if(isTreenutsSwitchActivated)
        {
            allergensList.add("Tree Nuts");
            allergensList.add("Nut");
            allergensList.add("Nuts");

        }
        //Vegan check

        boolean isMeatSwitchActivated = preferences.getBoolean("switchMeatV", false);

        if(isMeatSwitchActivated)
        {
            veganList.add("Chicken Breast");
            veganList.add("Beef");
            veganList.add("Pork");
            veganList.add("Chicken");
            veganList.add("Lamb");
            veganList.add("Cochineal");

        }

        boolean isEggVSwitchActivated = preferences.getBoolean("switchChickenEggV", false);

        if(isEggVSwitchActivated)
        {
            veganList.add("Egg");
        }

        boolean isDairySwitchActivatedV = preferences.getBoolean("switchDairyV", false);

        if(isDairySwitchActivatedV)
        {
            veganList.add("Cheese");
            veganList.add("Butter");
            veganList.add("Yogurt");
            veganList.add("Egg");
            veganList.add("Milk");

        }

        boolean isFishVSwitchActivated = preferences.getBoolean("switchFishV", false);

        if(isFishVSwitchActivated)
        {
            veganList.add("Fish");
            veganList.add("Seafood");

        }

        boolean isGeneralSwitchActivated = preferences.getBoolean("switchGeneralV", false);

        if(isGeneralSwitchActivated)
        {
            veganList.add("Honey");
            veganList.add("Gelatin");
            veganList.add("Casein");
            veganList.add("Lard");
            veganList.add("Tallow");
            veganList.add("Rennet");
            veganList.add("Whey");
        }

        SharedPreferences preferencesCustom = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> selectedDocumentSet = preferencesCustom.getStringSet("SelectedDocuments", new HashSet<>());

        for (String document : selectedDocumentSet) {
            if(!allergensList.contains(document))
            {
                allergensList.add(document);
            }
        }


        Intent intent = getIntent();
        List<Ingredient> ingredientsList = (List<Ingredient>) intent.getSerializableExtra("INGREDIENTS_LIST");
        recognizedText = intent.getStringExtra("RECOGNIZED_TEXT");
        ArrayList<String> unhealthyIngredients = intent.getStringArrayListExtra("UNHEALTHY");
        String count =intent.getStringExtra("COUNT");
        System.out.println(count);


        allergensDisplay = new ArrayList<>();
        List<Ingredient> sortedIngredientsList = new ArrayList<>(ingredientsList);


        Collections.sort(sortedIngredientsList, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient ingredient1, Ingredient ingredient2) {
                boolean isAllergen1 = allergensList.contains(ingredient1.getName());
                boolean isAllergen2 = allergensList.contains(ingredient2.getName());
                boolean isVegan1 = veganList.contains(ingredient1.getName());
                boolean isVegan2 = veganList.contains(ingredient2.getName());

                if (!isVegan1 && isVegan2) {
                    return 1; // Ingredient1 is non-vegan, so it comes before vegan Ingredient2.
                } else if (isVegan1 && !isVegan2) {
                    return -1; // Ingredient2 is non-vegan, so it comes before vegan Ingredient1.
                }

                // If both are non-vegan or both are vegan, consider allergens.
                if (isAllergen1 && !isAllergen2) {
                    return -1; // Ingredient1 is an allergen, so it comes before Ingredient2.
                } else if (!isAllergen1 && isAllergen2) {
                    return 1; // Ingredient2 is an allergen, so it comes before Ingredient1.
                }

                // If both are non-vegan, both are vegan, or both are not allergens,
                // then maintain the original order.
                return 0;
            }
        });




        if (ingredientsList != null && !ingredientsList.isEmpty()) {
            List<IngredientGroup> ingredientGroups = new ArrayList<>();
            for (Ingredient ingredient : sortedIngredientsList) {
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

            Set<String> uniqueSet = new LinkedHashSet<>(allergensDisplay);
            allergensDisplay.clear();
            allergensDisplay.addAll(uniqueSet);



            if (!allergensDisplay.isEmpty()) {
                // Allergens are available, show the bottom sheet
                showBottomSheet();
            } else {
                Toast.makeText(this, "Hurray! No Allergens found", Toast.LENGTH_LONG).show();
            }


            // Allergens list

            button = findViewById(R.id.button);

            // Set an OnClickListener for the button
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.navColorFix));
                    Context context = NoteActivity.this;
                    //BottomSheetDialog dialog = new BottomSheetDialog(context);

                    BottomSheetDialog dialog = new BottomSheetDialog(context) {
                        @Override
                        public void onStart() {
                            super.onStart();
                            View bottomSheet = findViewById(com.google.android.material.R.id.design_bottom_sheet);
                            if (bottomSheet != null) {
                                // Set a fixed height for the bottom sheet
                                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.bottom_sheet_height);
                                bottomSheet.setLayoutParams(layoutParams);
                            }
                        }
                    };



                    int dialogHeight = getResources().getDimensionPixelSize(R.dimen.bottom_sheet_height); // Create dimen resource
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight);

                    View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheetlayout, null);
                    FlowLayout flowLayout = dialogView.findViewById(R.id.flowLayoutAllergens);

                    if (!allergensDisplay.isEmpty()) {


                        for (String allergen : allergensDisplay) {
                            TextView textView = new TextView(context);
                            textView.setText(allergen);
                            textView.setTextColor(Color.WHITE);
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
                    } else
                    {
                        Toast.makeText(context, "Hurray! No allergens found", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            Button button2 = findViewById(R.id.button2);

            // Set an OnClickListener for the button
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.navColorFix));

                    Context context = NoteActivity.this;
                    //BottomSheetDialog dialog = new BottomSheetDialog(context);
                    BottomSheetDialog dialog = new BottomSheetDialog(context) {
                        @Override
                        public void onStart() {
                            super.onStart();
                            View bottomSheet = findViewById(com.google.android.material.R.id.design_bottom_sheet);
                            if (bottomSheet != null) {
                                // Set a fixed height for the bottom sheet
                                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.bottom_sheet_height);
                                bottomSheet.setLayoutParams(layoutParams);
                            }
                        }
                    };

                    int dialogHeight = getResources().getDimensionPixelSize(R.dimen.bottom_sheet_height); // Create dimen resource
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight);


                    View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheetlayoutuh, null);
                    FlowLayout flowLayout = dialogView.findViewById(R.id.flowLayoutUnhealthy);



                    if (!unhealthyIngredients.isEmpty()) {


                        for (String ingredient: unhealthyIngredients) {
                            TextView textView = new TextView(context);
                            textView.setText(ingredient);
                            textView.setTextColor(Color.WHITE);
                            textView.setPadding(16, 8, 16, 8);
                            textView.setBackgroundResource(R.drawable.rounded_unhealthy_bubble); // Set background resource
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
                        Toast.makeText(context, "Hurray! No Unhealthy Ingredients found", Toast.LENGTH_SHORT).show();

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
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navColorFix));
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

