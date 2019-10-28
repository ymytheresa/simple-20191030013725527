package com.bochk.hackathon.api.test.oauth.selfservice

import com.bochk.hackathon.api.oauth.api.HackathonSelfServiceApi
import com.bochk.hackathon.api.oauth.model.Body
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ResetDbApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(HackathonSelfServiceApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiClientCredentialsGrant = getApiService(HackathonSelfServiceApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiNoAuth = getApiService(HackathonSelfServiceApi::class.java)

    private val body = Body().areYouSure(Body.AreYouSureEnum.YES)

    @Disabled
    @Test
    fun `should accept if authorization_code grant is used`() {
        val response = api.internalResetDbPost(body).execute()

        assert(response.isSuccessful)
    }

    @Test
    fun `should reject if client_credentials grant is used`() {
        val response = apiClientCredentialsGrant.internalResetDbPost(body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.internalResetDbPost(body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
    }

}
