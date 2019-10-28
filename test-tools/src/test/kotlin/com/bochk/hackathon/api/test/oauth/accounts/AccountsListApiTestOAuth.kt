package com.bochk.hackathon.api.test.oauth.accounts

import com.bochk.hackathon.api.oauth.api.AccountOperationsApi
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AccountsListApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(AccountOperationsApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiClientCredentialsGrant = getApiService(AccountOperationsApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiNoAuth = getApiService(AccountOperationsApi::class.java)

    @Test
    fun `should accept if authorization_code grant is used`() {
        val response = api.accountsGet().execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if client_credentials grant is used`() {
        val response = apiClientCredentialsGrant.accountsGet().execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.accountsGet().execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
    }

}
