package com.bochk.hackathon.api.oauth.api;

import com.bochk.hackathon.api.oauth.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.oauth.model.Stock;
import com.bochk.hackathon.api.oauth.model.StockTradeRequest;
import com.bochk.hackathon.api.oauth.model.StockTradeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InvestmentsApi {
  /**
   * List investments
   * 
   * @return Call&lt;List&lt;Stock&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("investments")
  Call<List<Stock>> investmentsGet();
    

  /**
   * Trade stock
   * 
   * @param body  (required)
   * @return Call&lt;StockTradeResponse&gt;
   */
  @POST("investments/stock")
  Call<StockTradeResponse> investmentsStockPost(
    @retrofit2.http.Body StockTradeRequest body
  );

}
