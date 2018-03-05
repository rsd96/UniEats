package com.unieatsdev.unieats;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class RestaurantDetailView extends AppCompatActivity {


    DatabaseReference dbRef;
    List<LocationsIcons> locationsList;
    String restaurantChosen = "";

    Boolean summerCheack = true;
    Boolean winterCheck = false;
    Boolean sessionAutum = false;
    Boolean sessionSpring = false;

    RestaurantYearlyTimings autumnList = new RestaurantYearlyTimings();
    RestaurantYearlyTimings springList = new RestaurantYearlyTimings();
    RestaurantYearlyTimings summerList = new RestaurantYearlyTimings();
    RestaurantYearlyTimings winterList = new RestaurantYearlyTimings();

    TextView restaurantTimings;
    Bundle bundle;

    DatabaseReference restaurantAutumn;
    DatabaseReference restaurantSpring;
    DatabaseReference restaurantSummer;
    DatabaseReference restaurantWinter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_restaurant_detail_view);
         bundle = getIntent().getExtras();

        dbRef = FirebaseDatabase.getInstance().getReference();
        locationsList = new ArrayList<>();

        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);

        View customAppbar = inflater.inflate(R.layout.appbar_rest_detail, null);

        actionBar.setCustomView(customAppbar);
        actionBar.setDisplayShowCustomEnabled(true);*/

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarRestDetail);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        restaurantAutumn = FirebaseDatabase.getInstance().getReference("RestaurantAutum");
        restaurantSpring = FirebaseDatabase.getInstance().getReference("RestaurantSpring");
        restaurantSummer = FirebaseDatabase.getInstance().getReference("RestaurantSummer");
        restaurantWinter = FirebaseDatabase.getInstance().getReference("RestaurantWinter");

        final TextView restaurantName = (TextView) findViewById(R.id.restaurantDetailName);
        final FloatingActionButton callButton = (FloatingActionButton) findViewById(R.id.fabCall);

        dbRef.child("RestaurantInformation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    if (x.child("restName").getValue().toString().compareTo(bundle.getString("RestaurantName")) == 0) {
                        final String phoneNum = x.child("phoneNumber").getValue().toString();
                        if (phoneNum.compareTo("0") == 0) {
                            callButton.setVisibility(View.GONE);
                        } else {
                            callButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantDetailView.this);
                                    builder.setTitle("Call " + bundle.getString("RestaurantName"));
                                    builder.setMessage("Are you sure you would like to call " + bundle.getString("RestaurantName") + " ?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                                    android.Manifest.permission.CALL_PHONE)
                                                    != PackageManager.PERMISSION_GRANTED) {

                                                ActivityCompat.requestPermissions(RestaurantDetailView.this,
                                                        new String[]{android.Manifest.permission.CALL_PHONE},
                                                        1000);
                                            } else {
                                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", null);
                                    builder.create().show();
                                }
                            });
                        }

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//
//        setSupportActionBar(toolbar);

        final ArrayList<String> menuImageList = new ArrayList<String>();

        dbRef.child("RestaurantMenu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    if (x.getKey().toString().compareTo(bundle.getString("RestaurantName")) == 0) {
                        for (DataSnapshot menu : x.getChildren()) {
                            menuImageList.add(menu.getValue().toString());
                        }
                        break;
                    }
                }

                CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                ViewPager viewPager = findViewById(R.id.viewPager);
                ImageAdapter adapter = new ImageAdapter(RestaurantDetailView.this, menuImageList);
                viewPager.setAdapter(adapter);
                indicator.setViewPager(viewPager);
                adapter.registerDataSetObserver(indicator.getDataSetObserver());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        restaurantTimings = (TextView) findViewById(R.id.restaurantTimings);
        TextView restaurantPayment = (TextView) findViewById(R.id.restaurantPaymentMethod);
        TextView restaurantLocation = (TextView) findViewById(R.id.restaurantDetailLocation);
        ImageButton restaurantIconButton = (ImageButton) findViewById(R.id.locationIconButton);
        //Button popupButton = (Button) findViewById(R.id.popupButton);


        if (bundle != null) {
            restaurantName.setText(bundle.getString("RestaurantName"));
            restaurantPayment.setText(bundle.getString("RestaurantPayment"));
            restaurantLocation.setText(bundle.getString("RestaurantLocation"));
        }
        Log.d("trial----->", restaurantName.getText().toString());
        restaurantTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = (new Intent(RestaurantDetailView.this, Pop.class));
                //Intent i = new Intent(RestaurantDetailView.this,Pop.class);
                i.putExtra("RestaurantName",restaurantName.getText().toString());
                startActivity(i);
            }
        });


        // get time of sessions
        restaurantAutumn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot timingsSnapshots : dataSnapshot.getChildren()) {
                    RestaurantYearlyTimings yearlyTimingsList = timingsSnapshots.getValue(RestaurantYearlyTimings.class);
                    autumnList = yearlyTimingsList;
                }
                changeTimings();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        restaurantSpring.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot timingsSnapshot: dataSnapshot.getChildren()){
                    RestaurantYearlyTimings yearlyTimingsList = timingsSnapshot.getValue(RestaurantYearlyTimings.class);
                    springList = yearlyTimingsList;
                    changeTimings();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        restaurantSummer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot timingsSnapshot: dataSnapshot.getChildren()){
                    RestaurantYearlyTimings yearlyTimingsList = timingsSnapshot.getValue(RestaurantYearlyTimings.class);
                    summerList = yearlyTimingsList;
                }
                changeTimings();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        restaurantWinter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot timingsSnapshot: dataSnapshot.getChildren()){
                    RestaurantYearlyTimings yearlyTimingsList = timingsSnapshot.getValue(RestaurantYearlyTimings.class);
                    winterList = yearlyTimingsList;
                }
                changeTimings();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        fetch();
    }

    public void changeTimings(){


        String startDate = autumnList.getStartDate() + "/" + autumnList.getStartMonth();
        if (compareTime(startDate)){
            sessionAutum = true;
            summerCheack = winterCheck = sessionSpring = false;
        }

        startDate = springList.getStartDate() + "/" + springList.getStartMonth();
        if(compareTime(startDate)){
            sessionSpring = true;
            sessionAutum = winterCheck = summerCheack = false;
        }


        startDate = summerList.getStartDate() + "/" + summerList.getStartMonth();
        if(compareTime(startDate)){
            summerCheack = true;
            sessionAutum = winterCheck = sessionSpring = false;
        }

        startDate = winterList.getStartDate() + "/" + winterList.getStartMonth();
        if(compareTime(startDate)){
            winterCheck = true;
            sessionAutum = sessionSpring = summerCheack = false;
        }

        displayTimings();
    }

    boolean compareTime(String startDate) {

        try {
            if (new SimpleDateFormat("d/MMMM").parse(startDate)
                    .before(new SimpleDateFormat("d/MMMM")
                            .parse(new SimpleDateFormat("d/MMMM").format(Calendar.getInstance().getTime())))){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    public void displayTimings(){

        Log.d("TIMING ", sessionAutum + " " + sessionSpring + " " + summerCheack + " " + winterCheck);
        if (summerCheack == true || winterCheck == true) {
            restaurantTimings.setText(bundle.getString("RestaurantOffTimings"));
        } else {
            restaurantTimings.setText(bundle.getString("RestaurantOnTimings"));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        dbRef.child("RestaurantLocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locationsList.clear();
                Log.d("hello", "www");
                for (DataSnapshot locationSnapshots : dataSnapshot.getChildren()) {
                    LocationsIcons locationsIcons = locationSnapshots.getValue(LocationsIcons.class);

                    locationsList.add(locationsIcons);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void fetch() {
        Bundle bundle = getIntent().getExtras();
        final TextView restaurantName = (TextView) findViewById(R.id.restaurantDetailName);
        restaurantName.setText(bundle.getString("RestaurantName"));
      //  restaurantChosen = restaurantName.getText().toString();
        View detailMapButton = findViewById(R.id.btnGetDirection);
        detailMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < locationsList.size(); i++) {
                    final LocationsIcons locationsIcons = locationsList.get(i);
                    if (restaurantName.getText().toString().equals(locationsIcons.getTitle().toString())) {
                        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=&daddr=" + locationsIcons.getLatitude() + "," + locationsIcons.getLongitude() + "&mode=w");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
