package com.example.ddk.benefits;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFrag;
    GoogleMap mMap;
    MarkerOptions currentLocation;
    double latitude;
    double longitude;
    int PROXIMITY_RADIUS = 5093;

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


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in current Location
        latitude = 33.6416246;
        longitude = -117.8475188;
        LatLng curloc = new LatLng(latitude, longitude);
        currentLocation = new MarkerOptions()
                                        .position(curloc)
                                        .title("Current Location");
        currentLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.cur_loc));
        mMap.addMarker(currentLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curloc));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
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





}
