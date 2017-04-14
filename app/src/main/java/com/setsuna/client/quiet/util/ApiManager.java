package com.setsuna.client.quiet.util;

import com.setsuna.client.quiet.ModelSerializer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiManager {
    @FormUrlEncoded
    @POST("/SubmitAttendance.php")
    Call<ModelSerializer> insertDatabase(
            @Field("device_id") String device_id,
            @Field("username") String username,
            @Field("attendance_code") String attendance_code);

    @FormUrlEncoded
    @POST("/login.php")
    Call<ModelSerializer>signIn(
            @Field("username") String username,
            @Field("password") String password);

}

