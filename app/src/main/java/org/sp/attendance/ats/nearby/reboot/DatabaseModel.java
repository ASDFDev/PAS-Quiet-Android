package org.sp.attendance.ats.nearby.reboot;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel on 16/1/2017.
 */

public class DatabaseModel {

    @SerializedName("username")
    private String mUsername;

    @SerializedName("device_id")
    private String mDevice_id;

    @SerializedName("attendance_code")
     private String mAttendance_code;

    void setUsername(String username){
        this.mUsername = username;
    }

    void setDevice_id(String device_id){
        this.mDevice_id = device_id;
    }

    void setAttendance_code(String attendance_code){
        this.mAttendance_code = attendance_code;
    }

    String getUsername(){
        return mUsername;
    }

    String getDevice_id(){
        return mDevice_id;
    }

    String getAttendance_code(){
        return mAttendance_code;
    }


    void setDatabase(String username, String device_id, String attendance_code){
        this.mUsername = username;
        this.mDevice_id = device_id;
        this.mAttendance_code = attendance_code;
    }

}
