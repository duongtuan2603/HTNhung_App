package com.example.htnhung_app.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public abstract class BaseFragment <VB extends ViewDataBinding> extends Fragment {
    protected VB viewBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater,getLayoutRes(),container,false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initObserve();
    }

    protected abstract int getLayoutRes();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initObserve();

    public void navigate(int actionId) {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(actionId);
    }

    public void backToSpecificFragment(int fragmentId) {
        NavController navController = NavHostFragment.findNavController(this);
        navController.popBackStack(fragmentId, false);
    }

    public void backToPrevious() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.popBackStack();
    }
}
