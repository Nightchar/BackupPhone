package com.droid.backupphone.model.sms;

/**
 * Created by root on 18/6/17.
 */
public class Sms {

    /**
     * The constructor.
     *
     * @param Address incomming phone number
     * @param msg current message
     * @param time currentTime
     */
    private String address;
    private String msg;
    private String time;

    public Sms() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
