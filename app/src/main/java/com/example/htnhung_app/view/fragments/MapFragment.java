package com.example.htnhung_app.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.htnhung_app.R;
import com.example.htnhung_app.databinding.FragmentMapBinding;
import com.example.htnhung_app.model.CarPark;
import com.example.htnhung_app.model.CarParkAdapter;
import com.example.htnhung_app.model.DistanceResponse;
import com.example.htnhung_app.model.DistanceService;
import com.example.htnhung_app.model.LocationResponse;
import com.example.htnhung_app.model.ShareViewModel;
import com.example.htnhung_app.view.BaseFragment;
import com.example.htnhung_app.viewmodel.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends BaseFragment<FragmentMapBinding> implements OnMapReadyCallback {
    MapViewModel viewModel;
    ShareViewModel shareViewModel;
    GoogleMap map;
    LocationManager mLocationManager;
    Location currentLocation;
    LatLng selectedLocation;

    private DistanceService distanceService;
    private DistanceService locationService;

    String queryString = "";


    private List<CarPark> carParks = new ArrayList<>();
    private CarParkAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_map;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initData() {
        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        shareViewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        distanceService = new DistanceService("https://trueway-matrix.p.rapidapi.com/");
        locationService = new DistanceService("http://192.168.2.103:6060/");


        carParks.add(new CarPark("TH1", 21.009746, 105.823494, "Thái Hà", 100));
        carParks.add(new CarPark("TC2", 20.998585, 105.840124, "Trường Chinh", 5));
        adapter = new CarParkAdapter(new CarParkAdapter.ICarPark() {
            @Override
            public void onClickItem(double lat, double lon) {
                shareViewModel.setSelectedLocation(new LatLng(lat, lon));

            }
        });


        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            shareViewModel.setUserLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void initView() {
        viewBinding.fabDirection.setVisibility(View.GONE);
        viewBinding.rcvCarParks.setAdapter(adapter);
        viewBinding.rcvCarParks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
                navigate(R.id.action_mapFragment_to_carParksFragment);
            }
        });
        viewBinding.fabDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,

                        Uri.parse("http://maps.google.com/maps?saddr=" +
                                currentLocation.getLatitude() + "," + currentLocation.getLongitude() +
                                "&daddr=" + selectedLocation.latitude + "," + selectedLocation.longitude));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initObserve() {
        viewModel.getButtonMoreState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {


                viewBinding.fabMore.setImageResource(aBoolean ? R.drawable.ic_expanded : R.drawable.ic_not_expanded);
                viewBinding.bgBlur.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
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
        shareViewModel.getSelectedLocation().observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                if (latLng != null) {
                    viewBinding.rcvCarParks.setVisibility(View.GONE);
                    viewBinding.fabDirection.setVisibility(View.VISIBLE);
                    selectedLocation = latLng;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            map.addMarker(new MarkerOptions()
                                    .position(latLng
                                    ));
                            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                    }, 1000);

                } else {
                    viewBinding.rcvCarParks.setVisibility(View.VISIBLE);
                    viewBinding.fabDirection.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
        currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        shareViewModel.setUserLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        map.setMyLocationEnabled(true);
        map.setMinZoomPreference(15);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));


        LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        locationService.loicationAPI.getLocations().enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        carParks = response.body().getData();
                        for (int i = 0; i < carParks.size(); i++) {
                            CarPark carPark = carParks.get(i);
                            if (i != carParks.size() - 1) {
                                queryString = queryString.concat(carPark.getLat() + "," + carPark.getLon() + ";");
                            } else {
                                queryString = queryString.concat(carPark.getLat() + "," + carPark.getLon());
                            }

                        }
                        callAPIDistance(queryString, location);
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.d("onResponse", "response: " + t.getMessage());

            }
        });
    }

    private void callAPIDistance(String queryString, LatLng location) {
        distanceService.distanceAPI.getDistances("trueway-matrix.p.rapidapi.com", "d9394aba86msh5d9752ebcfcb740p10782ajsnad6dfcd9b9c6", queryString, location.latitude + "," + location.longitude).enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < carParks.size(); i++) {
                        CarPark carPark = carParks.get(i);
                        assert response.body() != null;
                        Log.d("onResponse", "response: " + response.body().getDistances().toString());
                        if (response.body().getDistances().size() > 0 && response.body().getDurations().size() > 0) {
                            List<Double> dumpDistances = (List<Double>) response.body().getDistances().get(i);
                            List<Double> dumpDurations = (List<Double>) response.body().getDurations().get(i);
                            carPark.setDistance(dumpDistances.get(0));
                            carPark.setDuration(

                                    dumpDurations.get(0));
                            carParks.set(i, carPark);
                        }
                        Log.d("onResponse", "carparks: " + carParks.toString());
                        adapter.setCarParks(carParks);
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {
                Log.d("onCallFailure", "onResponse: " + t.getMessage());
            }
        });
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    shareViewModel.setUserLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                } else {
                }
            });
}
