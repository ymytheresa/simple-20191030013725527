package com.bochk.hackathon.api.test.functional.personalloans

import com.bochk.hackathon.api.functional.api.PersonalLoansApi
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PersonalLoansListApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(PersonalLoansApi::class.java)

    @Test
    fun `should return a list of personal loans in ascending order of open date`() {
        val ibmAppUser = "cust3103"

        val response = api.personalLoansGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val personalLoans = response.body()!!
        personalLoans.forEach({
            assertTrue(it.openDate <= LocalDate.now())
            assertTrue(it.originalAmount > 0.toDouble())
            assertNotNull(it.currency)
            assertTrue(it.amount > 0.toDouble())
            assertTrue(it.noOfTerms > 0)
            assertTrue(it.rate > 0)
            assertTrue(it.installmentAmount > 0.toDouble())
            assertTrue(it.installmentDay in 1..31)
        })

        assertEquals(personalLoans.sortedBy { it.openDate }, personalLoans)
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"

        val response = api.personalLoansGet(ibmAppUser).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

}
