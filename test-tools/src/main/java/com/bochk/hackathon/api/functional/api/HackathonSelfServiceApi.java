package com.bochk.hackathon.api.functional.api;

import com.bochk.hackathon.api.functional.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.functional.model.Body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HackathonSelfServiceApi {
  /**
   * Reset database
   * Reset the API backend database to the factory default version.
   * @param ibmAppUser  (required)
   * @param body  (required)
   * @return Call&lt;Void&gt;
   */
  @POST("../internal/reset-db")
  Call<Void> internalResetDbPost(
    @retrofit2.http.Header("ibm-app-user") String ibmAppUser, @retrofit2.http.Body Body body
  );

}
