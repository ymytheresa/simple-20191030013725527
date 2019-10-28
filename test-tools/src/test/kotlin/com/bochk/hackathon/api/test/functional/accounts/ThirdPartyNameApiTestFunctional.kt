package com.bochk.hackathon.api.test.functional.accounts

import com.bochk.hackathon.api.functional.api.AccountOperationsApi
import com.bochk.hackathon.api.functional.model.InlineResponse200
import com.bochk.hackathon.api.test.ERROR_ACCOUNT_NOT_FOUND
import com.bochk.hackathon.api.test.ERROR_CUSTOMER_MUST_BE_DIFFERENT
import com.bochk.hackathon.api.test.ERROR_INVALID_INPUT
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import retrofit2.Call

class ThirdPartyNameApiTestFunctional : FunctionalBaseTest() {

    enum class AccountInfoType {
        ACCOUNT_NO,
        FPS_PHONE_NO,
        FPS_EMAIL,
        FPS_ID,
    }

    private val api = getApiService(AccountOperationsApi::class.java)

    private fun accountsThirdPartyNameGet2(accountInfoType: AccountInfoType, accountInfo: String): Call<InlineResponse200> {
        val ibmAppUser = "cust3103"

        return api.accountsThirdPartyNameGet(accountInfoType.name, accountInfo, ibmAppUser)!!
    }

    @Test
    fun `should return the name of a third party given a valid FPS phone no`() {
        val response = accountsThirdPartyNameGet2(AccountInfoType.FPS_PHONE_NO, "62222222").execute()

        assert(response.isSuccessful)
        val info = response.body()!!
        assertEquals("Ko Chi Nang", info.name)
    }

    @Test
    fun `should return the name of a third party given a valid FPS email`() {
        val response = accountsThirdPartyNameGet2(AccountInfoType.FPS_EMAIL, "winwinwong@bochk.com").execute()

        assert(response.isSuccessful)
        val info = response.body()!!
        assertEquals("Wong Win Win", info.name)
    }

    @Test
    fun `should return the name of a third party given a valid FPS ID`() {
        val response = accountsThirdPartyNameGet2(AccountInfoType.FPS_ID, "FPS-C00001").execute()

        assert(response.isSuccessful)
        val info = response.body()!!
        assertEquals("SuperCorp Limited", info.name)
    }

    @Test
    fun `should return the name of a third party given a valid account no`() {
        val response = accountsThirdPartyNameGet2(AccountInfoType.ACCOUNT_NO, "1287511111111").execute()

        assert(response.isSuccessful)
        val info = response.body()!!
        assertEquals("KONG Chung Ngan", info.name)
    }

    @Test
    fun `should reject given an invalid FPS info type`() {
        val ibmAppUser = "cust3103"

        val response = api.accountsThirdPartyNameGet("x", "x", ibmAppUser).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_INPUT, errorResponse.code)
    }

    @ParameterizedTest
    @EnumSource(AccountInfoType::class)
    fun `should reject given an invalid FPS account info`(accountInfoType: AccountInfoType) {
        val response = accountsThirdPartyNameGet2(accountInfoType, "x").execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_ACCOUNT_NOT_FOUND, errorResponse.code)
    }

    @Test
    fun `should reject if the account number provided is under the same customer`() {
        val response = accountsThirdPartyNameGet2(AccountInfoType.ACCOUNT_NO, "1287513333331").execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_CUSTOMER_MUST_BE_DIFFERENT, errorResponse.code)
    }

}
