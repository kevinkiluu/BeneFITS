package com.example.ddk.benefits;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.arsy.maps_library.MapRipple;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener{

    LocationManager locationManager;

    SupportMapFragment mapFrag;
    GoogleMap mMap;
    MarkerOptions currentLocation;
    double latitude;
    double longitude;
    LatLng curloc;

    int PROXIMITY_RADIUS = 5093;

    public MapRipple mapRipple;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, null);
        if(this.mapFrag == null){
            mapFrag = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        }

        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        Button gym_button = (Button) v.findViewById(R.id.B_gyms);
        Button parks_button = (Button) v.findViewById(R.id.B_parks);
        Button trails_button = (Button) v.findViewById(R.id.B_trails);

        gym_button.setOnClickListener(buttonClick);
        parks_button.setOnClickListener(buttonClick);
        trails_button.setOnClickListener(buttonClick);

        mapFrag.getMapAsync(this);
        return v;
    }

    View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            Object dataTransfer[] = new Object[2];
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();


            switch (v.getId()) {
                case R.id.B_gyms:
                    mMap.clear();
                    String url = getUrl(latitude, longitude, "gym");
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(getActivity(), "Showing Nearby Gyms", Toast.LENGTH_SHORT).show();

                    mMap.addMarker(currentLocation);
                    break;


                case R.id.B_parks:
                    mMap.clear();
                    url = getUrl(latitude, longitude, "park");
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(getActivity(), "Showing Nearby Parks", Toast.LENGTH_SHORT).show();

                    mMap.addMarker(currentLocation);
                    break;

                case R.id.B_trails:
                    mMap.clear();
                    url = getUrl(latitude, longitude, "trail");
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(getActivity(), "Showing Nearby Trails", Toast.LENGTH_SHORT).show();

                    mMap.addMarker(currentLocation);
                    break;

            }

        }
    };

    void getLocation() {
        try {
            locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in current Location
        mapRipple = new MapRipple(mMap, new LatLng(33.6404952, -117.8442962), getContext());

        mapRipple.withNumberOfRipples(3);
        mapRipple.withFillColor(android.graphics.Color.argb(80, 255, 100, 100));
        mapRipple.withStrokeColor(Color.RED);
        mapRipple.withStrokewidth(2);
        mapRipple.withDistance(200);
        mapRipple.withRippleDuration(4000);
        mapRipple.withDurationBetweenTwoRipples(7000);

        getLocation();
        curloc = new LatLng(latitude, longitude);
        currentLocation = new MarkerOptions()
                                        .position(curloc)
                                        .title("Current Location");
        currentLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.cur_loc));
        mapRipple.withLatLng(curloc);
        mapRipple.startRippleMapAnimation();
        //mMap.setOnMarkerClickListener(this);

        mMap.addMarker(currentLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curloc));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

    }


    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        if(nearbyPlace.equals("trail"))
            googlePlaceUrl.append("name=trail");
        else
            googlePlaceUrl.append("type="+nearbyPlace);
        googlePlaceUrl.append("&location="+latitude+","+longitude);
        googlePlaceUrl.append("&rankby=distance");

        googlePlaceUrl.append("&key="+"AIzaSyDISwGhSNSNWTcTqrOB6SrRLi10rISiSwo");


        return googlePlaceUrl.toString();
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mMap.clear();
        curloc = new LatLng(latitude, longitude);
        currentLocation.position(curloc);
        mMap.addMarker(currentLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curloc));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        mapRipple.withLatLng(curloc);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Must Enable GPS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }





}
