package com.example.dheeraj.location_tracking;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.dheeraj.location_tracking.model.LocationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class PathMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int DEFAULT_ZOOM = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_maps);
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
    private class ParserTask extends AsyncTask<String, Integer, List<LocationModel>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<LocationModel> doInBackground(String... jsonData) {

            ArrayList<LocationModel> modelArrayList = new ArrayList<>();

            final String url = "http://35.154.229.180:8090/user-registration/location/getLocationForUserForTimeRange" +
                    "?userId=1&starttime=2017-09-15%2018:30:00&endtime=2017-09-15%2021:30:00";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                Object[] locationModelList = restTemplate.getForObject(url,
                        Object[].class);

//            if (locationModelList == null || locationModelList.length == 0) {
//                return new ArrayList<>();
//            }
            } catch(Exception e){
                System.out.println(e);
            }
            return null;//Arrays.asList(locationModelList);
        }

        @SuppressWarnings("MissingPermission")
        @Override
        protected void onPostExecute(List<LocationModel> locationModelList) {
            if (CollectionUtils.isEmpty(locationModelList)) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            ArrayList points;
            PolylineOptions lineOptions;

            points = new ArrayList();
            lineOptions = new PolylineOptions();
            for (LocationModel locationModel : locationModelList) {


                LatLng currLoc = new LatLng(Double.parseDouble(locationModel.getLatitude()), Double.parseDouble(locationModel.getLongitude()));

                points.add(currLoc);


            }

            lineOptions.addAll(points);
            lineOptions.width(12);
            lineOptions.color(Color.RED);
            lineOptions.geodesic(true);
// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
//            // Add a marker in Sydney and move the camera
//            LatLng currLoc = new LatLng(Double.parseDouble(locationModel.getLatitude()), Double.parseDouble(locationModel.getLongitude()));
//            mMap.addMarker(new MarkerOptions().position(currLoc).title("Current Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, DEFAULT_ZOOM));
        }
    }
}
