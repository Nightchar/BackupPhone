package com.droid.backupphone.model.contact;

/**
 * Created by nikhil1804 on 24-04-2017.
 */

public class PhoneDetail {

    private int mPhoneType;

    private String mPhoneNumber;

    public PhoneDetail() {

    }

    public PhoneDetail(int phoneType, String phoneNumber) {
        this.mPhoneType = phoneType;
        this.mPhoneNumber = phoneNumber;
    }

    public int getPhoneType() {
        return mPhoneType;
    }

    public void setPhoneType(int phoneType) {
        this.mPhoneType = phoneType;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }
}
