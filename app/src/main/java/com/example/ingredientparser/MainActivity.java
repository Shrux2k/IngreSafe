package com.example.ingredientparser;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    //HomeFragment homeFragment = new HomeFragment();

    HomeActivity homeActivity = new HomeActivity();
    //NoteFragment noteFragment = new NoteFragment();

    NoteActivity noteActivity = new NoteActivity();


    //AddFragment addFragment = new AddFragment();

    AddActivity addActivity = new AddActivity();

   // HealthinessFragment healthinessFragment = new HealthinessFragment();

    HealthActivity healthActivity = new HealthActivity();

    //InformationFragment informationFragment = new InformationFragment();

    InfoActivity infoActivity = new InfoActivity();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        startActivity(new Intent(getApplicationContext(),AddActivity.class));
        overridePendingTransition(0,0);



        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.add);
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
    }

}

