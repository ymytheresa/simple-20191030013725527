package com.bochk.hackathon.api.test.oauth.personalloans

import com.bochk.hackathon.api.oauth.api.PersonalLoansApi
import com.bochk.hackathon.api.oauth.model.Currency
import com.bochk.hackathon.api.oauth.model.PersonalLoanCheckRateRequest
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PersonalizedRateApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(PersonalLoansApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiClientCredentialsGrant = getApiService(PersonalLoansApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiNoAuth = getApiService(PersonalLoansApi::class.java)

    private val currency = Currency.HKD
    private val amount = 150000L
    private val noOfTerms = 24L
    private val remark = "remark"
    private val body = PersonalLoanCheckRateRequest()
            .currency(currency)
            .amount(amount)
            .noOfTerms(noOfTerms)
            .remark(remark)

    @Test
    fun `should accept if authorization_code grant is used`() {
        val response = api.personalLoansPersonalizedRatePost(body).execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if client_credentials grant is used`() {
        val response = apiClientCredentialsGrant.personalLoansPersonalizedRatePost(body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.personalLoansPersonalizedRatePost(body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
    }

}
