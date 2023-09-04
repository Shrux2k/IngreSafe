package com.example.ingredientparser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;
import androidx.preference.SwitchPreferenceCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;


public class InfoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseFirestore firestore;

    String userEmail;

    Switch partial;
    Switch power;
    Switch text;


    TextView logout;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_info);

        firestore = FirebaseFirestore.getInstance();

        CardView feedbackForm = findViewById(R.id.feedback);
        CardView logoutButton = findViewById(R.id.logout);
        partial = findViewById(R.id.matches);
        power = findViewById(R.id.lowpower);
        text = findViewById(R.id.textrecog);

        // Save switch preferences
        SharedPreferences preferences = getSharedPreferences("SwitchPreferences", MODE_PRIVATE);


        boolean partialSwitch = preferences.getBoolean("partial", false);
        partial.setChecked(partialSwitch);

        partial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("partial", isChecked).apply();
        });

        boolean powerSwitch = preferences.getBoolean("power", false);
        power.setChecked(powerSwitch);

        power.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("power", isChecked).apply();
        });

        boolean textSwitch = preferences.getBoolean("text", false);
        text.setChecked(textSwitch);

        text.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("text", isChecked).apply();
        });








        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.info);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        logout = findViewById(R.id.logoutuser);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // The user is signed in
            userEmail = user.getEmail();
            if (userEmail.contains("@gmail.com")) {
                // Remove "@gmail.com" from the email
                userEmail = userEmail.replace("@gmail.com", "");
            }
            // userEmail now contains the user's email
        }

        logout.setText("Logout\n"+"("+userEmail+")");





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
                    overridePendingTransition(0, 0);                    return true;

                }
                return false;


            }

        });

        feedbackForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String googleFormUrl = "https://forms.gle/bDtTrXVUEfyiLtMQ6";

                // Create an intent to open a web browser
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(googleFormUrl));

                // Start the web browser activity to open the Google Form
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remembered", false);
                editor.apply();
                Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
