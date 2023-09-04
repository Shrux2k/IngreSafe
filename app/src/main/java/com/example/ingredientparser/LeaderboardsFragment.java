package com.example.ingredientparser;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardsFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboards, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null && currentUser.getEmail() != null) {
            String[] emailParts = currentUser.getEmail().split("@");
            String currentUsername = emailParts[0];
            //currentUsername = currentUsername + " (You)";

            Query query = db.collection("Leaderboards")
                    .orderBy("Scans", Query.Direction.DESCENDING);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<User> userList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        int iconDrawableId = R.drawable.baseline_person_outline_24; // Replace with your user icon
                        String username = document.getString("Username");
                        int totalScans = document.getLong("Scans").intValue();

                        User user = new User(iconDrawableId, username, totalScans);
                        userList.add(user);
                    }

                    // Pass the current username as a parameter
                    displayLeaderboard(userList, currentUsername);
                }
            });
        }

        return rootView;
    }

    private void displayLeaderboard(List<User> userList, String currentUsername) {

        LeaderboardAdapter adapter = new LeaderboardAdapter(userList, currentUsername);

        RecyclerView recyclerView = getView().findViewById(R.id.leaderboardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
}
