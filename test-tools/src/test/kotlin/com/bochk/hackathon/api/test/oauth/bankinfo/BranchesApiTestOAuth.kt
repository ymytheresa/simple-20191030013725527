package com.bochk.hackathon.api.test.oauth.bankinfo

import com.bochk.hackathon.api.oauth.api.BankInformationApi
import com.bochk.hackathon.api.test.ERROR_CLIENT_CRED_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BranchesApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(BankInformationApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiAuthorizationCodeGrant = getApiService(BankInformationApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiNoAuth = getApiService(BankInformationApi::class.java)

    @Test
    fun `should accept if client_credentials grant is used`() {
        val response = api.bankInfoBranchesGet().execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if authorization_code grant is used`() {
        val response = apiAuthorizationCodeGrant.bankInfoBranchesGet().execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_CLIENT_CRED_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.bankInfoBranchesGet().execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
    }

}
