package com.unieatsdev.unieats;

import java.io.Serializable;

/**
 * Created by dhruvtekchandani on 2/6/18.
 */

public class RestaurantInformation implements Serializable {

    String restName;
    String restLocation;
    String restImageAndroid;



    String restImageName;
    String restTimingsOffSession;
    String restTimingsOnSession;
    String restSaturdayTimings;
    String restSundayTimings;
    String restAvgPrice;
    String paymentMethod;
    String foodAvailable;
    String phoneNumber;

    public RestaurantInformation(){}


    public RestaurantInformation(String restName, String restLocation, String restImageAndroid, String restTimingsOffSession, String restTimingsOnSession,
                                 String restSaturdayTimings, String restSundayTimings, String restAvgPrice, String paymentMethod, String foodAvailable,
                                 String phoneNumber) {
        this.restName = restName;
        this.restLocation = restLocation;
        this.restImageAndroid = restImageAndroid;
        this.restTimingsOffSession = restTimingsOffSession;
        this.restTimingsOnSession = restTimingsOnSession;
        this.restSaturdayTimings = restSaturdayTimings;
        this.restSundayTimings = restSundayTimings;
        this.restAvgPrice = restAvgPrice;
        this.paymentMethod = paymentMethod;
        this.foodAvailable = foodAvailable;
        this.phoneNumber = phoneNumber;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public void setRestLocation(String restLocation) {
        this.restLocation = restLocation;
    }

    public void setRestImageAndroid(String restImageAndroid) {
        this.restImageAndroid = restImageAndroid;
    }

    public void setRestTimingsOffSession(String restTimingsOffSession) {
        this.restTimingsOffSession = restTimingsOffSession;
    }

    public void setRestTimingsOnSession(String restTimingsOnSession) {
        this.restTimingsOnSession = restTimingsOnSession;
    }

    public void setRestSaturdayTimings(String restSaturdayTimings) {
        this.restSaturdayTimings = restSaturdayTimings;
    }

    public void setRestSundayTimings(String restSundayTimings) {
        this.restSundayTimings = restSundayTimings;
    }

    public void setRestAvgPrice(String restAvgPrice) {
        this.restAvgPrice = restAvgPrice;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setFoodAvailable(String foodAvailable) {
        this.foodAvailable = foodAvailable;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRestName() {
        return restName;
    }

    public String getRestLocation() {
        return restLocation;
    }

    public String getRestImageAndroid() {
        return restImageAndroid;
    }

    public String getRestImageName() {
        return restImageName;
    }

    public void setRestImageName(String restImageName) {
        this.restImageName = restImageName;
    }

    public String getRestTimingsOffSession() {
        return restTimingsOffSession;
    }

    public String getRestTimingsOnSession() {
        return restTimingsOnSession;
    }

    public String getRestSaturdayTimings() {
        return restSaturdayTimings;
    }

    public String getRestSundayTimings() {
        return restSundayTimings;
    }

    public String getRestAvgPrice() {
        return restAvgPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getFoodAvailable() {
        return foodAvailable;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
