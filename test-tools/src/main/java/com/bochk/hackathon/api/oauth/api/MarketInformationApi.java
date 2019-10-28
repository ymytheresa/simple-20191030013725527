package com.bochk.hackathon.api.oauth.api;

import com.bochk.hackathon.api.oauth.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.oauth.model.FxRate;
import com.bochk.hackathon.api.oauth.model.StockPrice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MarketInformationApi {
  /**
   * Query currency exchange rate
   * To be designed by IBM
   * @param currency  (required)
   * @return Call&lt;FxRate&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("market-info/fx-rate")
  Call<FxRate> marketInfoFxRateGet(
    @retrofit2.http.Query("currency") String currency
  );

  /**
   * Query stock price
   * 
   * @param stockCode Stock code (required)
   * @return Call&lt;StockPrice&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("market-info/stock")
  Call<StockPrice> marketInfoStockGet(
    @retrofit2.http.Query("stock_code") String stockCode
  );

}
