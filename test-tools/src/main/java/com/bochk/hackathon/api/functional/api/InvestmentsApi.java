package com.bochk.hackathon.api.functional.api;

import com.bochk.hackathon.api.functional.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.functional.model.Stock;
import com.bochk.hackathon.api.functional.model.StockTradeRequest;
import com.bochk.hackathon.api.functional.model.StockTradeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InvestmentsApi {
  /**
   * List investments
   * 
   * @param ibmAppUser  (required)
   * @return Call&lt;List&lt;Stock&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("investments")
  Call<List<Stock>> investmentsGet(
    @retrofit2.http.Header("ibm-app-user") String ibmAppUser
  );

  /**
   * Trade stock
   * 
   * @param ibmAppUser  (required)
   * @param body  (required)
   * @return Call&lt;StockTradeResponse&gt;
   */
  @POST("investments/stock")
  Call<StockTradeResponse> investmentsStockPost(
    @retrofit2.http.Header("ibm-app-user") String ibmAppUser, @retrofit2.http.Body StockTradeRequest body
  );

}
