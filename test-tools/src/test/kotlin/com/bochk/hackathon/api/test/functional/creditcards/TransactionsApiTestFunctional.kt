package com.bochk.hackathon.api.test.functional.creditcards

import com.bochk.hackathon.api.functional.api.CardOperationsApi
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.ERROR_CREDIT_CARD_NOT_FOUND
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class TransactionsApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(CardOperationsApi::class.java)

    @Test
    fun `should return a list of transactions for the account in descending order of timestamp`() {
        val ibmAppUser = "cust3103"
        val cardNo = "4163180000002852"

        val response = api.creditCardsCardNoTransactionsGet(cardNo, ibmAppUser).execute()

        assert(response.isSuccessful)
        val transactions = response.body()!!
        assert(transactions.isNotEmpty())
        transactions.forEach({
            assertTrue(it.datetime < OffsetDateTime.now())
            assertEquals(cardNo, it.cardNo)
            assertNotNull(it.currency)
            assert(it.amount < 0.toDouble())
        })

        assertEquals(transactions.sortedByDescending { it.datetime }, transactions)
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"
        val cardNo = "x"

        val response = api.creditCardsCardNoTransactionsGet(cardNo, ibmAppUser).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if an invalid credit card no is specified`() {
        val ibmAppUser = "cust3103"
        val cardNo = "x"

        val response = api.creditCardsCardNoTransactionsGet(cardNo, ibmAppUser).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_CREDIT_CARD_NOT_FOUND, errorResponse.code)
    }

}
