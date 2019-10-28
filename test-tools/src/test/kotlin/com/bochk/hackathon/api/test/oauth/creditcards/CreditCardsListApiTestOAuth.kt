package com.bochk.hackathon.api.test.oauth.creditcards

import com.bochk.hackathon.api.oauth.api.CardOperationsApi
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreditCardsListApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(CardOperationsApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiClientCredentialsGrant = getApiService(CardOperationsApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiNoAuth = getApiService(CardOperationsApi::class.java)

    @Test
    fun `should accept if authorization_code grant is used`() {
        val response = api.creditCardsGet().execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if client_credentials grant is used`() {
        val response = apiClientCredentialsGrant.creditCardsGet().execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.creditCardsGet().execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
    }

}
