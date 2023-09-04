package com.example.ingredientparser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class BadgeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_badge, container, false);

        // Create a list of Badge objects (replace with your actual badge data)
        List<Badge> badgeList = new ArrayList<>();
        badgeList.add(new Badge("Ingredient Explorer", R.drawable.novice_foodie, 0));
        badgeList.add(new Badge("Food Detective", R.drawable.beginner_foodie, 100));
        badgeList.add(new Badge("Wellness Watcher", R.drawable.expert_foodie, 500));
        badgeList.add(new Badge("Health Maestro", R.drawable.master_foodie, 1000));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BadgeAdapter adapter = new BadgeAdapter(badgeList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
