package com.example.ingredientparser;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomHomeActivity extends AppCompatActivity {



    private List<String> selectedDocumentNames = new ArrayList<>();
    private List<Document> documentList = new ArrayList<>();
    private List<Document> filteredList = new ArrayList<>();

    List<Document> savedDocuments = new ArrayList<>();
    List<Document> unsavedDocuments = new ArrayList<>();

    List<String> savedDocumentNames;
    private RecyclerView recyclerView;
    private DocumentAdapter adapter;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_home);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        recyclerView = findViewById(R.id.documentRecyclerView);
        SearchView searchView = findViewById(R.id.searchView);
        Button saveButton = findViewById(R.id.saveButton);

        firestore = FirebaseFirestore.getInstance();
        String collectionPath = "Ingredients";


        firestore.collection(collectionPath).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String ingredientName = documentSnapshot.getString("Name");
                        documentList.add(new Document(ingredientName));
                    }
                adapter.notifyDataSetChanged();

                // Load previously selected document names from SharedPreferences
                savedDocumentNames = getSelectedDocumentNames();


                for (String documentName : savedDocumentNames) {
                    Log.d("SavedDocument", documentName);

                }

                if(!documentList.isEmpty()) {
                    for (Document document : documentList) {
                        if (savedDocumentNames.contains(document.getName())) {
                            document.setSelected(true);
                            savedDocuments.add(document);

                        }
                        else {
                            unsavedDocuments.add(document);
                        }
                    }
                    documentList.clear();
                    documentList.addAll(savedDocuments);
                    documentList.addAll(unsavedDocuments);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("Document List Empty", "zzz");
                     }
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Couldn't connect to Firebase");
            }
        });


        // Create and set the adapter
        adapter = new DocumentAdapter(documentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // If the search query is empty, display the documentList
                    adapter.setDocumentList(documentList);
                } else {
                    // If there is a search query, display the filteredList
                    filterDocuments(newText);
                    adapter.setDocumentList(filteredList);
                }
                return true;

            }
        });

        // Handle save button click
        saveButton.setOnClickListener(v -> {
            selectedDocumentNames.clear(); // Clear the ArrayList to avoid duplicates
            for (Document document : documentList) {
                if (document.isSelected()) {
                    selectedDocumentNames.add(document.getName());
                }
            }

            Toast.makeText(this, "Preferences saved", Toast.LENGTH_SHORT).show();

            // Save selected document names to SharedPreferences
            saveSelectedDocumentNames(selectedDocumentNames);

            // Print selected document names for debugging
            for (String documentName : selectedDocumentNames) {
                Log.d("SelectedDocument", documentName);
            }

            // TODO: Perform other actions as needed.
        });
    }


    private void saveSelectedDocumentNames(List<String> selectedDocumentNames) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> selectedDocumentSet = new HashSet<>(selectedDocumentNames);
        editor.putStringSet("SelectedDocuments", selectedDocumentSet);
        editor.apply();
    }

    // Method to retrieve selected document names from SharedPreferences
    private List<String> getSelectedDocumentNames() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> selectedDocumentSet = preferences.getStringSet("SelectedDocuments", new HashSet<>());
        return new ArrayList<>(selectedDocumentSet);
    }

    private void filterDocuments(String query) {
        Log.d("SearchQuery", "Query: " + query); // Log the search query
        filteredList.clear();
        for (Document document : documentList) {
            if (document.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(document);
            }
        }
        adapter.notifyDataSetChanged();
    }
}


