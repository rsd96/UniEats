package com.unieatsdev.unieats;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.RelativeLayout;
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

import me.relex.circleindicator.CircleIndicator;

public class RestaurantDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RestaurantDetailActiv";

    Toolbar myToolbar;
    TextView tvRestName;
    FloatingActionButton fabCall;
    TextView tvRestCurrentTime;
    View btnGetDir;
    TextView tvLocation;
    TextView tvPayment;
    Button btnZoom;
    RelativeLayout rlMenuPager;
    CircleIndicator indicator;
    ViewPager viewPager;
    TextView tvOpeningHours;

    // Dialog textviews
    TextView saturdayText;
    TextView sundayText;
    TextView mondayText;
    TextView tuesdayText;
    TextView wednesdayText;
    TextView thursdayText;
    TextView fridayText;
    ArrayList<TextView> allTimeTextViews = new ArrayList<>();

    RestLocation restLocation;

    Bundle bundle;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    String onSessStart, onSessEnd;
    boolean isOn = false;

    boolean isAllTime = false; // check to see if time cardview is expanded or not

    RestaurantInformation restInfo = new RestaurantInformation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_detail);
         bundle = getIntent().getExtras();

         restInfo = (RestaurantInformation) bundle.getSerializable("RestaurantInfo");

        dbRef = FirebaseDatabase.getInstance().getReference();
        restLocation = new RestLocation();

        getViews();

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        // Get on time and check if current time is on or off session
        dbRef.child("OnTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x: dataSnapshot.getChildren()) {

                    onSessStart = x.child("startDate").getValue().toString();
                    onSessEnd = x.child("endDate").getValue().toString();

                    Log.d(TAG, " " + onSessStart + " : " + onSessEnd);

                    if (compareTime(onSessStart, onSessEnd)) {
                        isOn = true;
                        break;
                    }
                }

                displayTimings();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //TODO class already has info no need to call firebase
        // Check if phone number exists, if yes display phone icon and implements call functionality
        dbRef.child("RestaurantInformation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    if (x.child("restName").getValue().toString().compareTo(restInfo.getRestName()) == 0) {
                        final String phoneNum = x.child("phoneNumber").getValue().toString();
                        if (phoneNum.compareTo("0") == 0) {
                            fabCall.setVisibility(View.GONE);
                        } else {
                            fabCall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantDetailActivity.this);
                                    builder.setTitle("Call " + restInfo.getRestName());
                                    builder.setMessage("Are you sure you would like to call " + restInfo.getRestName() + " ?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                                    android.Manifest.permission.CALL_PHONE)
                                                    != PackageManager.PERMISSION_GRANTED) {

                                                ActivityCompat.requestPermissions(RestaurantDetailActivity.this,
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


        // Get menu data
        final ArrayList<String> menuImageList = new ArrayList<String>();

        dbRef.child("RestaurantMenu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    if (x.getKey().toString().compareTo(restInfo.getRestName()) == 0) {
                        for (DataSnapshot menu : x.getChildren()) {
                            menuImageList.add(menu.getValue().toString());
                        }
                        break;
                    }
                }

                if (menuImageList.isEmpty() == false) {

                    ImageAdapter adapter = new ImageAdapter(RestaurantDetailActivity.this, menuImageList);
                    viewPager.setAdapter(adapter);
                    indicator.setViewPager(viewPager);
                    adapter.registerDataSetObserver(indicator.getDataSetObserver());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tvRestName.setText(restInfo.getRestName());
        tvPayment.setText(restInfo.getPaymentMethod());
        tvLocation.setText(restInfo.getRestLocation());

        btnGetDir.setOnClickListener(this);
        tvOpeningHours.setOnClickListener(this);
        tvRestCurrentTime.setOnClickListener(this);

    }


    boolean compareTime(String startDate, String endDate) {

        try {
            if (new SimpleDateFormat("d/MMMM").parse(startDate)
                    .before(new SimpleDateFormat("d/MMMM")
                            .parse(new SimpleDateFormat("d/MMMM").format(Calendar.getInstance().getTime())))

                    &&

                    new SimpleDateFormat("d/MMMM").parse(endDate)
                    .after(new SimpleDateFormat("d/MMMM")
                            .parse(new SimpleDateFormat("d/MMMM").format(Calendar.getInstance().getTime())))){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    // Set current time
    public void displayTimings(){
        if (isOn) {
            Log.d(TAG, "ON SESSION !!!");
            tvRestCurrentTime.setText(restInfo.getRestTimingsOnSession());
        } else {
            Log.d(TAG, "OFF SESSION !!!");
            tvRestCurrentTime.setText(restInfo.getRestTimingsOffSession());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Get Restaurant location
        dbRef.child("RestaurantLocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot locationSnapshots : dataSnapshot.getChildren()) {
                    if (restInfo.getRestName().equals(locationSnapshots.getValue(RestLocation.class).getTitle())) {
                        restLocation = locationSnapshots.getValue(RestLocation.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }




    public void getViews() {
        indicator = findViewById(R.id.indicator);
        viewPager = findViewById(R.id.viewPager);
        myToolbar = findViewById(R.id.toolbarRestDetail);
        tvRestName = findViewById(R.id.tvRestDetailName);
        fabCall = findViewById(R.id.fabCall);
        tvRestCurrentTime = findViewById(R.id.tvDetailTimeCurrent);
        btnGetDir = findViewById(R.id.btnGetDirection);
        tvLocation = findViewById(R.id.tvDetailLocation);
        tvPayment = findViewById(R.id.tvDetailPaymentMethod);
        btnZoom = findViewById(R.id.btnZoom);
        rlMenuPager = findViewById(R.id.rlMenuPager);
        tvOpeningHours = findViewById(R.id.tvDetailTimeTitle);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnGetDirection:
                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=&daddr=" + restLocation.getLatitude() + "," + restLocation.getLongitude() + "&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;

            case R.id.tvDetailTimeCurrent:

            case R.id.tvDetailTimeTitle:

                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.popupwindow);
                saturdayText = dialog.findViewById(R.id.saturdayText);
                sundayText = dialog.findViewById(R.id.sundayText);
                mondayText = dialog.findViewById(R.id.mondayText);
                tuesdayText = dialog.findViewById(R.id.tuesdayText);
                wednesdayText = dialog.findViewById(R.id.wednesdayText);
                thursdayText = dialog.findViewById(R.id.thursdayText);
                fridayText = dialog.findViewById(R.id.fridayText);
                allTimeTextViews.add(new TextView((this)));
                allTimeTextViews.add(sundayText);
                allTimeTextViews.add(mondayText);
                allTimeTextViews.add(tuesdayText);
                allTimeTextViews.add(wednesdayText);
                allTimeTextViews.add(thursdayText);
                allTimeTextViews.add(fridayText);
                allTimeTextViews.add(saturdayText);

                if (isOn)
                    setAllTime(restInfo.restTimingsOnSession);
                else
                    setAllTime(restInfo.restTimingsOffSession);

                dialog.show();
                break;

        }
    }

    void setAllTime(String time) {
        allTimeTextViews.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).setTextColor(Color.RED);
        for (TextView x : allTimeTextViews) {
            x.setText(x.getText() + " : " + time);
        }
    }
}
