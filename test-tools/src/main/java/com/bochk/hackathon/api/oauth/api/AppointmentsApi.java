package com.bochk.hackathon.api.oauth.api;

import com.bochk.hackathon.api.oauth.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.oauth.model.Appointment;
import com.bochk.hackathon.api.oauth.model.AppointmentCreationRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AppointmentsApi {
  /**
   * Cancel appointment
   * 
   * @param phoneNo  (required)
   * @param appointmentId  (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @DELETE("services/appointments/{phone_no}/{appointment_id}")
  Call<Void> servicesAppointmentsPhoneNoAppointmentIdDelete(
    @retrofit2.http.Path("phone_no") String phoneNo, @retrofit2.http.Path("appointment_id") String appointmentId
  );

  /**
   * List appointments
   * 
   * @param phoneNo  (required)
   * @return Call&lt;List&lt;Appointment&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("services/appointments/{phone_no}")
  Call<List<Appointment>> servicesAppointmentsPhoneNoGet(
    @retrofit2.http.Path("phone_no") String phoneNo
  );

  /**
   * Create appointment
   * 
   * @param phoneNo  (required)
   * @param body  (required)
   * @return Call&lt;Appointment&gt;
   */
  @POST("services/appointments/{phone_no}")
  Call<Appointment> servicesAppointmentsPhoneNoPost(
    @retrofit2.http.Path("phone_no") String phoneNo, @retrofit2.http.Body AppointmentCreationRequest body
  );

}
