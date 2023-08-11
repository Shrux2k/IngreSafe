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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.io.Serializable;

public class AddActivity extends AppCompatActivity {
    Button button_capture;
    //TextView textview_data;
    Bitmap bitmap;

    Button button_camera;

    Uri imageUri;

    FirebaseFirestore firestore;

    List<String> list = new ArrayList<>();




    int flag = 0;

    String recognizedText;
    private static final int REQUEST_CAMERA_CODE = 100;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        button_capture = findViewById(R.id.button_capture);
        button_camera = findViewById(R.id.button_camera);
        //textview_data = findViewById(R.id.text_data);


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


            if (flag == 1) {
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
                        public void onSuccess(Text text) {
                             recognizedText = text.getText();
                            //textview_data.setText(recognizedText);
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
                                    Toast.makeText(AddActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    System.out.println("Failed");
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void processData(String recognizedText) {
        // Split the string by spaces to get individual words

        if(recognizedText.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "No Ingredients found, please try again", Toast.LENGTH_LONG).show();

        }
        String preprocessedText = recognizedText.replaceAll("[\\[\\]()0-9%]", "");

        int ingredientsIndex = preprocessedText.toLowerCase().indexOf("ingredients");

        if (ingredientsIndex != -1) {
            // If "Ingredients" is found, extract the substring after it
            String ingredientsString = preprocessedText.substring(ingredientsIndex + "Ingredients".length()).trim();
            // Use an ArrayList to store the words
            String[] words = ingredientsString.split(",");

            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].trim();
            }

            ArrayList<String> ingredientList = new ArrayList<>();
            List<String> foundIngredients = new ArrayList<>();


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
            retrieveData(ingredientList);


        }


    }

    private void retrieveData(ArrayList<String> ingredientList) {
        firestore = FirebaseFirestore.getInstance();
        String collectionPath = "Ingredients";

        firestore.collection(collectionPath).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Ingredient> ingredientsList = new ArrayList<>();
                for (int i = 0; i < ingredientList.size(); i++) {
                    String ingredientNameToRetrieve = ingredientList.get(i);

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Get the data from each document
                        String ingredientName = documentSnapshot.getString("Name");
                        String description = documentSnapshot.getString("Description");
                        if (ingredientNameToRetrieve.equals(ingredientName))
                        {
                            Ingredient ingredient = new Ingredient(ingredientName, description);
                            ingredientsList.add(ingredient);
                            //ingredientsList.add(ingredientName + ": " + description);
                        }
                    }
                }



                // Now the ingredientsList is populated with data, you can use it here or pass it to another method for further processing
                list = ingredientList;
                System.out.println("Ingredients with their descriptions");
                for (int i = 0; i < ingredientsList.size(); i++) {
                    System.out.println(ingredientsList.get(i));
                }

                if(!ingredientList.isEmpty()) {
                    Intent intent = new Intent(AddActivity.this, NoteActivity.class);
                    intent.putExtra("INGREDIENTS_LIST", (Serializable) ingredientsList);
                    intent.putExtra("RECOGNIZED_TEXT", recognizedText);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Ingredients found, please try again", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
                Toast.makeText(getApplicationContext(), "No Ingredients found, please try again", Toast.LENGTH_LONG).show();

            }
        });
    }
}


