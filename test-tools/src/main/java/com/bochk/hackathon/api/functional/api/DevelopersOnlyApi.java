package com.bochk.hackathon.api.functional.api;

import com.bochk.hackathon.api.functional.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.functional.model.Body1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DevelopersOnlyApi {
  /**
   * Reset all databases
   * Reset all API backend databases to the factory default version.
   * @param body  (required)
   * @return Call&lt;Void&gt;
   */
  @POST("../internal/reset-all-db")
  Call<Void> internalResetAllDbPost(
    @retrofit2.http.Body Body1 body
  );

}
