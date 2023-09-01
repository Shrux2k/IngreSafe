package com.example.ingredientparser;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ingredientparser.BadgeFragment;
import com.example.ingredientparser.LeaderboardsFragment;

public class MyPagerAdapter extends FragmentStateAdapter {

    public MyPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BadgeFragment(); // Your Badge Fragment
            case 1:
                return new LeaderboardsFragment(); // Your Leaderboards Fragment
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}
