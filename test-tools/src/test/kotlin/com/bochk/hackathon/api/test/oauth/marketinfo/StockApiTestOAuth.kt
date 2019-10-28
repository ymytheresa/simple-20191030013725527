package com.bochk.hackathon.api.test.oauth.marketinfo

import com.bochk.hackathon.api.oauth.api.MarketInformationApi
import com.bochk.hackathon.api.test.ERROR_CLIENT_CRED_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class StockApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(MarketInformationApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiAuthorizationCodeGrant = getApiService(MarketInformationApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiNoAuth = getApiService(MarketInformationApi::class.java)

    private val stockCode = "0005"

    @Test
    fun `should accept if client_credentials grant is used`() {
        val response = api.marketInfoStockGet(stockCode).execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if authorization_code grant is used`() {
        val response = apiAuthorizationCodeGrant.marketInfoStockGet(stockCode).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_CLIENT_CRED_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.marketInfoStockGet(stockCode).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
    }

}
