package com.bochk.hackathon.api.test.oauth.creditcards

import com.bochk.hackathon.api.oauth.api.CardOperationsApi
import com.bochk.hackathon.api.oauth.model.CreditCardPaymentRequest
import com.bochk.hackathon.api.oauth.model.Currency
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PaymentApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(CardOperationsApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiClientCredentialsGrant = getApiService(CardOperationsApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiNoAuth = getApiService(CardOperationsApi::class.java)

    private val cardNo = "4163180000002852"
    private val currency = Currency.HKD
    private val amount = 5.toDouble()
    private val remark = "remark"
    private val body = CreditCardPaymentRequest()
            .currency(currency)
            .amount(amount)
            .remark(remark)

    @Test
    fun `should accept if authorization_code grant is used`() {
        val response = api.creditCardsCardNoPaymentPost(cardNo, body).execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if client_credentials grant is used`() {
        val response = apiClientCredentialsGrant.creditCardsCardNoPaymentPost(cardNo, body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.creditCardsCardNoPaymentPost(cardNo, body).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
    }

}
