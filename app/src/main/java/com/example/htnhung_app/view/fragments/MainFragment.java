package com.example.htnhung_app.view.fragments;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.htnhung_app.R;
import com.example.htnhung_app.databinding.FragmentMainBinding;
import com.example.htnhung_app.view.BaseFragment;
import com.example.htnhung_app.view.adapter.MainPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends BaseFragment<FragmentMainBinding> {
    private MainPagerAdapter pagerAdapter;
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData() {
        pagerAdapter = new MainPagerAdapter(getChildFragmentManager());

    }

    @Override
    protected void initView() {
        viewBinding.viewPager2.setAdapter(pagerAdapter);

        viewBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_notification:
                        viewBinding.viewPager2.setCurrentItem(1);
                        return true;
                    case R.id.navigation_setting:
                        viewBinding.viewPager2.setCurrentItem(2);
                        return true;
                    default:
                        viewBinding.viewPager2.setCurrentItem(0);
                        return true;
                }
            }
        });
    }

    @Override
    protected void initObserve() {

    }
}
