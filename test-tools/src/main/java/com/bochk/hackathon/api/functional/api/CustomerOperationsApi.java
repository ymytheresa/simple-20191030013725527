package com.bochk.hackathon.api.functional.api;

import com.bochk.hackathon.api.functional.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.functional.model.CustomerProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CustomerOperationsApi {
  /**
   * Get customer profile
   * 
   * @param ibmAppUser  (required)
   * @return Call&lt;CustomerProfile&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("customer/profile")
  Call<CustomerProfile> customerProfileGet(
    @retrofit2.http.Header("ibm-app-user") String ibmAppUser
  );

}
