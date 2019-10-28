package com.bochk.hackathon.api.oauth.api;

import com.bochk.hackathon.api.oauth.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.oauth.model.Atm;
import com.bochk.hackathon.api.oauth.model.Branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BankInformationApi {
  /**
   * List ATMs
   * 
   * @return Call&lt;List&lt;Atm&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("bank-info/atms")
  Call<List<Atm>> bankInfoAtmsGet();
    

  /**
   * List bank branches
   * 
   * @return Call&lt;List&lt;Branch&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("bank-info/branches")
  Call<List<Branch>> bankInfoBranchesGet();
    

}
