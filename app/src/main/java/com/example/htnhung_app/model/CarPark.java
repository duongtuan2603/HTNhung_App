package com.example.htnhung_app.model;

import androidx.annotation.NonNull;

import java.io.Serializable;


public class CarPark implements Serializable {
    private String id;
    private Double lat;
    private Double lon;
    private String name;
    private int emptySpace;
    private double duration;
    private double distance;
    private String distanceKilometer;
    private String durationMinute;
    private DistanceClassified distanceClassified;
    private DurationClassified durationClassified;
    private SpacesClassified spacesClassified;
    private int totalClassified;

    public DistanceClassified getDistanceClassified() {
        if (distance < 2000)
            return DistanceClassified.NEAR;
        return DistanceClassified.FAR;
    }

    public DurationClassified getDurationClassified() {
        if (duration < 15 * 60) {
            return DurationClassified.SHORT;
        }
        return DurationClassified.LONG;
    }

    public SpacesClassified getSpacesClassified() {
        if (emptySpace < 10)
            return SpacesClassified.FEW;
        return SpacesClassified.MANY;
    }

    public int getTotalClassified() {
        if (getDistanceClassified() == DistanceClassified.NEAR) {
            if (getDurationClassified() == DurationClassified.SHORT) {
                if (getSpacesClassified() == SpacesClassified.MANY)
                    return 1;
                return 2;
            } else {
                if (getSpacesClassified() == SpacesClassified.MANY)
                    return 3;
                return 4;
            }
        } else {
            if (getDurationClassified() == DurationClassified.SHORT) {
                if (getSpacesClassified() == SpacesClassified.MANY)
                    return 5;
                return 6;
            } else {
                if (getSpacesClassified() == SpacesClassified.MANY)
                    return 7;
                return 8;
            }
        }
    }

    public enum DistanceClassified {
        NEAR, FAR
    }

    public enum DurationClassified {
        SHORT, LONG
    }

    public enum SpacesClassified {
        FEW, MANY
    }


    public CarPark(String id, Double lat, double lon, String name, int emptySpace) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.emptySpace = emptySpace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmptySpace() {
        return emptySpace;
    }

    public void setEmptySpace(int emptySpace) {
        this.emptySpace = emptySpace;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDistanceKilometer() {
        return distance / 1000 + " km";
    }

    public String getDurationMinute() {
        return duration / 60 + " min";
    }

    @NonNull
    @Override
    public String toString() {
        return name + "-" + distanceKilometer + "-" + durationMinute + "\n";
    }
}
