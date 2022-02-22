package com.example.htnhung_app.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.htnhung_app.view.fragments.MapFragment;
import com.example.htnhung_app.view.fragments.NotificationFragment;
import com.example.htnhung_app.view.fragments.SettingsFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new NotificationFragment();
            case 2:
                return new SettingsFragment();
            default:
                return new MapFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
