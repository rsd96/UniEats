package com.unieatsdev.unieats;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivityView extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    static String location = "University of Wollongong";
    DatabaseReference databaseRestaurantLocations;
    List<LocationsIcons> locationsList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_view);
        databaseRestaurantLocations = FirebaseDatabase.getInstance().getReference("RestaurantLocation");
        locationsList = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setTitle("UOW Campus");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchRestaurantsLocation();
    }

    public void fetchRestaurantsLocation(){
        databaseRestaurantLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locationsList.clear();
                Log.d("hello","www");
                for(DataSnapshot locationSnapshots: dataSnapshot.getChildren()){
                    LocationsIcons locationsIcons = locationSnapshots.getValue(LocationsIcons.class);
                    final LatLng POSITION = new LatLng(Double.valueOf(locationsIcons.getLatitude()),Double.valueOf(locationsIcons.getLongitude()));
                    Marker mPostion;
                    mPostion = mMap.addMarker(new MarkerOptions().position(POSITION).title(locationsIcons.getTitle()).snippet(location));
                    mPostion.setTag(locationsIcons.getUserData());
                    locationsList.add(locationsIcons);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return false;
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
        mMap.setMinZoomPreference(16.0f);

        Log.d("*****************","??????????????????????????");
        Log.d("llllll","value" + locationsList.size());



        // Making the center of UOW
        // this is infront of the library
        LatLng sydney = new LatLng(-34.406240, 150.878623);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("marker","yup yup");
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("touch","yup yup");
        for(int i=0;i<locationsList.size();i++){
            LocationsIcons locationsIcons = locationsList.get(i);
            if(marker.getTag() == locationsIcons.getUserData()){
                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=&daddr="+locationsIcons.getLatitude()+","+locationsIcons.getLongitude()+"&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
