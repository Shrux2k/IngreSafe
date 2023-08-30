package com.example.ingredientparser;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import androidx.exifinterface.media.ExifInterface;


public class AddActivity extends AppCompatActivity implements ImageAnalysis.Analyzer, View.OnClickListener {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    PreviewView previewView;

    Uri savedImageUri;
    Bitmap bitmap;

    Uri imageUri;
    private ImageCapture imageCapture;
    Button button_capture;

    Button button_camera;

    FirebaseFirestore firestore;

    List<String> list = new ArrayList<>();
    List<String> unhealthyIng = new ArrayList<>();



    int counter;




    int flag = 0;

    String recognizedText;
    private static final int REQUEST_CAMERA_CODE = 100;

    BottomNavigationView bottomNavigationView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        showCustomDialog();

        preferences = getSharedPreferences("CounterPrefs", MODE_PRIVATE);
        counter = preferences.getInt("counter", 0);


        previewView = findViewById(R.id.previewView);
        button_camera = findViewById(R.id.button_camera);
        button_capture = findViewById(R.id.button_capture);

        button_camera.setOnClickListener(this);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.add);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(0, 0);                    return true;

                } else if (itemId == R.id.info) {
                    startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                    overridePendingTransition(0, 0);                    return true;
                }
                return false;


            }
        });

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddActivity.this);

            }

        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);

        TextView dialogText = dialogView.findViewById(R.id.dialogText);
        TextView dialogContent = dialogView.findViewById(R.id.dialogContent);
        Button dismissButton = dialogView.findViewById(R.id.dismissButton);

        dialogText.setText("⚠️ Always Double Check \n");
        dialogContent.setText("This is an informational tool that helps you check for ingredients, it might not be 100% accurate.\n\nALWAYS double check the packaging before consuming a product if you're allergic or intolerant.\n\nI understand that I should always double check the packaging and can't fully rely on this app.");

        dialogView.setBackgroundResource(R.drawable.rounded_dialog_background);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (flag == 1) {
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
        if(flag==1)
        {
            // Assuming you have a Bitmap named 'bitmap'
            Matrix matrix = new Matrix();
            matrix.postRotate(-270); // Negative value for counterclockwise rotation

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        }
        try {
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            TextRecognizer recognizer =
                    TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            Task<Text> result = recognizer.process(image)
                    .addOnSuccessListener(text -> {

                        recognizedText = text.getText();
                        System.out.println("Done");
                        processData(recognizedText);

                    })
                    .addOnFailureListener(
                            e -> {
                                // Task failed with an exception
                                // ...
                                Toast.makeText(AddActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_LONG).show();
                                System.out.println("Failed");
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
                if(words[i].contains("Milk"))
                {
                    words[i] = "Milk";
                } else if (words[i].contains("Wheat")) {
                    words[i] = "Wheat";
                }
                else if (words[i].contains("Humectant")) {
                    words[i] = "Glycerol";
                }
                else if (words[i].contains("Raising Agents")) {
                    words[i] = "Raising Agents";
                }
                else if (words[i].contains("Stabilisers")) {
                    words[i] = "Stabilisers";
                }
                else if (words[i].contains("Malt Vinegar")) {
                    words[i] = "Malt Vinegar";
                }
                else if (words[i].contains("Pectin")) {
                    words[i] = "Pectin";
                }
                else if (words[i].contains("Mustard Seed")) {
                    words[i] = "Mustard Seed";
                }
                else if (words[i].contains("Sunflower")) {
                    words[i] = "Sunflower Oil";
                }
                else if (words[i].contains("Garlic Powder")) {
                    words[i] = "Garlic Powder";
                }
                else if (words[i].contains("Barley")) {
                    words[i] = "Barley";
                }
                else if(words[i].contains("Egg"))
                {
                    words[i] = "Egg";
                }



                else {
                    words[i] = words[i].trim();
                }
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
                List<String> unhealthy = new ArrayList<>();
                for (int i = 0; i < ingredientList.size(); i++) {
                    String ingredientNameToRetrieve = ingredientList.get(i);

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Get the data from each document
                        String ingredientName = documentSnapshot.getString("Name");
                        String description = documentSnapshot.getString("Description");
                        String emoji = documentSnapshot.getString("Emoji");
                        String health = documentSnapshot.getString("Health");
                        if (ingredientNameToRetrieve.equals(ingredientName))
                        {
                            try {


                                if (health.equals("U")) {
                                    unhealthy.add(ingredientName);
                                }


                            }catch (Exception e)
                            {
                                Log.d(TAG, "Exception "+e);
                            }

                            Ingredient ingredient = new Ingredient(ingredientName, description,emoji);
                            ingredientsList.add(ingredient);
                        }
                    }
                }
                System.out.println("========== UNHEALTHY INGREDIENTS ==========");
                for (int i = 0; i < unhealthy.size(); i++) {
                    System.out.println(unhealthy.get(i));
                }



                // Now the ingredientsList is populated with data, you can use it here or pass it to another method for further processing
                list = ingredientList;
                unhealthyIng = unhealthy;
                System.out.println("Ingredients with their descriptions");
                for (int i = 0; i < ingredientsList.size(); i++) {
                    System.out.println(ingredientsList.get(i));
                }

                if(!ingredientList.isEmpty()) {
                    ProgressBar progressBar = findViewById(R.id.progressBarAdd);

                    progressBar.setVisibility(View.VISIBLE);

                    counter++;
                    System.out.println("Counter value is:"+ counter);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("counter", counter);
                    editor.apply();

                    SharedPreferences user = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String userEmail = user.getString("userEmail", "default@example.com");



                    setLeaderboardData(userEmail, counter);


                    Intent intent = new Intent(AddActivity.this, NoteActivity.class);
                    intent.putExtra("INGREDIENTS_LIST", (Serializable) ingredientsList);
                    intent.putExtra("RECOGNIZED_TEXT", recognizedText);
                    intent.putStringArrayListExtra("UNHEALTHY", (ArrayList<String>) unhealthyIng);
                    intent.putExtra("COUNT", counter);
                    startActivity(intent);


                    progressBar.setVisibility(View.INVISIBLE);


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

    private void setLeaderboardData(String userEmail, int counter)
    {
        try {
            CollectionReference usersCollection = db.collection("Leaderboards");
            Query query = usersCollection.whereEqualTo("Username", userEmail);

            query.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        Log.d("Firestore", "Query Success"); // Add this line

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            // Update the "Scans" field for each matching document
                            Log.d("For Loop", "went inside ");
                            DocumentReference documentRef = documentSnapshot.getReference();


                            // Create a map to update the "Scans" field
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("Scans", counter);

                            // Update the document
                            documentRef.update(updates)
                                    .addOnSuccessListener(aVoid -> {
                                        // Update successful for a specific document
                                        // You can add your code here to handle each successful update
                                        Log.d("Firestore", "Success");

                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle errors
                                        // You can add your code here to handle update failures
                                        Log.d("Firestore", "Failed");

                                    });
                        }
                        Log.d("For Loop", "Exited");

                    }).addOnFailureListener(e -> {
                        Log.d("Firestore error", "Failed");
                    });
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Firestore error", "Firestore operation failed " + e.getMessage());

        }


    }

    Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @NonNull
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        // Image analysis use case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(getExecutor(), this);

        // Bind to lifecycle
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        // image processing here for the current frame
        Log.d("TAG", "analyze: got the frame at: " + image.getImageInfo().getTimestamp());
        image.close();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_camera) {
            capturePhoto();
        }
    }

    private void capturePhoto() {
        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        savedImageUri = outputFileResults.getSavedUri();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), savedImageUri);
                            flag = 1;
                            getTextFromImage(bitmap);


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                    }


                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(AddActivity.this, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}

