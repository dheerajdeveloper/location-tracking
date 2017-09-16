package com.example.dheeraj.location_tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;

/**
 * Created by dheeraj on 14/09/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class LocationModel {

    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationtime() {
        return locationtime;
    }

    public void setLocationtime(String locationtime) {
        this.locationtime = locationtime;
    }

    long userId;

    String latitude;

    String longitude;

    String locationtime;

    public LocationModel() {
    }

    public LocationModel(long userId, String latitude, String longitude, String locationtime) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationtime = locationtime;
    }
}
