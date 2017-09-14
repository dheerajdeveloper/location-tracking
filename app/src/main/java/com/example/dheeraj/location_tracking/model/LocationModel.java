package com.example.dheeraj.location_tracking.model;

import lombok.Data;

/**
 * Created by dheeraj on 14/09/17.
 */

@Data
public class LocationModel {

    long id;

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
