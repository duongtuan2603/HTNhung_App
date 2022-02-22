package com.example.htnhung_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {
    public MapViewModel() {

    }

    private MutableLiveData<Boolean> buttonMoreState = new MutableLiveData<>();

    public LiveData<Boolean> getButtonMoreState() {

        if (buttonMoreState == null) {
            return new MutableLiveData<>();
        } else return buttonMoreState;
    }
    public void setButtonMoreState(boolean state){
        buttonMoreState.setValue(state);
    }
}
