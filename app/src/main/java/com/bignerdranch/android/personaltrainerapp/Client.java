package com.bignerdranch.android.personaltrainerapp;

import java.util.Date;
import java.util.UUID;

public class Client {

    private UUID mID;
    private String mName;
    private String mEmailAddress;
    private Date mDate;

    public Client() {
        this(UUID.randomUUID());
        this.mDate = new Date();
    }

    public Client(UUID id) {
        mID = id;
        mDate = new Date();
    }

    public UUID getID() {
        return mID;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
