package com.example.ingredientparser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.List;
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




    int flag = 0;

    String recognizedText;
    private static final int REQUEST_CAMERA_CODE = 100;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

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

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddActivity.this);

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
                for (int i = 0; i < ingredientList.size(); i++) {
                    String ingredientNameToRetrieve = ingredientList.get(i);

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Get the data from each document
                        String ingredientName = documentSnapshot.getString("Name");
                        String description = documentSnapshot.getString("Description");
                        String emoji = documentSnapshot.getString("Emoji");
                        if (ingredientNameToRetrieve.equals(ingredientName))
                        {
                            Ingredient ingredient = new Ingredient(ingredientName, description,emoji);
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
                    ProgressBar progressBar = findViewById(R.id.progressBarAdd);

                    progressBar.setVisibility(View.VISIBLE);

                    Intent intent = new Intent(AddActivity.this, NoteActivity.class);
                    intent.putExtra("INGREDIENTS_LIST", (Serializable) ingredientsList);
                    intent.putExtra("RECOGNIZED_TEXT", recognizedText);
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

