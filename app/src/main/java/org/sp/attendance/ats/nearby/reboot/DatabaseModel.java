package org.sp.attendance.ats.nearby.reboot;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel on 16/1/2017.
 */

public class DatabaseModel {

    @SerializedName("username")
    private String username;

    @SerializedName("device_id")
    private String device_id;

    @SerializedName("attendance_code")
    private String attendance_code;

    public void setUsername(String username){
        this.username = username;
    }

    public void setDevice_id(String device_id){
        this.device_id = device_id;
    }

    public void setAttendance_code(String attendance_code){
        this.attendance_code = attendance_code;
    }

    public String getUsername(){
        return username;
    }

    public String getDevice_id(){
        return device_id;
    }

    public String getAttendance_code(){
        return attendance_code;
    }

}
