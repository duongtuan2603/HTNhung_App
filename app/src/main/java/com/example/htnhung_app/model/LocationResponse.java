package com.example.htnhung_app.model;

import java.util.List;

public class LocationResponse {
    private List<CarPark> result;

    public List<CarPark> getData() {
        return result   ;
    }

    public void setData(List<CarPark> data) {
        this.result = data;
    }
}
