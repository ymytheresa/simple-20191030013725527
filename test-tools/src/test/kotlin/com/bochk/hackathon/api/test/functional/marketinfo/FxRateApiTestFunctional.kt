package com.bochk.hackathon.api.test.functional.marketinfo

import com.bochk.hackathon.api.functional.api.MarketInformationApi
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.test.ERROR_INVALID_CURRENCY
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FxRateApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(MarketInformationApi::class.java)

    @Test
    fun `should return FX rate`() {
        val currency = Currency.USD
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.marketInfoFxRateGet(currency.value, ibmAppUser).execute()

        assert(response.isSuccessful)
        val fxRate = response.body()!!
        Assertions.assertEquals(currency, fxRate.currency)
        Assertions.assertEquals("United States Dollar", fxRate.description)
        Assertions.assertEquals(788.08, fxRate.rate)
    }

    @Test
    fun `should reject if currency is HKD`() {
        val currency = Currency.HKD
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.marketInfoFxRateGet(currency.value, ibmAppUser).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_INVALID_CURRENCY, errorResponse.code)
    }

}
