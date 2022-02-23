package com.example.htnhung_app.model;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<LatLng> selectedLocation = new MutableLiveData<>();

    public void setSelectedLocation(LatLng location) {
        selectedLocation.setValue(location);
    }

    public LiveData<LatLng> getSelectedLocation() {
        if (selectedLocation == null) {
            return new MutableLiveData<>();
        } else return selectedLocation;
    }
}
