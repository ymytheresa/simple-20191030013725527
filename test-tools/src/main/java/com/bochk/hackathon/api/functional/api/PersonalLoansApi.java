package com.bochk.hackathon.api.functional.api;

import com.bochk.hackathon.api.functional.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.functional.model.PersonalLoan;
import com.bochk.hackathon.api.functional.model.PersonalLoanApplicationRequest;
import com.bochk.hackathon.api.functional.model.PersonalLoanApplicationResponse;
import com.bochk.hackathon.api.functional.model.PersonalLoanCheckRateRequest;
import com.bochk.hackathon.api.functional.model.PersonalLoanCheckRateResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PersonalLoansApi {
  /**
   * Apply for personal loan
   * Personal loan is always in HKD
   * @param ibmAppUser  (required)
   * @param body  (required)
   * @return Call&lt;PersonalLoanApplicationResponse&gt;
   */
  @POST("personal-loans/applications")
  Call<PersonalLoanApplicationResponse> personalLoansApplicationsPost(
    @retrofit2.http.Header("ibm-app-user") String ibmAppUser, @retrofit2.http.Body PersonalLoanApplicationRequest body
  );

  /**
   * List personal loans
   * 
   * @param ibmAppUser  (required)
   * @return Call&lt;List&lt;PersonalLoan&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("personal-loans")
  Call<List<PersonalLoan>> personalLoansGet(
    @retrofit2.http.Header("ibm-app-user") String ibmAppUser
  );

  /**
   * Check personalized loan rate
   * 
   * @param ibmAppUser  (required)
   * @param body  (required)
   * @return Call&lt;PersonalLoanCheckRateResponse&gt;
   */
  @POST("personal-loans/personalized-rate")
  Call<PersonalLoanCheckRateResponse> personalLoansPersonalizedRatePost(
    @retrofit2.http.Header("ibm-app-user") String ibmAppUser, @retrofit2.http.Body PersonalLoanCheckRateRequest body
  );

}
