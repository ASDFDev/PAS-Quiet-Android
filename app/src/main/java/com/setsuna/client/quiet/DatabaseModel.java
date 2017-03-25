package com.setsuna.client.quiet;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel on 16/1/2017.
 */

public class DatabaseModel {

    @SerializedName("device_id")
    private String mDevice_id;

    @SerializedName("username")
    private String mUsername;

    @SerializedName("attendance_code")
     private String mAttendance_code;

    String getUsername(){
        return mUsername;
    }

    String getDevice_id(){
        return mDevice_id;
    }

    String getAttendance_code(){
        return mAttendance_code;
    }

    void setDatabase(String device_id, String username, String attendance_code){
        this.mDevice_id = device_id;
        this.mUsername = username;
        this.mAttendance_code = attendance_code;
    }

}
