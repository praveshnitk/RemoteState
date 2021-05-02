package com.ratnasagar.remotestate.model;

import java.io.Serializable;

public class TrackingData implements Serializable {
    private String latitude;
    private String longitude;
    private String TrackingTime;
    private String Address;


    public TrackingData(String latitude, String longitude, String TrackingTime, String Address){
        this.latitude=latitude;
        this.longitude=longitude;
        this.TrackingTime=TrackingTime;
        this.Address=Address;
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

    public String getTrackingTime() {
        return TrackingTime;
    }

    public void setTrackingTime(String trackingTime) {
        TrackingTime = trackingTime;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
