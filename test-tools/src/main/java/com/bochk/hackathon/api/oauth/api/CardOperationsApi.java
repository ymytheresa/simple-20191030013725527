package com.bochk.hackathon.api.oauth.api;

import com.bochk.hackathon.api.oauth.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.oauth.model.CreditCard;
import com.bochk.hackathon.api.oauth.model.CreditCardPaymentRequest;
import com.bochk.hackathon.api.oauth.model.CreditCardPaymentResponse;
import com.bochk.hackathon.api.oauth.model.CreditCardTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CardOperationsApi {
  /**
   * Perform credit card payment
   * The amount is paid to the pre-registered bank account of the API client organization.
   * @param cardNo  (required)
   * @param body  (required)
   * @return Call&lt;CreditCardPaymentResponse&gt;
   */
  @POST("credit-cards/{card_no}/payment")
  Call<CreditCardPaymentResponse> creditCardsCardNoPaymentPost(
    @retrofit2.http.Path("card_no") String cardNo, @retrofit2.http.Body CreditCardPaymentRequest body
  );

  /**
   * List credit card transactions
   * 
   * @param cardNo  (required)
   * @return Call&lt;List&lt;CreditCardTransaction&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("credit-cards/{card_no}/transactions")
  Call<List<CreditCardTransaction>> creditCardsCardNoTransactionsGet(
    @retrofit2.http.Path("card_no") String cardNo
  );

  /**
   * List credit cards
   * 
   * @return Call&lt;List&lt;CreditCard&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("credit-cards")
  Call<List<CreditCard>> creditCardsGet();
    

}
