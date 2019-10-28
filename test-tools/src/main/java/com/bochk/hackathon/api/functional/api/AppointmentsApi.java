package com.bochk.hackathon.api.functional.api;

import com.bochk.hackathon.api.functional.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.functional.model.Appointment;
import com.bochk.hackathon.api.functional.model.AppointmentCreationRequest;
import com.bochk.hackathon.api.functional.model.ErrorResponse;

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
   * @param ibmAppUser  (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @DELETE("services/appointments/{phone_no}/{appointment_id}")
  Call<Void> servicesAppointmentsPhoneNoAppointmentIdDelete(
    @retrofit2.http.Path("phone_no") String phoneNo, @retrofit2.http.Path("appointment_id") String appointmentId, @retrofit2.http.Header("ibm-app-user") String ibmAppUser
  );

  /**
   * List appointments
   * 
   * @param phoneNo  (required)
   * @param ibmAppUser  (required)
   * @return Call&lt;List&lt;Appointment&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("services/appointments/{phone_no}")
  Call<List<Appointment>> servicesAppointmentsPhoneNoGet(
    @retrofit2.http.Path("phone_no") String phoneNo, @retrofit2.http.Header("ibm-app-user") String ibmAppUser
  );

  /**
   * Create appointment
   * 
   * @param phoneNo  (required)
   * @param ibmAppUser  (required)
   * @param body  (required)
   * @return Call&lt;Appointment&gt;
   */
  @POST("services/appointments/{phone_no}")
  Call<Appointment> servicesAppointmentsPhoneNoPost(
    @retrofit2.http.Path("phone_no") String phoneNo, @retrofit2.http.Header("ibm-app-user") String ibmAppUser, @retrofit2.http.Body AppointmentCreationRequest body
  );

}
