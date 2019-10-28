package com.bochk.hackathon.api.test.oauth.investments

import com.bochk.hackathon.api.oauth.api.InvestmentsApi
import com.bochk.hackathon.api.oauth.model.StockTradeRequest
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TradeStockApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(InvestmentsApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiClientCredentialsGrant = getApiService(InvestmentsApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiNoAuth = getApiService(InvestmentsApi::class.java)

    private val action = StockTradeRequest.ActionEnum.BUY
    private val stockCode = "0005"
    private val unitPrice = 78.85
    private val quantity = 10L
    private val settlementAccountNo = "1288213333333"
    private val remark = "this is a remark"
    private val body = StockTradeRequest()
            .action(action)
            .stockCode(stockCode)
            .unitPrice(unitPrice)
            .quantity(quantity)
            .settlementAccountNo(settlementAccountNo)
            .remark(remark)

    @Test
    fun `should accept if authorization_code grant is used`() {
        val response = api.investmentsStockPost(body).execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if client_credentials grant is used`() {
        val response = apiClientCredentialsGrant.investmentsStockPost(body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.investmentsStockPost(body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
    }

}
