package com.bochk.hackathon.api.test.functional.creditcards

import com.bochk.hackathon.api.functional.api.CardOperationsApi
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate

class CreditCardsListApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(CardOperationsApi::class.java)

    @ParameterizedTest
    @ValueSource(strings = ["cust3103", "cust0101", "cust3003"])
    fun `should return a list of credit cards`(ibmAppUser: String) {
        val response = api.creditCardsGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val creditCards = response.body()!!
        assertEquals(3, creditCards.size)   // in principle, this function can return an empty list of the customer has no card

        creditCards.forEach {
            assertNotNull(it.cardType)
            assertNotNull(it.cardNo)
            assertTrue(it.openDate <= LocalDate.now())
            assertNotNull(it.currency)
            if (it.currency == Currency.USD) {
                assertEquals(100000.toDouble(), it.creditLimit)
            } else {
                assertEquals(500000.toDouble(), it.creditLimit)
            }
            assert(it.creditUsed in 0..500000)
        }
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"

        val response = api.creditCardsGet(ibmAppUser).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

}
