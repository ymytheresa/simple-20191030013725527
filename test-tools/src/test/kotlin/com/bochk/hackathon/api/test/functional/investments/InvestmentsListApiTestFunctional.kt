package com.bochk.hackathon.api.test.functional.investments

import com.bochk.hackathon.api.functional.api.InvestmentsApi
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class InvestmentsListApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(InvestmentsApi::class.java)

    @Test
    fun `should return a list of investments for the account`() {
        val ibmAppUser = "cust3103"

        val response = api.investmentsGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val stocks = response.body()!!

        stocks.groupBy { it.code }.forEach { t, u -> assert(u.size <= 1) }

        assert(stocks.isNotEmpty())
        stocks.forEach({
            assertNotNull(it.code)
            assert(it.quantity >= 0)
        })
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"

        val response = api.investmentsGet(ibmAppUser).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

}
