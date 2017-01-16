package org.sp.attendance.ats.nearby.reboot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Daniel on 15/1/2017.
 */

public interface ApiManager {
    @FormUrlEncoded
    @POST("/AttendanceDatabase.php")

    Call<DatabaseModel> insertDatabase(
            @Field("username") String username,
            @Field("device_id") String device_id,
            @Field("attendance_code") String attendance_code);
}