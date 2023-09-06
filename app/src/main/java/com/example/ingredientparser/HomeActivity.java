
package com.example.ingredientparser;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private Switch veganSwitch;

    private SharedPreferences preferences;

    private ImageView badgeImageView;

    private int scanCount;

    int remScans;

    int totalCount = 0;

    String userEmail = "there!";

     int scansValue;
    TextView scanCountView;
    TextView remScansView;


    BottomNavigationView bottomNavigationView;

    String badgeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        scanCountView = findViewById(R.id.scanCountView);
         remScansView= findViewById(R.id.remainingScans);
        TextView greetingUser = findViewById(R.id.greeting);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // The user is signed in
            userEmail = user.getEmail();
            if (userEmail.contains("@gmail.com")) {
                // Remove "@gmail.com" from the email
                userEmail = userEmail.split("@")[0];
                userEmail = userEmail.toLowerCase();

            }

            // userEmail now contains the user's email
        }
        greetingUser.setText("Hi "+userEmail+", \nHere's your progress");

        ImageView imageView = findViewById(R.id.badgeImageView);

        veganSwitch = findViewById(R.id.veganSwitch);

        GetScanCountFromDB();
        System.out.println("Value after the method: "+scansValue);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
       // SharedPreferences.Editor editor = preferences.edit();
        boolean isVeganSwitchActivated = preferences.getBoolean("veganSwitch", false);
        veganSwitch.setChecked(isVeganSwitchActivated);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userEmail", userEmail);
        editor.apply();



        veganSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //editor.putBoolean("veganSwitch", isChecked);
            //editor.apply();
            preferences.edit().putBoolean("veganSwitch", isChecked).apply();
        });



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(HomeActivity.this, BadgeDisplay.class);
            startActivity(intent);

            }
        });

        CardView cardViewAllergy = findViewById(R.id.cardViewAllergy);

        cardViewAllergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, AllergyHomeActivity.class);
                startActivity(intent);

            }
        });

        CardView cardViewVegan = findViewById(R.id.cardViewVegan);

        cardViewVegan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, VeganHomeActivity.class);
                startActivity(intent);

            }
        });

        CardView cardViewEnvironment = findViewById(R.id.cardViewEnv);
        cardViewEnvironment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, EnvironmentHomeActivity.class);
                startActivity(intent);

            }
        });

        CardView cardViewCustom = findViewById(R.id.cardViewCustom);
        cardViewCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, CustomHomeActivity.class);
                startActivity(intent);

            }
        });


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.add) {
                    startActivity(new Intent(getApplicationContext(), AddActivity.class));
                    overridePendingTransition(0, 0);                    return true;

                } else if (itemId == R.id.info) {
                    startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;


            }
        });
    }

    private void GetScanCountFromDB()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentRef = db.collection("Leaderboards").document(userEmail);

        documentRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {


                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("Firebase", "came inside OnSuccess");
                        if (documentSnapshot.exists()) {
                            Log.d("Firebase", "Inside onSuccess");
                            int scans = documentSnapshot.getLong("Scans").intValue();
                            System.out.println("Scan count is:"+scans);
                            badgeImageView = findViewById(R.id.badgeImageView);
                            //loadScanCount();


                            updateBadge(scans); // Update the badge based on the loaded scan count
                            String scan = "Healthy Ingredient Scans : " + scans+"/"+totalCount;
                            scanCountView.setText(scan);
                            remScans = totalCount-scans;
                            if(scans > 3000)
                            {
                                remScansView.setText("You have unlocked all tiers!");

                            }
                            else {
                                remScansView.setText("Scan " + remScans + " healthy ingredients more to unlock the next tier - "+badgeName);
                            }

                            int endValue =0;

                            int startValue = 0;


                            int duration = 1000;

                            if (scans >= 0 && scans < 1000) {
                                totalCount = 1000;
                                endValue = (int) ((float) scans / totalCount * 100);
                                startValue = 0; // End at 100%
                            } else if (scans >= 1000 && scans < 2000) {
                                totalCount = 2000;
                                endValue = (int) ((float) (scans - 1000) / (totalCount - 1000) * 100);
                                startValue = 0; // End at 100%
                            } else if (scans >= 2000 && scans < 3000) {
                                totalCount = 3000;
                                endValue = (int) ((float) (scans - 2000) / (totalCount - 2000) * 100);
                                startValue = 0; // End at 100%
                            } else {
                                totalCount = 10000;
                                endValue = (int) ((float) scans / totalCount * 100);
                                startValue = 0; // End at 100%
                            }

                            ProgressBar progressBar = findViewById(R.id.progressBar);
                            progressBar.setMax(100);
                            ObjectAnimator.ofInt(progressBar, "progress", startValue, endValue)
                                    .setDuration(duration)
                                    .start();

                        } else {
                            Log.d("LoadScanCount", "Could not load ScanCount: ");
                        }
                    }

                })


                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred during the operation
                        Log.e("Firestore", "Error getting document for scanCount", e);
                    }
                });

    }


    private void loadScanCount()
    {
        SharedPreferences preferences = getSharedPreferences("CounterPrefs", MODE_PRIVATE);
        scanCount = preferences.getInt("counter", 0);

    }

    private void updateBadge(int scansValue) {
        System.out.println("Update badge"+scansValue);
        if (scansValue >= 0 && scansValue<1000) {
            badgeImageView.setImageResource(R.drawable.novice_foodie);
            totalCount = 1000;
            badgeName = "Food Detective";
        }
        else if (scansValue >= 1000 && scansValue<2000)
        {
            badgeImageView.setImageResource(R.drawable.beginner_foodie);
            totalCount = 2000;
            badgeName = "Wellness Watcher";

        }else if (scansValue>=2000 && scansValue<3000)
        {
            badgeImageView.setImageResource(R.drawable.expert_foodie);
            totalCount = 3000;
            badgeName = "Health Maestro";

        }
        else if(scansValue>3000)
        {

            badgeImageView.setImageResource(R.drawable.master_foodie);
            totalCount = 10000;
        }
    }
}




