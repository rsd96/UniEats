package com.unieatsdev.unieats;

/**
 * Created by dhruvtekchandani on 2/9/18.
 */

public class RestaurantYearlyTimings {

    String startMonth;
    String startDate;
    String endMonth;
    String endDate;

    public RestaurantYearlyTimings(){}

    public RestaurantYearlyTimings(String startMonth, String startDate, String endDate, String endMonth){
        this.startMonth = startMonth;
        this.startDate = startDate;
        this.endDate = endDate;
        this.endMonth = endMonth;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
