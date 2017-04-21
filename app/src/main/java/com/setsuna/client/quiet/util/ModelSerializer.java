package com.setsuna.client.quiet.util;

import com.google.gson.annotations.SerializedName;

public class ModelSerializer {

    @SerializedName("device_id")
    private String device_id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("attendance_code")
     private String attendance_code;

    String getUsername(){
        return username;
    }

    String getPassword(){
        return password;
    }

    String getDevice_id(){
        return device_id;
    }

    String getAttendance_code(){
        return attendance_code;
    }

    public void setDatabase(String device_id, String username, String attendance_code){
        this.device_id = device_id;
        this.username = username;
        this.attendance_code = attendance_code;
    }

    void signIn(String username, String password){
        this.username = username;
        this.password = password;
    }

}
