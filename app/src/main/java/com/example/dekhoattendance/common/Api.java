package com.example.dekhoattendance.common;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    public static String applink ="https://anytimehr.in/api/";
    public static String key="Access-Control-Request-Headers";
    public static String value="123456";

  /*  @Headers(key +":"+ value )
    @GET("index.php")
    Call<BannerImage> getBannerImage(@Query("route") String route);
    Call<BannerImage> addtoCart(@Query("route") String route);
    */

 /*@Headers({
          "Content-Type: application/json",
           key +":"+ value,
          "X-Oc-Session" +":"+ sess,
          "Cookie:language=en-gb; currency=INR; OCSESSID=658a7ba1cb3ca2757ff883d1ca",
  })*/

    @POST("company-registration.php")
    @FormUrlEncoded
    Call<String> company_registration(@FieldMap Map<String,String> reg_data);

    @POST("company-login.php")
    @FormUrlEncoded
    Call<String> company_login(@FieldMap Map<String,String> login_data);


    @POST("leave-type.php")
    Call<String> leave_type(@Query("company_id") String company_id);

    @POST("employee-detail.php")
    Call<String> employee_detail(@Query("employee_id") String employee_id, @Query("date") String date);

    @POST("mark_attendance.php")
    @FormUrlEncoded
    Call<String> mark_attendance(@FieldMap Map<String,String> mark_attendance);

    @POST("mark_attendance_api.php")
    Call<String> mark_attendancec(@Query("status") String status, @Query("address") String address,@Query("last_updated_by") String last_updated, @Query("emp_id") String id);



    @POST("employee-registration.php")
    @FormUrlEncoded
    Call<String> employee_registration(@FieldMap Map<String,String> reg_data);



    @POST("employee-attendance.php")
    @FormUrlEncoded
    Call<String> employee_attendance(@FieldMap Map<String,String> login_data);


    @POST("index.php")
    Call<String> SavebillAddress(@Query("route") String route, @Query("existing") String exist);


}
