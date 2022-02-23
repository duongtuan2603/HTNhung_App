package com.example.htnhung_app.view.fragments;

import android.location.Location;
import android.location.LocationManager;
import android.text.Layout;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.htnhung_app.R;
import com.example.htnhung_app.databinding.FragmentCarParksBinding;
import com.example.htnhung_app.model.CarPark;
import com.example.htnhung_app.model.CarParkAdapter;
import com.example.htnhung_app.model.DistanceResponse;
import com.example.htnhung_app.model.DistanceService;
import com.example.htnhung_app.model.ShareViewModel;
import com.example.htnhung_app.view.BaseFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarParksFragment extends BaseFragment<FragmentCarParksBinding> {
    private ShareViewModel shareViewModel;
    private List<CarPark> carParks = new ArrayList<>();
    private CarParkAdapter adapter;

    private DistanceService distanceService;



    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_car_parks;
    }

    @Override
    protected void initData() {
        shareViewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        distanceService = new DistanceService("https://trueway-matrix.p.rapidapi.com/");


        carParks.add(new CarPark("TH1", 21.009746, 105.823494, "Thái Hà", 100));
        carParks.add(new CarPark("TC2", 20.998585, 105.840124, "Trường Chinh", 5));
        adapter = new CarParkAdapter(new CarParkAdapter.ICarPark() {
            @Override
            public void onClickItem(double lat, double lon) {
                shareViewModel.setSelectedLocation(new LatLng(lat, lon));
                backToPrevious();

            }
        });






    }

    private void callAPIDistance(String queryString,LatLng location){
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

    @Override
    protected void initView() {
        viewBinding.rcvCarParks.setAdapter(adapter);
        viewBinding.rcvCarParks.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void initObserve() {
        shareViewModel.getUserLocation().observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                String queryString = "";
                LatLng location = latLng;
                if (location!=null){
                    for (int i = 0; i < carParks.size(); i++) {
                        CarPark carPark = carParks.get(i);
                        if (i != carParks.size() - 1) {
                            queryString = queryString.concat(carPark.getLat() + "," + carPark.getLon() + ";");
                        } else {
                            queryString = queryString.concat(carPark.getLat() + "," + carPark.getLon());
                        }
                    }
                    callAPIDistance(queryString,location);
                }
            }
        });
    }
}
