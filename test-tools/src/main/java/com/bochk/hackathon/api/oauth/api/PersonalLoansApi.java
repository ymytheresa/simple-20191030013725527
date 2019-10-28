package com.bochk.hackathon.api.oauth.api;

import com.bochk.hackathon.api.oauth.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.oauth.model.PersonalLoan;
import com.bochk.hackathon.api.oauth.model.PersonalLoanApplicationRequest;
import com.bochk.hackathon.api.oauth.model.PersonalLoanApplicationResponse;
import com.bochk.hackathon.api.oauth.model.PersonalLoanCheckRateRequest;
import com.bochk.hackathon.api.oauth.model.PersonalLoanCheckRateResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PersonalLoansApi {
  /**
   * Apply for personal loan
   * Personal loan is always in HKD
   * @param body  (required)
   * @return Call&lt;PersonalLoanApplicationResponse&gt;
   */
  @POST("personal-loans/applications")
  Call<PersonalLoanApplicationResponse> personalLoansApplicationsPost(
    @retrofit2.http.Body PersonalLoanApplicationRequest body
  );

  /**
   * List personal loans
   * 
   * @return Call&lt;List&lt;PersonalLoan&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("personal-loans")
  Call<List<PersonalLoan>> personalLoansGet();
    

  /**
   * Check personalized loan rate
   * 
   * @param body  (required)
   * @return Call&lt;PersonalLoanCheckRateResponse&gt;
   */
  @POST("personal-loans/personalized-rate")
  Call<PersonalLoanCheckRateResponse> personalLoansPersonalizedRatePost(
    @retrofit2.http.Body PersonalLoanCheckRateRequest body
  );

}
