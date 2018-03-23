package com.unieatsdev.unieats;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
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


/**
 * Created by dhruvtekchandani on 2/9/18.
 */

public class Pop extends Activity{

    DatabaseReference restaurantAutumn;
    DatabaseReference restaurantSpring;
    DatabaseReference restaurantSummer;
    DatabaseReference restaurantWinter;
    DatabaseReference databaseRestaurantInformation;

    RestaurantYearlyTimings autumnList = new RestaurantYearlyTimings();
    RestaurantYearlyTimings springList = new RestaurantYearlyTimings();
    RestaurantYearlyTimings summerList = new RestaurantYearlyTimings();
    RestaurantYearlyTimings winterList = new RestaurantYearlyTimings();
    List<RestaurantInformation> restaurantTimings;

    Boolean summerCheack = true;
    Boolean winterCheck = false;
    Boolean sessionAutum = false;
    Boolean sessionSpring = false;

    String restaurantChosen = "";
    TextView saturdayText;
    TextView sundayText;
    TextView mondayText;
    TextView tuesdayText;
    TextView wednesdayText;
    TextView thursdayText;
    TextView fridayText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);


        Bundle bundle = getIntent().getExtras();
        restaurantChosen = bundle.getString("RestaurantName");
        saturdayText = (TextView) findViewById(R.id.saturdayText);
        sundayText = (TextView) findViewById(R.id.sundayText);
        mondayText = (TextView) findViewById(R.id.mondayText);
        tuesdayText = (TextView) findViewById(R.id.tuesdayText);
        wednesdayText = (TextView) findViewById(R.id.wednesdayText);
        thursdayText = (TextView) findViewById(R.id.thursdayText);
        fridayText = (TextView) findViewById(R.id.fridayText);


        databaseRestaurantInformation = FirebaseDatabase.getInstance().getReference("RestaurantInformation");
        restaurantAutumn = FirebaseDatabase.getInstance().getReference("RestaurantAutum");
        restaurantSpring = FirebaseDatabase.getInstance().getReference("RestaurantSpring");
        restaurantSummer = FirebaseDatabase.getInstance().getReference("RestaurantSummer");
        restaurantWinter = FirebaseDatabase.getInstance().getReference("RestaurantWinter");

        restaurantTimings = new ArrayList<>();


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams xmp = getWindow().getAttributes();
        xmp.x = 0;
        xmp.y = -180;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //getWindow().setLayout((int)(width*0.6),(int) (height*0.6));
        getWindow().setGravity(Gravity.CENTER_VERTICAL);
        highlightToRed();
    }


    @Override
    protected void onStart() {
        super.onStart();
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
        databaseRestaurantInformation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                restaurantTimings.clear();
                for(DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()) {
                    RestaurantInformation restaurantInformation = restaurantSnapshot.getValue(RestaurantInformation.class);
                    restaurantTimings.add(restaurantInformation);
                }
                changeTimings();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        for(int i =0; i<restaurantTimings.size() ;i++ ){
            final RestaurantInformation restaurantInformation = restaurantTimings.get(i);

            if (restaurantChosen.equals(restaurantInformation.getRestName().toString())) {
                if (summerCheack == true || winterCheck == true) {
                    setTextToOffSession(restaurantInformation);
                } else {
                    setTextToOnSession(restaurantInformation);
                }
            }
        }
    }


    public void highlightToRed(){
        TextView saturdayText = (TextView) findViewById(R.id.saturdayText);
        TextView sundayText = (TextView) findViewById(R.id.sundayText);
        TextView mondayText = (TextView) findViewById(R.id.mondayText);
        TextView tuesdayText = (TextView) findViewById(R.id.tuesdayText);
        TextView wednesdayText = (TextView) findViewById(R.id.wednesdayText);
        TextView thursdayText = (TextView) findViewById(R.id.thursdayText);
        TextView fridayText = (TextView) findViewById(R.id.fridayText);
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch(day){

            case 1:
                sundayText.setTextColor(Color.RED);
                break;

            case 2:
                mondayText.setTextColor(Color.RED);
                break;

            case 3:
                tuesdayText.setTextColor(Color.RED);
                break;

            case 4:
                wednesdayText.setTextColor(Color.RED);
                break;

            case 5:
                thursdayText.setTextColor(Color.RED);
                break;

            case 6:
                fridayText.setTextColor(Color.RED);
                break;

            case 7:
                saturdayText.setTextColor(Color.RED);
                break;
        }

    }

    void setTextToOnSession(RestaurantInformation restaurantInformation) {
        mondayText.setText("Mon: " + restaurantInformation.restTimingsOnSession);
        tuesdayText.setText("Tue:  " + restaurantInformation.restTimingsOnSession);
        wednesdayText.setText("Wed: " + restaurantInformation.restTimingsOnSession);
        thursdayText.setText("Thu: " + restaurantInformation.restTimingsOnSession);
        fridayText.setText("Fri:  " + restaurantInformation.restTimingsOnSession);
        saturdayText.setText("Sat: " + restaurantInformation.restSaturdayTimings);
        sundayText.setText("Sun: " + restaurantInformation.restSundayTimings);
    }

    void setTextToOffSession(RestaurantInformation restaurantInformation) {
        mondayText.setText("Mon: "+restaurantInformation.restTimingsOffSession);
        tuesdayText.setText("Tue:  "+restaurantInformation.restTimingsOffSession);
        wednesdayText.setText("Wed: "+restaurantInformation.restTimingsOffSession);
        thursdayText.setText("Thu: "+restaurantInformation.restTimingsOffSession);
        fridayText.setText("Fri:  "+restaurantInformation.restTimingsOffSession);
        saturdayText.setText("Sat: "+restaurantInformation.restSaturdayTimings);
        sundayText.setText("Sun: "+restaurantInformation.restSundayTimings);
    }
}
