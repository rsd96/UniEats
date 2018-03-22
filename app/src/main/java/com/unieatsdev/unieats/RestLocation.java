package com.unieatsdev.unieats;

/**
 * Created by dhruvtekchandani on 2/7/18.
 */

public class RestLocation {

    String title;
    String snippet;
    String latitude;
    String longitude;
    Integer userData;


    public RestLocation(){}

    public RestLocation(String title, String snippet, String latitude, String longitude, Integer userData){
        this.title = title;
        this.snippet = snippet;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userData = userData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getUserData() {
        return userData;
    }

    public void setUserData(Integer userData) {
        this.userData = userData;
    }
}
