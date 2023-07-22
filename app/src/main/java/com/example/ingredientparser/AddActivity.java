package com.example.ingredientparser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
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

public class AddActivity extends AppCompatActivity {
    Button button_capture;
    TextView textview_data;
    Bitmap bitmap;

    Button button_camera;

    Uri imageUri;


    int flag = 0;
    private static final int REQUEST_CAMERA_CODE = 100;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        button_capture = findViewById(R.id.button_capture);
        button_camera = findViewById(R.id.button_camera);
        textview_data = findViewById(R.id.text_data);


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
                } else if (itemId == R.id.note) {
                    startActivity(new Intent(getApplicationContext(), NoteActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.add) {
                    startActivity(new Intent(getApplicationContext(), AddActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.health) {
                    startActivity(new Intent(getApplicationContext(), HealthActivity.class));
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
        if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddActivity.this);


            }

        });
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Sample Title");
                values.put(MediaStore.Images.Media.TITLE, "Sample Description");

                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraActivityResultLauncher.launch(intent);

                //CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);


            }
            private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        flag = 1;
                        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddActivity.this);
                    } else {
                        Toast.makeText(AddActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }

                }

            });
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if(flag==1)
            {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    getTextFromImage(bitmap);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);

                    getTextFromImage(bitmap);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    private void getTextFromImage(Bitmap bitmap) {

        try {
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            TextRecognizer recognizer =
                    TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            Task<Text> result = recognizer.process(image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text)
                        {
                            String recognizedText = text.getText();
                            textview_data.setText(recognizedText);
                            System.out.println("Done");
                            processData(recognizedText);

                            // ...
                        }
                    })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    // ...
                                    Toast.makeText(AddActivity.this,"Failed" +e.getMessage(),Toast.LENGTH_LONG).show();
                                    System.out.println("Failed");
                                }
                            });
        }catch(Exception e)
        {
            e.printStackTrace();

        }



    }

    private void processData(String recognizedText) {
        // Split the string by spaces to get individual words
        String preprocessedText  = recognizedText.replaceAll("[\\[\\]()0-9%]", "");

        int ingredientsIndex = preprocessedText.toLowerCase().indexOf("ingredients");

        if (ingredientsIndex != -1) {
            // If "Ingredients" is found, extract the substring after it
            String ingredientsString = preprocessedText.substring(ingredientsIndex + "Ingredients".length()).trim();
            // Use an ArrayList to store the words
            String[] words = ingredientsString.split(",");
            ArrayList<String> ingredientList = new ArrayList<>();


            for (String word : words) {
                // Add each word to the ArrayList
                ingredientList.add(word);
            }

            int numOfIngredients = ingredientList.size();

            for (int i = 0; i < numOfIngredients; i++) {
                // Retrieve each ingredient one by one from the ArrayList and display it
                System.out.println("Ingredient " + i + ": " + ingredientList.get(i));
                //Log.d(TAG, "Ingredient " + i + ": " + ingredientList.get(i));
            }
        }


    }

}

