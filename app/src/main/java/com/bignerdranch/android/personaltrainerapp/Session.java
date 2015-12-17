package com.bignerdranch.android.personaltrainerapp;

import java.util.Date;
import java.util.UUID;

public class Session {

    private UUID mSessionID;
    private UUID mClientID;
    private String mTitle;
    private String mLocation;
    private Date mDate;
    private double mWeight;

    public Session() {
        this(UUID.randomUUID());
        this.mDate = new Date();
    }

    public Session(UUID id) {
        mSessionID = id;
        mDate = new Date();
    }

    public UUID getSessionID() {
        return mSessionID;
    }

    public UUID getClientID() {
        return mClientID;
    }

    public void setClientID(UUID clientID) {
        mClientID = clientID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }

    public String getPictureFilename() {
        return "IMG_" + getSessionID().toString() + ".jpg";
    }
}
