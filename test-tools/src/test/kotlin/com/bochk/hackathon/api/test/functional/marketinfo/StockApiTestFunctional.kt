package com.bochk.hackathon.api.test.functional.marketinfo

import com.bochk.hackathon.api.functional.api.MarketInformationApi
import com.bochk.hackathon.api.test.ERROR_INVALID_INPUT
import com.bochk.hackathon.api.test.ERROR_STOCK_NOT_FOUND
import com.bochk.hackathon.api.test.assertAll
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class StockApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(MarketInformationApi::class.java)

    @ParameterizedTest
    @ValueSource(strings = ["700", "0700"])
    fun `should return stock info of 700 and 0700`(stockCode: String) {
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.marketInfoStockGet(stockCode, ibmAppUser).execute()

        assert(response.isSuccessful)
        val stock = response.body()!!
        assertAll("response",
                { assertEquals("700", stock.stockCode) },
                { assertEquals("TENCENT", stock.nameEn) },
                { assertEquals("騰訊控股", stock.nameTc) },
                { assertEquals(394.2, stock.price) },
                { assertEquals(5.2, stock.change) },
                { assertEquals(1.34, stock.changePct) },
                { assertEquals(80.93, stock.peRatio) },
                { assertEquals(7989000000, stock.turnover) }
        )
    }

    @Test
    fun `should reject if non-existing stock code`() {
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.marketInfoStockGet("9999", ibmAppUser).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_STOCK_NOT_FOUND, errorResponse.code)
    }

    @Test
    fun `should reject if illegal stock code`() {
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.marketInfoStockGet("abc", ibmAppUser).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_INPUT, errorResponse.code)
    }

}
