package com.example.htnhung_app.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.htnhung_app.R;
import com.example.htnhung_app.databinding.FragmentMapBinding;
import com.example.htnhung_app.view.BaseFragment;
import com.example.htnhung_app.viewmodel.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MapFragment extends BaseFragment<FragmentMapBinding> implements OnMapReadyCallback, LocationListener {
    MapViewModel viewModel;
    GoogleMap map;
    LocationManager mLocationManager;
    int LOCATION_REFRESH_TIME = 15000; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 200; // 500 meters to update

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_map;
    }

    @Override
    protected void initData() {
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, this::onLocationChanged);
    }

    @Override
    protected void initView() {
        viewModel.setButtonMoreState(false);
        viewBinding.fabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setButtonMoreState(!viewModel.getButtonMoreState().getValue());
            }
        });
        viewBinding.fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(R.id.action_mainFragment_to_carParksFragment);
            }
        });
    }

    @Override
    protected void initObserve() {
        viewModel.getButtonMoreState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {


                viewBinding.fabMore.setImageResource(aBoolean ? R.drawable.ic_expanded : R.drawable.ic_not_expanded);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewBinding.layoutList.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                    }
                }, 0);
                new android.os.Handler().postDelayed(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             viewBinding.layoutOrder.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                                                         }
                                                     }, 100
                );
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
        Location currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        map.setMyLocationEnabled(true);
        map.setMinZoomPreference(15);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("locationChanged_", "onLocationChanged: " + location.getLatitude() + "-" + location.getLongitude());

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }
}
