package com.droid.backupphone.model.contact;

import java.util.List;

/**
 * The contact model class.
 */
public class Contact {

    private String mId;
    private String mContactName;
    private List<PhoneDetail> mPhoneList;

    public Contact() {
        // do nothing
    }

    public Contact(String id, String contactName, List<PhoneDetail> phoneList) {
        this.mId = id;
        this.mContactName = contactName;
        this.mPhoneList = phoneList;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String contactName) {
        this.mContactName = contactName;
    }

    public List<PhoneDetail> getPhoneList() {
        return mPhoneList;
    }

    public void setPhoneList(List<PhoneDetail> phoneList) {
        this.mPhoneList = phoneList;
    }
}
