package com.example.dheeraj.location_tracking;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.dheeraj.location_tracking.model.LocationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class NMinsMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int DEFAULT_ZOOM = 15;


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmins_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        new ParserTask().execute();

    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, LocationModel[]> {

        // Parsing the data in non-ui thread
        @Override
        protected LocationModel[] doInBackground(String... jsonData) {

            // Get the Intent that started this activity and extract the string
            Intent intent = getIntent();
            String minute = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

            LocationModel locationModel[] = new LocationModel[0];
            final String url = "http://35.154.229.180:8090/user-registration/location/getLocationForUserForLastNMinute?userId=1&minute="+minute;

            System.out.println("url is " + url);

            RestTemplate restTemplate = new RestTemplate();

            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<LocationModel[]> responseEntity = restTemplate.getForEntity(url, LocationModel[].class);
                LocationModel[] objects = responseEntity.getBody();

                if (objects != null && objects.length > 0) {
                    locationModel = objects;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return locationModel;
        }

        @SuppressWarnings("MissingPermission")
        @Override
        protected void onPostExecute(LocationModel[] locationModelList) {
            if (locationModelList == null || locationModelList.length == 0) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            ArrayList points;
            PolylineOptions lineOptions;

            points = new ArrayList();
            lineOptions = new PolylineOptions();

            LatLng startLocation = null;
            LatLng endLocation = null;


            for (int i = 0; i < locationModelList.length; i++) {

                LocationModel locationModel = locationModelList[i];

                LatLng currLoc = new LatLng(Double.parseDouble(locationModel.getLatitude()), Double.parseDouble(locationModel.getLongitude()));
                if (i == 0) {
                    endLocation = currLoc;
                }

                if (i == locationModelList.length - 1) {
                    startLocation = currLoc;
                }

                points.add(currLoc);


            }

            lineOptions.addAll(points);
            lineOptions.width(12);
            lineOptions.color(Color.RED);
            lineOptions.geodesic(true);
            mMap.addPolyline(lineOptions);
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(startLocation).title("Start Location"));
            mMap.addMarker(new MarkerOptions().position(endLocation).title("End Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLocation, DEFAULT_ZOOM));
        }
    }
}
