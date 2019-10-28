package com.bochk.hackathon.api.test.oauth.accounts

import com.bochk.hackathon.api.oauth.api.AccountOperationsApi
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ThirdPartyNameApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(AccountOperationsApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiClientCredentialsGrant = getApiService(AccountOperationsApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiNoAuth = getApiService(AccountOperationsApi::class.java)

    private val accountInfoType = "FPS_PHONE_NO"
    private val accountInfo = "62222222"

    @Test
    fun `should accept if authorization_code grant is used`() {
        val response = api.accountsThirdPartyNameGet(accountInfoType, accountInfo).execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if client_credentials grant is used`() {
        val response = apiClientCredentialsGrant.accountsThirdPartyNameGet(accountInfoType, accountInfo).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.accountsThirdPartyNameGet(accountInfoType, accountInfo).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
    }

}
