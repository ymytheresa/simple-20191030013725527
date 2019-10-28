package com.bochk.hackathon.api.oauth.api;

import com.bochk.hackathon.api.oauth.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.bochk.hackathon.api.oauth.model.AccountSummary;
import com.bochk.hackathon.api.oauth.model.AccountTransaction;
import com.bochk.hackathon.api.oauth.model.ForexRequest;
import com.bochk.hackathon.api.oauth.model.ForexResponse;
import com.bochk.hackathon.api.oauth.model.InlineResponse200;
import com.bochk.hackathon.api.oauth.model.MoneyTransferFpsRequest;
import com.bochk.hackathon.api.oauth.model.MoneyTransferFpsResponse;
import com.bochk.hackathon.api.oauth.model.MoneyTransferRequest;
import com.bochk.hackathon.api.oauth.model.MoneyTransferResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AccountOperationsApi {
  /**
   * Exchange currency
   * Amount calculations: * new src_amount &#x3D; src_amount - amount * (rate / 100) * new dst_amount &#x3D; dst_amount + amount
   * @param accountNo  (required)
   * @param body  (required)
   * @return Call&lt;ForexResponse&gt;
   */
  @POST("accounts/{account_no}/forex")
  Call<ForexResponse> accountsAccountNoForexPost(
    @retrofit2.http.Path("account_no") String accountNo, @retrofit2.http.Body ForexRequest body
  );

  /**
   * Transfer money (inter-bank, i.e. Faster Payment System)
   * 
   * @param accountNo  (required)
   * @param body  (required)
   * @return Call&lt;MoneyTransferFpsResponse&gt;
   */
  @POST("accounts/{account_no}/money-transfer-fps")
  Call<MoneyTransferFpsResponse> accountsAccountNoMoneyTransferFpsPost(
    @retrofit2.http.Path("account_no") String accountNo, @retrofit2.http.Body MoneyTransferFpsRequest body
  );

  /**
   * Transfer money (intra-bank)
   * 
   * @param accountNo  (required)
   * @param body  (required)
   * @return Call&lt;MoneyTransferResponse&gt;
   */
  @POST("accounts/{account_no}/money-transfer")
  Call<MoneyTransferResponse> accountsAccountNoMoneyTransferPost(
    @retrofit2.http.Path("account_no") String accountNo, @retrofit2.http.Body MoneyTransferRequest body
  );

  /**
   * List account transactions
   * 
   * @param accountNo  (required)
   * @return Call&lt;List&lt;AccountTransaction&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("accounts/{account_no}/transactions")
  Call<List<AccountTransaction>> accountsAccountNoTransactionsGet(
    @retrofit2.http.Path("account_no") String accountNo
  );

  /**
   * List accounts (with balances)
   * 
   * @return Call&lt;List&lt;AccountSummary&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("accounts")
  Call<List<AccountSummary>> accountsGet();
    

  /**
   * Retrieve the name of a 3rd party bank or FPS account
   * For display in confirmation dialog of money transfer.
   * @param accountInfoType  (required)
   * @param accountInfo  (required)
   * @return Call&lt;InlineResponse200&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("accounts/third-party-name")
  Call<InlineResponse200> accountsThirdPartyNameGet(
    @retrofit2.http.Query("account_info_type") String accountInfoType, @retrofit2.http.Query("account_info") String accountInfo
  );

}
