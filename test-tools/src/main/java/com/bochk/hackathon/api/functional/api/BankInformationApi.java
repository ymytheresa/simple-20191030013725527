package com.bochk.hackathon.api.functional.api;

import com.bochk.hackathon.api.functional.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.functional.model.Atm;
import com.bochk.hackathon.api.functional.model.Branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BankInformationApi {
  /**
   * List ATMs
   * 
   * @param ibmAppUser  (required)
   * @return Call&lt;List&lt;Atm&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("bank-info/atms")
  Call<List<Atm>> bankInfoAtmsGet(
    @retrofit2.http.Header("ibm-app-user") String ibmAppUser
  );

  /**
   * List bank branches
   * 
   * @param ibmAppUser  (required)
   * @return Call&lt;List&lt;Branch&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("bank-info/branches")
  Call<List<Branch>> bankInfoBranchesGet(
    @retrofit2.http.Header("ibm-app-user") String ibmAppUser
  );

}
