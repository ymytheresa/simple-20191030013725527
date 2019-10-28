package com.bochk.hackathon.api.test.oauth.accounts

import com.bochk.hackathon.api.oauth.api.AccountOperationsApi
import com.bochk.hackathon.api.oauth.model.Currency
import com.bochk.hackathon.api.oauth.model.MoneyTransferRequest
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MoneyTransferApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(AccountOperationsApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiClientCredentialsGrant = getApiService(AccountOperationsApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiNoAuth = getApiService(AccountOperationsApi::class.java)

    private val srcAccountNo = "1268813333332"
    private val dstAccountNo = "1287513333331"
    private val currency = Currency.HKD
    private val amount = 10.5
    private val remark = "remark"
    private val body = MoneyTransferRequest()
            .currency(currency)
            .amount(amount)
            .dstAccountNo(dstAccountNo)
            .remark(remark)

    @Test
    fun `should accept if authorization_code grant is used`() {
        val response = api.accountsAccountNoMoneyTransferPost(srcAccountNo, body).execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should reject if client_credentials grant is used`() {
        val response = apiClientCredentialsGrant.accountsAccountNoMoneyTransferPost(srcAccountNo, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if no OAuth is used`() {
        val response = apiNoAuth.accountsAccountNoMoneyTransferPost(srcAccountNo, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
    }

}
