package com.example.htnhung_app.view.fragments;

import android.location.Location;
import android.location.LocationManager;
import android.text.Layout;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.htnhung_app.R;
import com.example.htnhung_app.databinding.FragmentCarParksBinding;
import com.example.htnhung_app.model.CarPark;
import com.example.htnhung_app.model.CarParkAdapter;
import com.example.htnhung_app.model.ShareViewModel;
import com.example.htnhung_app.view.BaseFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class CarParksFragment extends BaseFragment<FragmentCarParksBinding> {
    private ShareViewModel shareViewModel;
    private List<CarPark> carParks = new ArrayList<>();
    private CarParkAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_car_parks;
    }

    @Override
    protected void initData() {
        shareViewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

        carParks.add(new CarPark("TH1", 21.009746, 105.823494, "Thái Hà", 100));
        carParks.add(new CarPark("TC2", 20.998585, 105.840124, "Trường Chinh", 5));
        adapter = new CarParkAdapter(new CarParkAdapter.ICarPark() {
            @Override
            public void onClickItem(double lat, double lon) {
                shareViewModel.setSelectedLocation(new LatLng(lat, lon));
                backToPrevious();

            }
        });
        adapter.setCarParks(carParks);
        viewBinding.rcvCarParks.setAdapter(adapter);
        viewBinding.rcvCarParks.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initObserve() {

    }
}
