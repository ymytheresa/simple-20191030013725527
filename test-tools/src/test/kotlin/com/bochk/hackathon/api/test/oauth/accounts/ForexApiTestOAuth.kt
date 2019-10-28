package com.bochk.hackathon.api.test.oauth.accounts

import com.bochk.hackathon.api.oauth.api.AccountOperationsApi
import com.bochk.hackathon.api.oauth.model.Currency
import com.bochk.hackathon.api.oauth.model.ForexRequest
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ForexApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(AccountOperationsApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiClientCredentialsGrant = getApiService(AccountOperationsApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiNoAuth = getApiService(AccountOperationsApi::class.java)

    private val srcAccountNo = "1287513333331"
    private val dstAccountNo = "1288213333333"
    private val srcCurrency = Currency.HKD
    private val dstCurrency = Currency.USD
    private val dstAmount = 10.5
    private val rate = 788.08
    private val remark = "remark"
    private val body = ForexRequest()
            .srcCurrency(srcCurrency)
            .dstAccountNo(dstAccountNo)
            .dstCurrency(dstCurrency)
            .dstAmount(dstAmount)
            .rate(rate)
            .remark(remark)

    @Test
    fun `should accept if authorization_code grant is used`() {
        val response = api.accountsAccountNoForexPost(srcAccountNo, body).execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if client_credentials grant is used`() {
        val response = apiClientCredentialsGrant.accountsAccountNoForexPost(srcAccountNo, body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.accountsAccountNoForexPost(srcAccountNo, body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
    }

}
